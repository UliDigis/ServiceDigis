package com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.RestController;

import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.DAO.DireccionJPADAOImplementation;
import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.JPA.Result;
import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.JPA.DireccionJPA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/direccion")
public class DireccionRestController {

    @Autowired
    private DireccionJPADAOImplementation direccionJPADAOImplementation;

    Result result = new Result();

    @GetMapping("/id")
    public ResponseEntity<Result> GetByIdDireccion(@RequestParam("IdDireccion") int IdDireccion) {

        try {
            result = direccionJPADAOImplementation.GetByIdDireccion(IdDireccion);
            result.correct = true;
            result.status = 200;

        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getMessage();
            result.status = 500;
        }

        return ResponseEntity.status(result.status).body(result);
    }
    
    
    @DeleteMapping("/delete/direccion")
    public ResponseEntity<Result> DeleteDireccion(@RequestParam("IdDireccion") int IdDireccion){
        
        try{
            result = direccionJPADAOImplementation.DeleteDireccion(IdDireccion);
            result.correct = true;
            result.status=200;
        }catch(Exception ex){
            result.correct = false;
            result.errorMessage = ex.getMessage();
            result.status = 500;
        }
        return ResponseEntity.status(result.status).body(result);
    }
    
    @PostMapping("/add/{IdUsuario}")
    public ResponseEntity<Result> AddDireccion(@RequestBody DireccionJPA direccionJPA, @PathVariable("IdUsuario") int IdUsuario){
        
        try{
            
            result = direccionJPADAOImplementation.AddDireccion(direccionJPA, IdUsuario);
            result.correct = true;
            result.status = 202;
            
        }catch(Exception ex){
            result.correct = false;
            result.errorMessage = "Error inesperado: " + ex.getMessage();
            result.status = 500;
        }
        
        
        
        return ResponseEntity.status(result.status).body(result);
    }
    

}
