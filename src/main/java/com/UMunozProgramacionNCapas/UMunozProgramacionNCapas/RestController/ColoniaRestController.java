package com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.RestController;

import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.DAO.ColoniaJPADAOImplementation;
import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.JPA.Result;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/colonia")
public class ColoniaRestController {

    @Autowired
    private ColoniaJPADAOImplementation coloniaJPADAOImplementation;

    Result result = new Result();

    // Colonia
    @GetMapping("/municipio")
    public ResponseEntity<Result> GetByMunicipio(@RequestParam("IdMunicipio") int IdMunicipio) {

        try {
            result = coloniaJPADAOImplementation.GetByMunicipio(IdMunicipio);
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

    @GetMapping("/codigoPostal")
    public ResponseEntity<Result> GetByCodigoPostal(@RequestParam("idColonia") int IdColonia) {

        try {

            result = coloniaJPADAOImplementation.GetByCodigoPostal(IdColonia);
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

    // Colonia
}
