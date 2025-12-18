package com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.RestController;

import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.DAO.DireccionJPADAOImplementation;
import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.JPA.DireccionJPA;
import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.JPA.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/direccion") // ← con slash inicial
public class DireccionRestController {

    @Autowired
    private DireccionJPADAOImplementation direccionJPADAOImplementation;

    @GetMapping("/id")
    public ResponseEntity<Result> GetByIdDireccion(@RequestParam("IdDireccion") int IdDireccion) {
        Result result;
        try {
            result = direccionJPADAOImplementation.GetByIdDireccion(IdDireccion);
        } catch (Exception ex) {
            result = new Result();
            result.correct = false;
            result.errorMessage = ex.getMessage();
            result.status = 500;
        }
        int status = (result.status > 0 ? result.status : (result.correct ? 200 : 500));
        return ResponseEntity.status(status).body(result);
    }

    @DeleteMapping("/delete/direccion")
    public ResponseEntity<Result> DeleteDireccion(@RequestParam("IdDireccion") int IdDireccion) {
        Result result = new Result();
        try {
            result = direccionJPADAOImplementation.DeleteDireccion(IdDireccion);
        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getMessage();
            result.status = 500;
        }
        int status = (result.status > 0 ? result.status : (result.correct ? 200 : 500));
        return ResponseEntity.status(status).body(result);
    }

    @PostMapping("/add/{IdUsuario}")
    public ResponseEntity<Result> AddDireccion(@RequestBody DireccionJPA direccionJPA,
            @PathVariable("IdUsuario") int IdUsuario) {
        Result result;
        try {
            result = direccionJPADAOImplementation.AddDireccion(direccionJPA, IdUsuario);
        } catch (Exception ex) {
            result = new Result();
            result.correct = false;
            result.errorMessage = "Error inesperado: " + ex.getMessage();
            result.status = 500;
        }
        int status = (result.status > 0 ? result.status : (result.correct ? 201 : 500));
        return ResponseEntity.status(status).body(result);
    }
}
