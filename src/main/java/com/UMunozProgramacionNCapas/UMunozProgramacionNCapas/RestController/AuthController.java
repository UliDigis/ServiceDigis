package com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.RestController;

import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.DAO.IUsuarioRepositoryDAO;
import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.JPA.LoginDTO;
import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.JPA.Result;
import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.JPA.UsuarioJPA;
import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.Security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final IUsuarioRepositoryDAO iUsuarioRepositoryDAO;
    private final JwtUtil jwtUtil;

    public AuthController(AuthenticationManager authenticationManager,
                          IUsuarioRepositoryDAO iUsuarioRepositoryDAO,
                          JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.iUsuarioRepositoryDAO = iUsuarioRepositoryDAO;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<Result> Login(@RequestBody LoginDTO loginRequest) {
        Result result = new Result();

        try {
            if (loginRequest.getUserName() == null || loginRequest.getPassword() == null) {
                throw new BadCredentialsException("Faltan credenciales.");
            }

            
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUserName(),
                            loginRequest.getPassword()
                    )
            );

            UsuarioJPA usuarioBD = iUsuarioRepositoryDAO.findByUserName(loginRequest.getUserName());
            if (usuarioBD != null && !usuarioBD.isStatus()) {
                result.correct = false;
                result.errorMessage = "Usuario inactivo.";
                return ResponseEntity.status(403).body(result);
            }

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            
            String rolUsuario = userDetails.getAuthorities().stream()
                    .findFirst()
                    .map(item -> item.getAuthority())
                    .orElse("ROLE_USER");

            String token = jwtUtil.GenerateToken(userDetails.getUsername(), rolUsuario);

            
            
            String urlDestino = "/"; // Default
            
            if (rolUsuario.equals("ROLE_Administrador")) {
                urlDestino = "/usuario"; 
            } else if (rolUsuario.equals("ROLE_Cliente")) {
                urlDestino = "api/usuario/?id=" + usuarioBD.getIdUsuario(); 
            } else if (rolUsuario.equals("ROLE_Usuario")) {
                urlDestino = "api/usuario/?id=" + usuarioBD.getIdUsuario();
            }

            LoginDTO responseDTO = new LoginDTO();
            responseDTO.setToken(token);
            responseDTO.setRole(rolUsuario);
            responseDTO.setUserName(userDetails.getUsername());
            
            responseDTO.setRedirectUrl(urlDestino); 

            result.Object = responseDTO;
            result.correct = true;
            result.errorMessage = "Login Exitoso";

            return ResponseEntity.ok(result);

        } catch (BadCredentialsException e) {
            result.correct = false;
            result.errorMessage = "Usuario o contraseña incorrectos.";
            return ResponseEntity.status(401).body(result);

        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = "Error interno: " + ex.getMessage();
            result.ex = ex;
            return ResponseEntity.status(500).body(result);
        }
    }
}