package com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.RestController;

import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.DAO.RolJPADAOImplementation;
import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.DAO.UsuarioJPADAOImplementation;
import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.JPA.DireccionJPA;
import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.JPA.Result;
import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.JPA.UsuarioJPA;
import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.Service.EmailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("api/usuario")
public class UsuarioRestController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsuarioJPADAOImplementation usuarioJPADAOImplementation;

    @Autowired
    private RolJPADAOImplementation rolJPADAOImplementation;

    Result result = new Result();
    private static final String baseUrl = "http://localhost:8080";

    @GetMapping
    public ResponseEntity<Result> GetAll() {

        try {
            result = usuarioJPADAOImplementation.GetAllJPA();
            result.correct = true;
            result.status = 200;

        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
            result.status = 500;
        }

        return ResponseEntity.status(result.status).body(result);
    }

    @GetMapping("/")
    public ResponseEntity<Result> GetById(@RequestParam("id") int Id) {

        try {
            result = usuarioJPADAOImplementation.GetById(Id);
            result.correct = true;
            result.status = 200;

        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
            result.status = 500;
        }

        return ResponseEntity.status(result.status).body(result);
    }

    @PostMapping("/add")
    public ResponseEntity<Result> Add(@RequestBody UsuarioJPA usuarioJPA) {
        Result result = new Result();
        try {
            if (usuarioJPA == null) {
                result.correct = false;
                result.errorMessage = "El usuario llegó vacío o hubo un problema";
                result.status = 400;
                return ResponseEntity.status(result.status).body(result);
            }

            usuarioJPA.setPassword(passwordEncoder.encode(usuarioJPA.getPassword()));

            Result addResult = usuarioJPADAOImplementation.Add(usuarioJPA);
            if (addResult.correct) {
                Integer idGenerado = (addResult.Object instanceof Integer) ? (Integer) addResult.Object : 0;
                if (idGenerado > 0) {
                    Result getResult = usuarioJPADAOImplementation.GetById(idGenerado);
                    UsuarioJPA saved = getResult.correct ? (UsuarioJPA) getResult.Object : null;

                    emailService.sendVerificationEmail(idGenerado, usuarioJPA.getEmail(), baseUrl);

                    result.correct = true;
                    result.status = 201;
                    result.Object = (saved != null) ? saved : idGenerado;
                } else {
                    result.correct = false;
                    result.status = 500;
                    result.errorMessage = "No se pudo obtener el id generado";
                }
            } else {
                result.correct = false;
                result.status = 500;
                result.errorMessage = "No se pudo persistir el usuario";
            }

        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getMessage();
            result.status = 500;
        }
        return ResponseEntity.status(result.status).body(result);
    }

    @GetMapping("/search")
    public ResponseEntity<Result> SearchUsuarios(@RequestParam(required = false) String nombre,
            @RequestParam(required = false) String apellidoPaterno,
            @RequestParam(required = false) String apellidoMaterno,
            @RequestParam(required = false) Integer idRol,
            @RequestParam(required = false) Boolean status) {
        Result result = new Result();
        try {
            result = usuarioJPADAOImplementation.searchUsuario(nombre, apellidoPaterno, apellidoMaterno, idRol, status);
            result.correct = true;
            result.status = 200;
        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getMessage();
            result.status = 500;
        }
        return ResponseEntity.status(result.status).body(result);
    }

    @PutMapping("/update/{IdUsuario}")
    public ResponseEntity<Result> UpdateUsuario(@RequestBody UsuarioJPA usuarioJPA,
            @PathVariable("IdUsuario") int IdUsuario) {

        try {

            if (usuarioJPA.rol == null || usuarioJPA.rol.getIdRol() == 0) {
                result.correct = false;
                result.errorMessage = "Debes proporcionar un Rol válido.";
                result.status = 400;
                return ResponseEntity.status(result.status).body(result);
            }

            Result rolExistente = rolJPADAOImplementation.GetById(usuarioJPA.rol.getIdRol());
            if (!rolExistente.correct || rolExistente.Object == null) {
                result.correct = false;
                result.errorMessage = "El Rol con ID " + usuarioJPA.rol.getIdRol() + " no existe.";
                result.status = 404;
                return ResponseEntity.status(result.status).body(result);
            }

            result = usuarioJPADAOImplementation.Update(usuarioJPA, IdUsuario);
            result.correct = true;
            result.status = 200;

        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getMessage();
            result.ex = ex;
            result.status = 500;
        }

        return ResponseEntity.status(result.status).body(result);
    }

    @PutMapping("/{IdUsuario}/direccion/{IdDireccion}")
    public ResponseEntity<Result> UpdateDireccion(@PathVariable("IdUsuario") int IdUsuario,
            @PathVariable("IdDireccion") int IdDireccion,
            @RequestBody DireccionJPA direccionJPA) {

        Result result;

        try {
            result = usuarioJPADAOImplementation.UpdateDireccion(direccionJPA, IdUsuario, IdDireccion);
        } catch (Exception ex) {
            result = new Result();
            result.correct = false;
            result.errorMessage = ex.getMessage();
            result.ex = ex;
            result.status = 500;
        }

        return ResponseEntity.status(result.status).body(result);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Result> Delete(@RequestParam("IdUsuario") int IdUsuario) {

        try {
            result = usuarioJPADAOImplementation.DeleteUsuario(IdUsuario);
            result.Object = "El usuario fue eliminado correctamente";
            result.correct = true;
            result.status = 200;
        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getMessage();
            result.ex = ex;
            result.status = 500;
        }

        return ResponseEntity.status(result.status).body(result);
    }

    @GetMapping("/verify/{idUsuario}")
    public ResponseEntity<Result> VerifyUser(@PathVariable("idUsuario") int idUsuario) {
        Result result = usuarioJPADAOImplementation.verifyUser(idUsuario);

        if (result.correct) {
            result.status = 200;
            result.Object = "Usuario verificado exitosamente.";
            return ResponseEntity.ok(result);
        } else {
            result.status = result.status == 0 ? 500 : result.status;
            result.Object = "Error al verificar el usuario: " + result.errorMessage;
            return ResponseEntity.status(result.status).body(result);
        }
    }

}
