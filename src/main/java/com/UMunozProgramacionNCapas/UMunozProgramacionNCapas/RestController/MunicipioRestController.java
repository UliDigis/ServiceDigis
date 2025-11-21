package com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.RestController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.DAO.MunicipioJPADAOImplementation;
import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.JPA.Result;

@RestController
@RequestMapping("api/municipio")
public class MunicipioRestController {

    @Autowired
    private MunicipioJPADAOImplementation municipioJPADAOImplementation;

    Result result = new Result();

    //    Municipio
    @GetMapping("/estado")
    public ResponseEntity GetByEstado(@RequestParam("IdEstado") int IdEstado) {

        try {
            result = municipioJPADAOImplementation.GetByEstado(IdEstado);
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

//    Municipio

}
