package com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.DAO;

import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.JPA.Result;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

@Repository
public class DireccionJPADAOImplementation {
    
    Result result = new Result();
    
    public Result GetByIdDireccion(int IdDireccion){
      
        try{
            
            TypedQuery<DireccionJPA> query
            
        }catch(Exception ex){
            result.status = 500;
        }
        
        return result;
    }
    
}
