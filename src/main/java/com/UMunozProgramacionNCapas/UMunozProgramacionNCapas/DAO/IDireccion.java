
package com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.DAO;

import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.JPA.Result;
import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.JPA.DireccionJPA;

public interface IDireccion {

    Result GetByIdDireccion(int IdDireccion);
    
    Result DeleteDireccion(int IdDireccion);
    
    Result AddDireccion(DireccionJPA direccionJPA);
    
}
