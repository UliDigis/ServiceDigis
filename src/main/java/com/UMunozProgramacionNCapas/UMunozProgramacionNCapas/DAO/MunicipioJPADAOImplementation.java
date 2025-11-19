package com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.DAO;

import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.JPA.Result;
import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.JPA.MunicipioJPA;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class MunicipioJPADAOImplementation implements IMunicipioJPA{

    @Autowired
    private EntityManager entityManager;
    
    Result result = new Result();
   
    public Result GetByEstado(int IdEstado){
        
        try{
            
            TypedQuery<MunicipioJPA> queryMunicipio = entityManager.createQuery("FROM MunicipioJPA m WHERE m.estado.IdEstado = :IdEstado", MunicipioJPA.class);
            queryMunicipio.setParameter("IdEstado",IdEstado);
            
            List<MunicipioJPA> municipioJPA = queryMunicipio.getResultList();
            result.Object = municipioJPA;
                   
            result.correct = true;
            
        }catch(Exception ex){
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.Object = null;
        }
        
        return result;
    }
    
}
