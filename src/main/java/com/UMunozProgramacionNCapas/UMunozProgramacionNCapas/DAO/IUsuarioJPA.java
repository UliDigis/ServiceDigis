package com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.DAO;

import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.JPA.Result;
//import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.ML.Usuario;

public interface IUsuarioJPA {

    Result GetAllJPA();
    
//    Result AddUsuarioJPA(Usuario usuairo);
    
    Result GetById(int IdUsuario);
    
}
