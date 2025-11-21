package com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.RestController;

import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.JPA.Result;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/direccion/")
public class DireccionRestController {

    Result result = new Result();

    @GetMapping
    public ResponseEntity<Result> GetByIdDireccion(@RequestParam("IdDireccion") int IdDireccion) {

        try {
            
            
            
        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getMessage();
            result.status = 500;
        }

        return ResponseEntity.status(result.status).body(result);
    }

}
