package com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.RestController;

import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.DAO.DireccionJPADAOImplementation;
import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.DAO.UsuarioJPADAOImplementation;
import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.JPA.DireccionJPA;
import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.JPA.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
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
@RequestMapping("/api/direccion")
public class DireccionRestController {

    @Autowired
    private DireccionJPADAOImplementation direccionJPADAOImplementation;

    @Autowired
    private UsuarioJPADAOImplementation usuarioJPADAOImplementation;

    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping("/id")
    public ResponseEntity<Result> GetByIdDireccion(@RequestParam("IdDireccion") int IdDireccion) {
        Result result = new Result();
        try {
            result = direccionJPADAOImplementation.GetByIdDireccion(IdDireccion);
        } catch (Exception ex) {
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
    public ResponseEntity<Result> AddDireccion(@RequestBody Map<String, Object> payload,
            @PathVariable("IdUsuario") int IdUsuario) {

        Result result = new Result();

        try {
            if (payload == null || payload.isEmpty()) {
                result.correct = false;
                result.errorMessage = "La dirección llegó vacía.";
                result.status = 400;
            } else {

                Object idObj = payload.get("IdDireccion");
                if (idObj == null) {
                    idObj = payload.get("idDireccion");
                }
                if (idObj == null) {
                    idObj = payload.get("iddireccion");
                }

                int idDireccion = 0;
                if (idObj instanceof Number) {
                    idDireccion = ((Number) idObj).intValue();
                } else if (idObj != null) {
                    try {
                        idDireccion = Integer.parseInt(String.valueOf(idObj));
                    } catch (Exception ex) {
                        idDireccion = 0;
                    }
                }

                if (!payload.containsKey("idDireccion") && idObj != null) {
                    payload.put("idDireccion", idObj);
                }

                DireccionJPA direccionJPA = objectMapper.convertValue(payload, DireccionJPA.class);

                if (idDireccion > 0) {
                    result = usuarioJPADAOImplementation.UpdateDireccion(direccionJPA, IdUsuario, idDireccion);
                    if (result.status <= 0) {
                        result.status = (result.correct ? 200 : 500);
                    }
                } else {
                    result = direccionJPADAOImplementation.AddDireccion(direccionJPA, IdUsuario);
                    if (result.status <= 0) {
                        result.status = (result.correct ? 201 : 500);
                    }
                }
            }

        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = "Error inesperado: " + ex.getMessage();
            result.status = 500;
        }

        return ResponseEntity.status(result.status).body(result);
    }
}
