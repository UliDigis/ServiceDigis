package com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.DAO;

import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.JPA.Result;

public interface IRolJPA {

    Result GetAll();
    
    Result GetById(int IdRol);
    
}
