package com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.DAO;

import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.JPA.Result;
import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.JPA.DireccionJPA;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class DireccionJPADAOImplementation {
    
    @Autowired
    private EntityManager entityManager;
    
    Result result = new Result();
    
    public Result GetByIdDireccion(int IdDireccion){
        Result result = new Result();
        try{
            TypedQuery<DireccionJPA> query = entityManager.createQuery("FROM DireccionJPA d WHERE d.IdDireccion = :IdDireccion", DireccionJPA.class);
            query.setParameter("IdDireccion", IdDireccion);

            List<DireccionJPA> direccionJPA = query.getResultList();
            result.Object = direccionJPA;
            result.correct = true;

        }catch(Exception ex){
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
        }
        
        return result;
    }

    
    
}
