package com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.DAO;

import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.JPA.Result;
import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.JPA.PaisJPA;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
// import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class PaisJPADAOImplementation implements IPaisJPA {

    @Autowired
    private EntityManager entityManager;

    public Result GetAll() {
        Result result = new Result();

        try {
            TypedQuery<PaisJPA> queryPais = entityManager.createQuery("FROM PaisJPA ORDER BY IdPais", PaisJPA.class);

            List<PaisJPA> paisJPA = queryPais.getResultList();
            result.Object = paisJPA;

            result.correct = true;
        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.Objects = null;
        }

        return result;
    }

}
