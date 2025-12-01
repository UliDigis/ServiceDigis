package com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.Service;

import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.DAO.IUsuarioRepositoryDAO;
import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.JPA.UsuarioJPA;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsJPAService implements UserDetailsService {

    private final IUsuarioRepositoryDAO iUsuarioRepositoryDAO;

    public UserDetailsJPAService(IUsuarioRepositoryDAO iUsuarioRepositoryDAO) {
        this.iUsuarioRepositoryDAO = iUsuarioRepositoryDAO;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UsuarioJPA usuario = iUsuarioRepositoryDAO.findByUserName(username);

        if(usuario == null){
            throw new UsernameNotFoundException("Usuario no encontrado: " + username);
        }

        return User.withUsername(usuario.getUserName())
                .password(usuario.getPassword())
                .roles(usuario.rol.getNombre())   
                .disabled(!usuario.isStatus())     
                .build();
    }
}

