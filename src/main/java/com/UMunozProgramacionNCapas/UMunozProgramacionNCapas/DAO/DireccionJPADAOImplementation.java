package com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.DAO;

import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.JPA.ColoniaJPA;
import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.JPA.DireccionJPA;
import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.JPA.Result;
import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.JPA.UsuarioJPA;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class DireccionJPADAOImplementation implements IDireccion {

    @Autowired
    private EntityManager entityManager;

    @Override
    public Result GetByIdDireccion(int IdDireccion) {
        Result result = new Result();
        try {
            TypedQuery<DireccionJPA> query = entityManager.createQuery("FROM DireccionJPA d WHERE d.IdDireccion = :IdDireccion",
                    DireccionJPA.class);
            query.setParameter("IdDireccion", IdDireccion);

            List<DireccionJPA> list = query.getResultList();

            if (list != null && !list.isEmpty()) {
                result.Object = list.get(0); // devolver objeto, no lista
                result.correct = true;
                result.status = 200;
            } else {
                result.correct = false;
                result.status = 404;
                result.errorMessage = "Dirección no encontrada";
            }

        } catch (Exception ex) {
            result.correct = false;
            result.status = 500;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
        }

        return result;
    }

    @Transactional
    @Override
    public Result DeleteDireccion(int IdDireccion) {
        Result result = new Result();
        try {
            TypedQuery<DireccionJPA> query = entityManager.createQuery("FROM DireccionJPA d WHERE d.IdDireccion = :IdDireccion",
                    DireccionJPA.class);
            query.setParameter("IdDireccion", IdDireccion);

            List<DireccionJPA> list = query.getResultList();

            if (list != null && !list.isEmpty()) {
                DireccionJPA direccion = list.get(0);
                entityManager.remove(direccion);
                result.correct = true;
                result.status = 200;
            } else {
                result.correct = false;
                result.status = 404;
                result.errorMessage = "Dirección no encontrada";
            }
        } catch (Exception ex) {
            result.correct = false;
            result.status = 500;
            result.errorMessage = ex.getMessage();
            result.ex = ex;
        }
        return result;
    }

    @Override
    @Transactional
    public Result AddDireccion(DireccionJPA direccionJPA, int IdUsuario) {
        Result result = new Result();
        try {
            UsuarioJPA usuarioJPA = entityManager.find(UsuarioJPA.class, IdUsuario);
            if (usuarioJPA == null) {
                result.correct = false;
                result.status = 404;
                result.errorMessage = "Usuario no encontrado";
                return result;
            }

            if (direccionJPA == null || direccionJPA.colonia == null) {
                result.correct = false;
                result.status = 400;
                result.errorMessage = "Colonia requerida";
                return result;
            }

            ColoniaJPA coloniaJPA = entityManager.find(ColoniaJPA.class, direccionJPA.colonia.getIdColonia());
            if (coloniaJPA == null) {
                result.correct = false;
                result.status = 404;
                result.errorMessage = "Colonia no encontrada";
                return result;
            }

            direccionJPA.usuario = usuarioJPA;
            direccionJPA.colonia = coloniaJPA;

            entityManager.persist(direccionJPA);

            result.correct = true;
            result.status = 201;

        } catch (Exception ex) {
            result.correct = false;
            result.status = 500;
            result.errorMessage = "Error al agregar la dirección: " + ex.getMessage();
            result.ex = ex;
        }
        return result;
    }

    @Override
    @Transactional
    public Result UpdateDireccion(DireccionJPA direccionJPA, int IdUsuario) {
        Result result = new Result();
        try {
            if (direccionJPA == null) {
                result.correct = false;
                result.status = 400;
                result.errorMessage = "Dirección requerida";
                return result;
            }

            Integer idDir = null;
            try {
                idDir = direccionJPA.getIdDireccion();
            } catch (Exception ignored) {
            }
            if (idDir == null || idDir == 0) {
                result.correct = false;
                result.status = 400;
                result.errorMessage = "IdDireccion requerido para actualizar";
                return result;
            }

            DireccionJPA existente = entityManager.find(DireccionJPA.class, idDir);
            if (existente == null) {
                result.correct = false;
                result.status = 404;
                result.errorMessage = "Dirección no encontrada";
                return result;
            }

            UsuarioJPA usuarioJPA = entityManager.find(UsuarioJPA.class, IdUsuario);
            if (usuarioJPA == null) {
                result.correct = false;
                result.status = 404;
                result.errorMessage = "Usuario no encontrado";
                return result;
            }

            if (direccionJPA.colonia == null) {
                result.correct = false;
                result.status = 400;
                result.errorMessage = "Colonia requerida";
                return result;
            }

            ColoniaJPA coloniaJPA = entityManager.find(ColoniaJPA.class, direccionJPA.colonia.getIdColonia());
            if (coloniaJPA == null) {
                result.correct = false;
                result.status = 404;
                result.errorMessage = "Colonia no encontrada";
                return result;
            }

            existente.setCalle(direccionJPA.getCalle());
            existente.setNumeroExterior(direccionJPA.getNumeroExterior());
            existente.setNumeroInterior(direccionJPA.getNumeroInterior());

            existente.usuario = usuarioJPA;
            existente.colonia = coloniaJPA;

            entityManager.merge(existente);

            result.correct = true;
            result.status = 200;

        } catch (Exception ex) {
            result.correct = false;
            result.status = 500;
            result.errorMessage = "Error al actualizar la dirección: " + ex.getMessage();
            result.ex = ex;
        }

        return result;
    }
}
