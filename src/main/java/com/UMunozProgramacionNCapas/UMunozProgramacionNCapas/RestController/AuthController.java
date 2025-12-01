package com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.RestController;

import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.DAO.IUsuarioRepositoryDAO;
import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.JPA.Result;
import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.JPA.UsuarioJPA;
import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.Security.JwtUtil;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private IUsuarioRepositoryDAO iUsuarioRepositoryDAO;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<Result> Login(@RequestBody UsuarioJPA usuario) {
        Result result = new Result();

        if (usuario == null || usuario.getUserName() == null || usuario.getPassword() == null) {
            result.correct = false;
            result.errorMessage = "Faltan credenciales o el usuario esta desavilitado(usuario o contraseña).";
            result.status = 400;
            return ResponseEntity.status(result.status).body(result);
        }

        try {

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            usuario.getUserName(),
                            usuario.getPassword()
                    )
            );

            UsuarioJPA usuarioBD = iUsuarioRepositoryDAO.findByUserName(usuario.getUserName());

            if (usuarioBD.isStatus() == false) {
                result.correct = false;
                result.status = 403; 
                result.errorMessage = "Usuario inactivo, no puede iniciar sesión.";
                return ResponseEntity.status(result.status).body(result);
            }

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            String rolUsuario = userDetails.getAuthorities()
                    .stream()
                    .map(auth -> auth.getAuthority())
                    .findFirst()
                    .orElse("Sin_Rol");

            String token = jwtUtil.GenerateToken(userDetails.getUsername(), rolUsuario );

            result.Object = "Login exitoso. Usuario: " + usuario.getUserName() + " Su token generado es:" + token + " Sus rol es:" + rolUsuario;
            result.correct = true;
            result.status = 200;

        } catch (Exception ex){
            result.correct = false;
            result.errorMessage = "Error interno del servidor durante el login.";
            result.status = 500;
            result.ex = ex;
        }

        return ResponseEntity.status(result.status).body(result);
    }
}
