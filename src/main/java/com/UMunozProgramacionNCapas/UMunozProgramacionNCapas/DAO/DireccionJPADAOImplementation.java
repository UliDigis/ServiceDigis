package com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.DAO;

import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.JPA.Result;
import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.JPA.DireccionJPA;
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

    Result result = new Result();

    @Override
    public Result GetByIdDireccion(int IdDireccion) {
        Result result = new Result();
        try {
            TypedQuery<DireccionJPA> query = entityManager.createQuery("FROM DireccionJPA d WHERE d.IdDireccion = :IdDireccion", DireccionJPA.class);
            query.setParameter("IdDireccion", IdDireccion);

            List<DireccionJPA> direccionJPA = query.getResultList();
            result.Object = direccionJPA;
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
    public Result DeleteDireccion(int IdDireccion) {

        try {

            TypedQuery<DireccionJPA> query = entityManager.createQuery("FROM DireccionJPA d WHERE d.Direccion = :IdDireccion", DireccionJPA.class);
            query.setParameter("IdDireccion", IdDireccion);

            List<DireccionJPA> direccionJPA = query.getResultList();

            if (direccionJPA != null) {

                DireccionJPA direccion = direccionJPA.get(0);
                entityManager.remove(direccion);
                result.correct = true;
            } else {
                result.correct = false;
                result.errorMessage = "Direccion no encontrada";
                result.status = 204;
            }

        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getMessage();
        }
        return result;
    }

    @Transactional
    @Override
    public Result AddDireccion(DireccionJPA direccionJPA) {

        try {

            if (direccionJPA == null) {
                result.correct = false;
                result.errorMessage = "La calle es obligatoria";
                result.status = 400;
                return result;
            } else {

                DireccionJPA direccion = new DireccionJPA();
                direccion.setCalle(direccionJPA.getCalle().trim());
                direccion.setNumeroInterior(direccionJPA.getNumeroInterior() != null
                        ? direccionJPA.getNumeroInterior().trim() : null);
                direccion.setNumeroExterior(direccionJPA.getNumeroExterior() != null
                        ? direccionJPA.getNumeroExterior().trim() : null);
                direccion.colonia.setIdColonia(direccionJPA.colonia.getIdColonia());
                direccion.usuario.setIdUsuario(direccionJPA.usuario.getIdUsuario());
                entityManager.persist(direccionJPA);
                result.correct = true;
                result.status = 201;
            }

        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = "Error al agregar la dirección: " + ex.getMessage();
            result.status = 500;
        }
        return result;
    }

}
