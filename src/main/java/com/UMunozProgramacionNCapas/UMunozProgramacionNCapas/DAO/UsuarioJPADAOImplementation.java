package com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.DAO;

//import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.JPA.DireccionJPA;
import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.JPA.Result;
import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.JPA.UsuarioJPA;
//import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.Service.UsuarioMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

// import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Repository;
// import org.springframework.transaction.annotation.Transactional;

@Repository
public class UsuarioJPADAOImplementation implements IUsuarioJPA {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    // private UsuarioMapper usuarioMapper;

    @Override
    public Result GetAllJPA() {
        Result result = new Result();

        try {
            TypedQuery<UsuarioJPA> queryUsuario = entityManager.createQuery("FROM UsuarioJPA ORDER BY IdUsuario",
                    UsuarioJPA.class);
            List<UsuarioJPA> usuariosJPA = queryUsuario.getResultList();
            result.Object = usuariosJPA;

            result.correct = true;

        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
        }

        return result;
    }

    @Transactional
    @Override
    public Result Add(UsuarioJPA usuario) {
        Result result = new Result();

        try {

            if (usuario == null) {
                result.correct = false;
                result.errorMessage = "El usuario llego vacio o hubo un problema";
                result.status = 400;
            } else {

                entityManager.persist(usuario);

                result.correct = true;
                result.status = 201;
            }

        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getMessage();
            result.status = 500;
        }

        return result;
    }

    @Override
    public Result GetById(int IdUsuario) {
        Result result = new Result();

        try {
            TypedQuery<UsuarioJPA> query = entityManager.createQuery("FROM UsuarioJPA u WHERE u.IdUsuario = :IdUsuario",
                    UsuarioJPA.class);
            query.setParameter("IdUsuario", IdUsuario);

            List<UsuarioJPA> usuarioJPA = query.getResultList();

            result.Object = usuarioJPA;

            result.correct = true;

        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getMessage();
            result.ex = ex;
        }

        return result;
    }

    

    // @Override
    // @Transactional
    // public Result AddUsuarioJPA(Usuario usuario) {
    //
    // Result result = new Result();
    //
    // try {
    //// UsuarioJPA usuarioJPA = usuarioMapper.toEntity(usuario);
    //
    //// usuarioJPA.getDirecciones().get(0).setUsuario(usuarioJPA);
    //// entityManager.persist(usuarioJPA);
    //
    // result.correct = true;
    //// result.Object = usuarioJPA.getIdUsuario();
    //
    // } catch (Exception ex) {
    // result.correct = false;
    // result.errorMessage = ex.getLocalizedMessage();
    // result.ex = ex;
    //
    // }
    //
    // return result;
    // }
    // son pruebas
}
