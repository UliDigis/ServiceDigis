package com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.RestController;

import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.DAO.ColoniaJPADAOImplementation;
import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.DAO.EstadoJPADAOImplementation;
import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.DAO.MunicipioJPADAOImplementation;
import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.DAO.PaisJPADAOImplementation;
import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.DAO.RolJPADAOImplementation;
import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.DAO.UsuarioJPADAOImplementation;
import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.JPA.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/usuario")
public class UsuarioRestController {

    @Autowired
    private UsuarioJPADAOImplementation usuarioJPADAOImplementation;
    @Autowired
    private RolJPADAOImplementation rolJPADAOImplementation;
    @Autowired
    private PaisJPADAOImplementation paisJPADAOImplementation;
    @Autowired
    private EstadoJPADAOImplementation estadoJPADAOImplementation;
    @Autowired
    private MunicipioJPADAOImplementation municipioJPADAOImplementation;
    @Autowired
    private ColoniaJPADAOImplementation coloniaJPADAOImplementation;

    Result result = new Result();

//    Usuario
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

//    Usuario
//    Rol
    @GetMapping("/rol")
    public ResponseEntity GetAllRol() {

        try {
            result = rolJPADAOImplementation.GetAll();
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
//      Rol

//   Pais
    @GetMapping("/pais")
    public ResponseEntity GetAllPais() {

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
//    Pais

//    Estado
    @GetMapping("/estado/")
    public ResponseEntity GetByPais(@RequestParam("IdPais") int IdPais) {

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

//    Estado
//    Municipio
    @GetMapping("/municipio/")
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
//    Colonia
    @GetMapping("/colonia/")
    public ResponseEntity GetByMunicipio(@RequestParam("IdMunicipio") int IdMunicipio) {

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

//    Colonia
}
