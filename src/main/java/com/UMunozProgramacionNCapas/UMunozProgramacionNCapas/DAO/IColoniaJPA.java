package com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.DAO;

import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.JPA.Result;

public interface IColoniaJPA {    
    Result GetByMunicipio(int Municipio);
    
    Result GetByCodigoPostal(int IdColonia);
    
}
