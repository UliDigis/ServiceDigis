package com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.DAO;

import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.JPA.Result;
import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.JPA.EstadoJPA;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
// import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class EstadoJPADAOImplementation implements IEstadoJPA{

    @Autowired
    private EntityManager entityManager;
    
    Result result = new Result();
    
    public Result GetByPais(int IdPais){
        
        try{
            
        TypedQuery<EstadoJPA> queryEstado = entityManager.createQuery("FROM EstadoJPA e WHERE e.pais.IdPais = :IdPais", EstadoJPA.class);
        queryEstado.setParameter("IdPais", IdPais);
        
        List<EstadoJPA> estadoJPA = queryEstado.getResultList();
        
        result.Object = estadoJPA;
              
        result.correct = true;
        }catch(Exception ex){
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
        }
        return result;
    }
    
    
}
