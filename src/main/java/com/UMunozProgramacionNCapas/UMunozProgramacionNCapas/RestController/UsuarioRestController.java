package com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.RestController;

import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.DAO.RolJPADAOImplementation;
import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.DAO.UsuarioJPADAOImplementation;
import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.JPA.Result;
import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.JPA.UsuarioJPA;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    private UsuarioJPADAOImplementation usuarioJPADAOImplementation;

    @Autowired
    private RolJPADAOImplementation rolJPADAOImplementation;

    Result result = new Result();

    // Usuario
    @GetMapping
    public ResponseEntity GetAll() {

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
    public ResponseEntity GetById(@RequestParam("id") int Id) {

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
    public ResponseEntity<Result> Add(@RequestBody UsuarioJPA Usuario) {

        try {

            result = usuarioJPADAOImplementation.Add(Usuario);
            result.Object = "El usuario fue registrado correctamente";
            result.correct = true;
            result.status = 201;

        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getMessage();
            result.ex = ex;
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

        } catch (Exception ex) {
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

    // Usuario
}
