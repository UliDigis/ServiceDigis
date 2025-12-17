package com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.DAO;

import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.JPA.DireccionJPA;
import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.JPA.Result;
//import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.ML.Usuario;
import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.JPA.UsuarioJPA;

public interface IUsuarioJPA {

    Result GetAllJPA();

    Result Add(UsuarioJPA usuario);

    Result GetById(int IdUsuario);

    Result DeleteUsuario(int IdUsuario);
    
    Result Update(UsuarioJPA usuarioJPA, int IdUsuario);
    
    Result UpdateDireccion(DireccionJPA direccionJPA, int IdUsuario, int IdDireccion);
    
    Result searchUsuario(String nombre, String ap, String am, Integer idRol, Boolean status);

    Result verifyUser(int idUsuario);
    
}
