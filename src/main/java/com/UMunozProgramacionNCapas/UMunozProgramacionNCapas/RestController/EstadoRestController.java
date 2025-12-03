package com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.RestController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.DAO.EstadoJPADAOImplementation;
import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.JPA.Result;

@RestController
@RequestMapping("api/estado")
public class EstadoRestController {

    @Autowired
    private EstadoJPADAOImplementation estadoJPADAOImplementation;

    Result result = new Result();

    // Estado
    @GetMapping("/pais")
    public ResponseEntity<Result> GetByPais(@RequestParam("IdPais") int IdPais) {

        try {
            result = estadoJPADAOImplementation.GetByPais(IdPais);
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

    // Estado

}
