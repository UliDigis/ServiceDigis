package com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.DAO;


import org.springframework.data.jpa.repository.JpaRepository;

import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.JPA.UsuarioJPA;

public interface IUsuarioRepositoryDAO extends JpaRepository<UsuarioJPA, Integer> {
    
    UsuarioJPA findByUserName(String UserName);
}