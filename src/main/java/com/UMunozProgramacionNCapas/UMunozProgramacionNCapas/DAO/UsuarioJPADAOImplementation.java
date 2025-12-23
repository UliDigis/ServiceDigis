package com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.DAO;

import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.JPA.*;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UsuarioJPADAOImplementation implements IUsuarioJPA {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Result GetAllJPA() {
        Result result = new Result();
        try {
            TypedQuery<UsuarioJPA> queryUsuarios = entityManager.createQuery(
                    "FROM UsuarioJPA ORDER BY IdUsuario", UsuarioJPA.class);
            List<UsuarioJPA> listaUsuarios = queryUsuarios.getResultList();
            result.Object = listaUsuarios;
            result.correct = true;
            result.status = 200;
        } catch (Exception exception) {
            result.correct = false;
            result.status = 500;
            result.errorMessage = exception.getMessage();
            result.ex = exception;
        }
        return result;
    }

    @Transactional
    @Override
    public Result Add(UsuarioJPA usuarioJPA) {
        Result result = new Result();
        try {
            if (usuarioJPA == null) {
                result.correct = false;
                result.status = 400;
                result.errorMessage = "El usuario llegó vacío o hubo un problema";
                return result;
            }

            usuarioJPA.setVerified(false);

            UsuarioJPA usuarioPersistido;
            if (usuarioJPA.getDirecciones() == null || usuarioJPA.getDirecciones().isEmpty()) {
                entityManager.persist(usuarioJPA);
                entityManager.flush();
                usuarioPersistido = usuarioJPA;
            } else {
                for (DireccionJPA direccion : usuarioJPA.getDirecciones()) {
                    direccion.setUsuario(usuarioJPA);
                }
                usuarioPersistido = entityManager.merge(usuarioJPA);
                entityManager.flush();
            }

            result.correct = true;
            result.status = 201;
            result.Object = usuarioPersistido.getIdUsuario();

        } catch (Exception exception) {
            result.correct = false;
            result.status = 500;
            result.errorMessage = exception.getMessage();
            result.ex = exception;
        }

        return result;
    }

    @Override
    public Result GetById(int idUsuario) {
        Result result = new Result();
        try {
            UsuarioJPA usuarioEncontrado = entityManager.find(UsuarioJPA.class, idUsuario);
            result.Object = usuarioEncontrado;
            result.correct = true;
            if (usuarioEncontrado != null) {
                result.status = 200;
            } else {
                result.status = 404;
                result.errorMessage = "Usuario no encontrado";
            }
        } catch (Exception exception) {
            result.correct = false;
            result.status = 500;
            result.errorMessage = exception.getMessage();
            result.ex = exception;
        }
        return result;
    }

    @Transactional
    @Override
    public Result Update(UsuarioJPA usuarioJPA, int idUsuario) {
        Result result = new Result();
        try {
            UsuarioJPA usuarioEnBase = entityManager.find(UsuarioJPA.class, idUsuario);

            if (usuarioEnBase == null) {
                result.correct = false;
                result.status = 404;
                result.errorMessage = "Usuario no encontrado";
                return result;
            }

            usuarioJPA.setPassword(usuarioEnBase.getPassword());

            usuarioEnBase.setUserName(usuarioJPA.getUserName());
            usuarioEnBase.setNombre(usuarioJPA.getNombre());
            usuarioEnBase.setApellidoPaterno(usuarioJPA.getApellidoPaterno());
            usuarioEnBase.setApellidoMaterno(usuarioJPA.getApellidoMaterno());
            usuarioEnBase.setEmail(usuarioJPA.getEmail());
            usuarioEnBase.setPassword(usuarioJPA.getPassword());
            usuarioEnBase.setTelefono(usuarioJPA.getTelefono());
            usuarioEnBase.setCelular(usuarioJPA.getCelular());
            usuarioEnBase.setCURP(usuarioJPA.getCURP());
            usuarioEnBase.setSexo(usuarioJPA.getSexo());
            usuarioEnBase.setFechaNacimiento(usuarioJPA.getFechaNacimiento());
            usuarioEnBase.setImagen(usuarioJPA.getImagen());

            if (usuarioJPA.rol != null && usuarioJPA.rol.getIdRol() > 0) {
                RolJPA rolReferencia = entityManager.getReference(RolJPA.class, usuarioJPA.rol.getIdRol());
                usuarioEnBase.rol = rolReferencia;
            }

            entityManager.merge(usuarioEnBase);

            result.correct = true;
            result.status = 200;

        } catch (Exception exception) {
            result.correct = false;
            result.status = 500;
            result.errorMessage = exception.getMessage();
            result.ex = exception;
        }

        return result;
    }

    @Transactional
    @Override
    public Result UpdateDireccion(DireccionJPA direccionJPA, int idUsuario, int idDireccion) {
        Result result = new Result();

        try {
            UsuarioJPA usuarioEnBase = entityManager.find(UsuarioJPA.class, idUsuario);
            if (usuarioEnBase == null) {
                result.correct = false;
                result.status = 404;
                result.errorMessage = "Usuario no encontrado";
                return result;
            }

            DireccionJPA direccionEnBase = entityManager.find(DireccionJPA.class, idDireccion);
            if (direccionEnBase == null) {
                result.correct = false;
                result.status = 404;
                result.errorMessage = "Dirección no encontrada";
                return result;
            }

            if (direccionEnBase.getUsuario() == null || direccionEnBase.getUsuario().getIdUsuario() != idUsuario) {
                result.correct = false;
                result.status = 400;
                result.errorMessage = "La dirección no pertenece al usuario especificado";
                return result;
            }

            direccionEnBase.setCalle(direccionJPA.getCalle());
            direccionEnBase.setNumeroInterior(direccionJPA.getNumeroInterior());
            direccionEnBase.setNumeroExterior(direccionJPA.getNumeroExterior());

            if (direccionJPA.getColonia() != null && direccionJPA.getColonia().getIdColonia() > 0) {
                ColoniaJPA coloniaReferencia = entityManager.getReference(
                        ColoniaJPA.class, direccionJPA.getColonia().getIdColonia());
                direccionEnBase.setColonia(coloniaReferencia);
            }

            entityManager.merge(direccionEnBase);

            result.correct = true;
            result.status = 200;
            result.Object = "Dirección actualizada correctamente";

        } catch (Exception exception) {
            result.correct = false;
            result.status = 500;
            result.errorMessage = exception.getMessage();
            result.ex = exception;
        }

        return result;
    }

    @Transactional
    @Override
    public Result DeleteUsuario(int idUsuario) {
        Result result = new Result();

        try {
            UsuarioJPA usuarioEnBase = entityManager.find(UsuarioJPA.class, idUsuario);
            if (usuarioEnBase == null) {
                result.correct = false;
                result.status = 404;
                result.errorMessage = "Usuario no encontrado";
                return result;
            }

            TypedQuery<DireccionJPA> queryDirecciones = entityManager.createQuery(
                    "FROM DireccionJPA direccion WHERE direccion.usuario.IdUsuario = :IdUsuario",
                    DireccionJPA.class);
            queryDirecciones.setParameter("IdUsuario", idUsuario);
            List<DireccionJPA> listaDirecciones = queryDirecciones.getResultList();

            for (DireccionJPA direccion : listaDirecciones) {
                entityManager.remove(direccion);
            }

            entityManager.remove(usuarioEnBase);

            result.correct = true;
            result.status = 202;
            result.Object = "Usuario eliminado correctamente";

        } catch (Exception exception) {
            result.correct = false;
            result.status = 500;
            result.errorMessage = exception.getMessage();
            result.ex = exception;
        }

        return result;
    }

    @Override
    public Result searchUsuario(String nombre, String apellidoPaterno, String apellidoMaterno, Integer idRol, Boolean status) {
        Result result = new Result();

        try {
            StringBuilder jpql = new StringBuilder(
                    "SELECT DISTINCT usuario FROM UsuarioJPA usuario LEFT JOIN usuario.rol rol WHERE 1=1");

            if (nombre != null && !nombre.isBlank()) {
                jpql.append(" AND LOWER(usuario.Nombre) LIKE LOWER(:nombre)");
            }
            if (apellidoPaterno != null && !apellidoPaterno.isBlank()) {
                jpql.append(" AND LOWER(usuario.ApellidoPaterno) LIKE LOWER(:apellidoPaterno)");
            }
            if (apellidoMaterno != null && !apellidoMaterno.isBlank()) {
                jpql.append(" AND LOWER(usuario.ApellidoMaterno) LIKE LOWER(:apellidoMaterno)");
            }
            if (idRol != null && idRol > 0) {
                jpql.append(" AND rol.idRol = :idRol");
            }
            if (status != null) {
                jpql.append(" AND usuario.Status = :status");
            }

            TypedQuery<UsuarioJPA> queryBusqueda = entityManager.createQuery(jpql.toString(), UsuarioJPA.class);

            if (nombre != null && !nombre.isBlank()) {
                queryBusqueda.setParameter("nombre", "%" + nombre.trim() + "%");
            }
            if (apellidoPaterno != null && !apellidoPaterno.isBlank()) {
                queryBusqueda.setParameter("apellidoPaterno", "%" + apellidoPaterno.trim() + "%");
            }
            if (apellidoMaterno != null && !apellidoMaterno.isBlank()) {
                queryBusqueda.setParameter("apellidoMaterno", "%" + apellidoMaterno.trim() + "%");
            }
            if (idRol != null && idRol > 0) {
                queryBusqueda.setParameter("idRol", idRol);
            }
            if (status != null) {
                queryBusqueda.setParameter("status", status);
            }

            result.Object = queryBusqueda.getResultList();
            result.correct = true;
            result.status = 200;

        } catch (Exception exception) {
            result.correct = false;
            result.status = 500;
            result.errorMessage = exception.getMessage();
            result.ex = exception;
        }

        return result;
    }

    @Transactional
    @Override
    public Result verifyUser(int idUsuario) {
        Result result = new Result();

        try {
            int filasAfectadas = entityManager.createQuery(
                            "UPDATE UsuarioJPA usuario SET usuario.Verified = true WHERE usuario.IdUsuario = :idUsuario")
                    .setParameter("idUsuario", idUsuario)
                    .executeUpdate();

            result.correct = filasAfectadas > 0;
            result.status = result.correct ? 200 : 404;
            if (!result.correct) {
                result.errorMessage = "No se encontró el usuario.";
            }

        } catch (Exception exception) {
            result.correct = false;
            result.status = 500;
            result.errorMessage = exception.getMessage();
            result.ex = exception;
        }

        return result;
    }

    @Transactional
    @Override
    public Result CargaMasiva(List<UsuarioJPA> listaUsuarios) {

        if (listaUsuarios == null || listaUsuarios.isEmpty()) {
            Result result = new Result();
            result.correct = false;
            result.status = 400;
            result.errorMessage = "La lista de usuarios viene vacía";
            return result;
        }

        int totalInsertados = 0;
        int batchSize = 50;

        for (int indice = 0; indice < listaUsuarios.size(); indice++) {

            int numeroFila = indice + 1;
            UsuarioJPA usuarioNuevo = listaUsuarios.get(indice);

            try {
                usuarioNuevo.setVerified(false);

                if (usuarioNuevo.rol == null || usuarioNuevo.rol.getIdRol() <= 0) {
                    throw new IllegalArgumentException("Fila " + numeroFila + ": idRol inválido.");
                }

                RolJPA rolReferencia = entityManager.getReference(RolJPA.class, usuarioNuevo.rol.getIdRol());
                usuarioNuevo.rol = rolReferencia;

                if (usuarioNuevo.getDirecciones() != null && !usuarioNuevo.getDirecciones().isEmpty()) {
                    for (DireccionJPA direccionNueva : usuarioNuevo.getDirecciones()) {
                        direccionNueva.setUsuario(usuarioNuevo);

                        ColoniaJPA coloniaReferencia = resolverColoniaReferencia(direccionNueva.getColonia(), numeroFila);
                        direccionNueva.setColonia(coloniaReferencia);
                    }

                    entityManager.merge(usuarioNuevo);
                } else {
                    entityManager.persist(usuarioNuevo);
                }

                totalInsertados++;

                if (totalInsertados % batchSize == 0) {
                    entityManager.flush();
                    entityManager.clear();
                }

            } catch (Exception exception) {
                entityManager.flush();
                throw new RuntimeException("Error en carga masiva. " + obtenerMensajeCausaRaiz(exception), exception);
            }
        }

        entityManager.flush();
        entityManager.clear();

        Result result = new Result();
        result.correct = true;
        result.status = 200;

        Map<String, Object> payload = new HashMap<>();
        payload.put("insertados", totalInsertados);
        result.Object = payload;

        return result;
    }

    private ColoniaJPA resolverColoniaReferencia(ColoniaJPA coloniaArchivo, int numeroFila) {

        if (coloniaArchivo == null) {
            throw new IllegalArgumentException("Fila " + numeroFila + ": colonia requerida.");
        }

        if (coloniaArchivo.getIdColonia() > 0) {
            return entityManager.getReference(ColoniaJPA.class, coloniaArchivo.getIdColonia());
        }

        String nombreColonia = coloniaArchivo.getNombre() != null ? coloniaArchivo.getNombre().trim() : "";
        String codigoPostal = coloniaArchivo.getCodigoPostal() != null ? coloniaArchivo.getCodigoPostal().trim() : "";

        if (nombreColonia.isBlank() || codigoPostal.isBlank()) {
            throw new IllegalArgumentException("Fila " + numeroFila + ": colonia y códigoPostal requeridos.");
        }

        TypedQuery<ColoniaJPA> queryColonia = entityManager.createQuery(
                "SELECT colonia FROM ColoniaJPA colonia " +
                        "WHERE LOWER(colonia.Nombre) = LOWER(:nombreColonia) " +
                        "AND colonia.CodigoPostal = :codigoPostal",
                ColoniaJPA.class
        );

        queryColonia.setParameter("nombreColonia", nombreColonia);
        queryColonia.setParameter("codigoPostal", codigoPostal);

        List<ColoniaJPA> listaColonias = queryColonia.getResultList();
        if (listaColonias == null || listaColonias.isEmpty()) {
            throw new IllegalArgumentException(
                    "Fila " + numeroFila + ": no existe Colonia en BD para colonia='" + nombreColonia + "' y cp='" + codigoPostal + "'."
            );
        }

        return listaColonias.get(0);
    }

    private String obtenerMensajeCausaRaiz(Throwable throwable) {
        Throwable causa = throwable;
        while (causa.getCause() != null && causa.getCause() != causa) {
            causa = causa.getCause();
        }
        String mensaje = causa.getMessage();
        return (mensaje == null || mensaje.isBlank()) ? causa.toString() : mensaje;
    }
}
