package com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.DAO;

import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.JPA.Result;

public interface IMunicipioJPA {
    Result GetByEstado(int IdEstado);
    
}
