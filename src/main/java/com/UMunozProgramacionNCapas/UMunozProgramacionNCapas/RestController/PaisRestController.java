package com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.RestController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.DAO.PaisJPADAOImplementation;
import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.JPA.Result;

@RestController
@RequestMapping("api/pais")
public class PaisRestController {

    @Autowired
    private PaisJPADAOImplementation paisJPADAOImplementation;

    Result result = new Result();

    // Pais
    @GetMapping()
    public ResponseEntity<Result> GetAllPais() {

        try {
            result = paisJPADAOImplementation.GetAll();
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
    // Pais

}
