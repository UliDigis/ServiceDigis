
package com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.DAO;

import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.JPA.Result;

public interface IDireccion {

    Result GetByIdDireccion(int IdDireccion);
    
    Result DeleteDireccion(int IdDireccion);
    
}
