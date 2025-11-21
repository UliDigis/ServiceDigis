package com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.DAO;

import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.JPA.Result;
import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.JPA.ColoniaJPA;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ColoniaJPADAOImplementation {
    
    @Autowired
    private EntityManager entityManager;
    
    Result result = new Result();
    
    
    public Result GetByMunicipio(int IdMunicipio){
        
        try{
        TypedQuery<ColoniaJPA> queryColonia = entityManager.createQuery("FROM ColoniaJPA c WHERE c.municipio.IdMunicipio = :IdMunicipio", ColoniaJPA.class);
        queryColonia.setParameter("IdMunicipio",IdMunicipio);
        
        List<ColoniaJPA> coloniaJPA= queryColonia.getResultList();
        result.Object = coloniaJPA;
        
        result.correct = true;
        
        }catch(Exception ex){
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
        }
        
        
        return result;
    }
    
    public Result GetByCodigoPostal(int IdColonia){
        
        try{
            
            TypedQuery<String> queryCodigoPostal = entityManager.createQuery("SELECT c.CodigoPostal FROM ColoniaJPA c WHERE c.IdColonia = :IdColonia", String.class);
            queryCodigoPostal.setParameter("IdColonia", IdColonia);
            
            List<String> coloniaJPA = queryCodigoPostal.getResultList();
            result.Object = coloniaJPA;
            result.correct = true;
            
            
        }catch(Exception ex){
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
        }
        
        return result;
    }
    
}
