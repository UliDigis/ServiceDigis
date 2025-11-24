package com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.DAO;

import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.JPA.Result;
import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.JPA.RolJPA;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
// import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class RolJPADAOImplementation implements IRolJPA {

    @Autowired
    private EntityManager entityManager;

    @Autowired
//    private RolMapper rolMapper;

    @Override
    public Result GetAll() {
        Result result = new Result();

        try {

            TypedQuery<RolJPA> queryRol = entityManager.createQuery("FROM RolJPA ORDER BY IdRol", RolJPA.class);

            List<RolJPA> rolJPA = queryRol.getResultList();
            result.Object = rolJPA;

            result.correct = true;

        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
        }

        return result;
    }
    
    @Override
    public Result GetById(int idRol) {
        Result result = new Result();

        try {
            RolJPA rol = entityManager.find(RolJPA.class, idRol);

            if (rol != null) {
                result.Object = rol;
                result.correct = true;
            } else {
                result.correct = false;
                result.errorMessage = "No se encontró el Rol con Id: " + idRol;
            }

        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
        }

        return result;
    }

}
