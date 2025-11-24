package com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.DAO;

//import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.JPA.DireccionJPA;
import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.JPA.Result;
import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.JPA.DireccionJPA;
import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.JPA.UsuarioJPA;
import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.JPA.ColoniaJPA;
import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.JPA.RolJPA;
import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.JPA.MunicipioJPA;
import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.JPA.EstadoJPA;
import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.JPA.PaisJPA;
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

    @Transactional
    @Override
    public Result Update(UsuarioJPA usuarioJPA, int IdUsuario) {
        Result result = new Result();
        try {

            UsuarioJPA usuarioDB = entityManager.find(UsuarioJPA.class, IdUsuario);

            if (usuarioDB == null) {
                result.correct = false;
                result.status = 404;
                result.errorMessage = "Usuario no encontrado";
                return result;
            } else {

                usuarioDB.setUserName(usuarioJPA.getUserName());
                usuarioDB.setNombre(usuarioJPA.getNombre());
                usuarioDB.setApellidoPaterno(usuarioJPA.getApellidoPaterno());
                usuarioDB.setApellidoMaterno(usuarioJPA.getApellidoMaterno());
                usuarioDB.setEmail(usuarioJPA.getEmail());
                usuarioDB.setPassword(usuarioJPA.getPassword());
                usuarioDB.setTelefono(usuarioJPA.getTelefono());
                usuarioDB.setCelular(usuarioJPA.getCelular());
                usuarioDB.setCURP(usuarioJPA.getCURP());
                usuarioDB.setSexo(usuarioJPA.getSexo());
                usuarioDB.setFechaNacimiento(usuarioJPA.getFechaNacimiento());
                usuarioDB.setImagen(usuarioJPA.getImagen());

                if (usuarioJPA.rol != null && usuarioJPA.rol.getIdRol() > 0) {
                    usuarioDB.rol = entityManager.getReference(
                            RolJPA.class,
                            usuarioJPA.rol.getIdRol()
                    );
                }

                entityManager.merge(usuarioDB);
                result.correct = true;
                result.status = 200;

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
    public Result UpdateDireccion(DireccionJPA direccionJPA, int IdUsuario, int IdDireccion) {
        Result result = new Result();

        try {
            UsuarioJPA usuarioDB = entityManager.find(UsuarioJPA.class, IdUsuario);
            if (usuarioDB == null) {
                result.correct = false;
                result.status = 400;
                result.errorMessage = "Usuario no encontrado";
                return result;
            }

            DireccionJPA direccionDB = entityManager.find(DireccionJPA.class, IdDireccion);
            if (direccionDB == null) {
                result.correct = false;
                result.status = 400;
                result.errorMessage = "Dirección no encontrada";
                return result;
            }

            if (direccionDB.usuario.getIdUsuario() != IdUsuario) {
                result.correct = false;
                result.status = 400;
                result.errorMessage = "La dirección no pertenece al usuario especificado";
                return result;
            }

            direccionDB.setCalle(direccionJPA.getCalle());
            direccionDB.setNumeroInterior(direccionJPA.getNumeroInterior());
            direccionDB.setNumeroExterior(direccionJPA.getNumeroExterior());

            if (direccionDB.colonia != null) {
                direccionDB.colonia.setNombre(direccionJPA.colonia.getNombre());
                direccionDB.colonia.setCodigoPostal(direccionJPA.colonia.getCodigoPostal());

                if (direccionDB.colonia.municipio != null) {
                    direccionDB.colonia.municipio.setNombre(direccionJPA.colonia.municipio.getNombre());

                    if (direccionDB.colonia.municipio.estado != null) {
                        direccionDB.colonia.municipio.estado.setNombre(direccionJPA.colonia.municipio.estado.getNombre());

                        if (direccionDB.colonia.municipio.estado.pais != null) {
                            direccionDB.colonia.municipio.estado.pais.setNombre(direccionJPA.colonia.municipio.estado.pais.getNombre());
                        }
                    }
                }
            }

            entityManager.merge(direccionDB);

            result.correct = true;
            result.status = 200;
            result.Object = "Dirección actualizada correctamente";

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
    public Result DeleteUsuario(int IdUsuario) {
        Result result = new Result();

        try {
            TypedQuery<UsuarioJPA> queryUsuario = entityManager.createQuery(
                    "FROM UsuarioJPA u WHERE u.IdUsuario = :IdUsuario", UsuarioJPA.class);
            queryUsuario.setParameter("IdUsuario", IdUsuario);
            List<UsuarioJPA> usuarios = queryUsuario.getResultList();

            if (usuarios != null && !usuarios.isEmpty()) {
                UsuarioJPA usuario = usuarios.get(0);

                TypedQuery<DireccionJPA> queryDirecciones = entityManager.createQuery(
                        "FROM DireccionJPA d WHERE d.usuario.IdUsuario = :IdUsuario",
                        DireccionJPA.class);
                queryDirecciones.setParameter("IdUsuario", IdUsuario);
                List<DireccionJPA> direcciones = queryDirecciones.getResultList();

                for (DireccionJPA d : direcciones) {
                    entityManager.remove(d);
                }

                entityManager.remove(usuario);

                result.correct = true;
                result.status = 202;
                result.errorMessage = "Usuario eliminado correctamente";
            } else {
                result.correct = false;
                result.errorMessage = "Usuario no encontrado";
                result.status = 404;
            }

        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getMessage();
            result.status = 500;
        }

        return result;
    }

    @Override
    public Result searchUsuario(String nombre, String apellidoPaterno, String apellidoMaterno, Integer idRol) {
        Result result = new Result();
        try {
            StringBuilder jpql = new StringBuilder("SELECT u FROM UsuarioJPA u LEFT JOIN FETCH u.rol WHERE 1=1");

            if (nombre != null && !nombre.trim().isEmpty()) {
                jpql.append(" AND LOWER(u.Nombre) LIKE LOWER(:nombre)");
            }
            if (apellidoPaterno != null && !apellidoPaterno.trim().isEmpty()) {
                jpql.append(" AND LOWER(u.ApellidoPaterno) LIKE LOWER(:apellidoPaterno)");
            }
            if (apellidoMaterno != null && !apellidoMaterno.trim().isEmpty()) {
                jpql.append(" AND LOWER(u.ApellidoMaterno) LIKE LOWER(:apellidoMaterno)");
            }
            if (idRol != null && idRol > 0) {
                jpql.append(" AND u.rol.idRol = :idRol");
            }

            TypedQuery<UsuarioJPA> query = entityManager.createQuery(jpql.toString(), UsuarioJPA.class);

            if (nombre != null && !nombre.trim().isEmpty()) {
                query.setParameter("nombre", "%" + nombre.trim() + "%");
            }
            if (apellidoPaterno != null && !apellidoPaterno.trim().isEmpty()) {
                query.setParameter("apellidoPaterno", "%" + apellidoPaterno.trim() + "%");
            }
            if (apellidoMaterno != null && !apellidoMaterno.trim().isEmpty()) {
                query.setParameter("apellidoMaterno", "%" + apellidoMaterno.trim() + "%");
            }
            if (idRol != null && idRol > 0) {
                query.setParameter("idRol", idRol);
            }

            List<UsuarioJPA> usuarios = query.getResultList();

            result.Object = usuarios;
            result.correct = true;
            result.status = 200;

        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getMessage();
            result.status = 500;
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
