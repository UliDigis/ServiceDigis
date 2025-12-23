package com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.RestController;

import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.DAO.UsuarioJPADAOImplementation;
import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.JPA.*;
import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.Service.ValidationService;

import jakarta.servlet.http.HttpSession;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("api/cargaMasiva")
public class CargaMasivaRestController {

    private final UsuarioJPADAOImplementation usuarioDAO;
    private final ValidationService validationService;
    private final PasswordEncoder passwordEncoder;

    private static final String DEFAULT_IMAGE_MARKER = null; 

    public CargaMasivaRestController(
            UsuarioJPADAOImplementation usuarioDAO,
            ValidationService validationService,
            PasswordEncoder passwordEncoder
    ) {
        this.usuarioDAO = usuarioDAO;
        this.validationService = validationService;
        this.passwordEncoder = passwordEncoder;
    }

    public static class ErrorCarga {
        public int linea;
        public String campo;
        public String descripcion;

        public ErrorCarga() { }

        public ErrorCarga(int linea, String campo, String descripcion) {
            this.linea = linea;
            this.campo = campo;
            this.descripcion = descripcion;
        }
    }

    @PostMapping(value = "/validar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Result> validar(@RequestParam("archivo") MultipartFile archivo,
                                          HttpSession session) {

        Result result = new Result();

        String nombreArchivo = archivo.getOriginalFilename();
        if (nombreArchivo == null || !nombreArchivo.contains(".")) {
            result.correct = false;
            result.errorMessage = "Extensión del archivo inválida";
            result.status = 400;
            return ResponseEntity.status(result.status).body(result);
        }

        String extension = nombreArchivo.substring(nombreArchivo.lastIndexOf('.') + 1)
                .toLowerCase(Locale.ROOT);

        if (!extension.equals("txt") && !extension.equals("xlsx")) {
            result.correct = false;
            result.errorMessage = "Extensión no compatible (solo .txt o .xlsx)";
            result.status = 400;
            return ResponseEntity.status(result.status).body(result);
        }

        String carpetaDestino = System.getProperty("java.io.tmpdir") + File.separator + "archivosCarga";
        try { Files.createDirectories(Path.of(carpetaDestino)); } catch (Exception ignore) {}

        String marcaTiempo = String.valueOf(System.currentTimeMillis());
        String pathDefinitivo = carpetaDestino + File.separator + marcaTiempo + "_" + nombreArchivo;

        try {
            archivo.transferTo(new File(pathDefinitivo));
        } catch (Exception exception) {
            result.correct = false;
            result.errorMessage = "Error guardando el archivo: " + exception.getMessage();
            result.status = 500;
            return ResponseEntity.status(result.status).body(result);
        }

        try {
            List<UsuarioJPA> listaUsuarios = extension.equals("txt")
                    ? lecturaArchivoTXT(new File(pathDefinitivo))
                    : lecturaArchivoXLSX(new File(pathDefinitivo));

            if (listaUsuarios == null) {
                safeDelete(pathDefinitivo);
                result.correct = false;
                result.errorMessage = "Error al leer el archivo";
                result.status = 400;
                return ResponseEntity.status(result.status).body(result);
            }

            List<ErrorCarga> listaErrores = validarDatosArchivo(listaUsuarios);

            if (!listaErrores.isEmpty()) {
                safeDelete(pathDefinitivo);

                Map<String, Object> payload = new HashMap<>();
                payload.put("sinErrores", false);
                payload.put("totalRegistros", listaUsuarios.size());
                payload.put("errores", listaErrores);

                result.correct = false;
                result.status = 422;
                result.errorMessage = "El archivo tiene errores de validación";
                result.Object = payload;
                return ResponseEntity.status(result.status).body(result);
            }

            session.setAttribute("archivoCargaMasiva", pathDefinitivo);

            Map<String, Object> payload = new HashMap<>();
            payload.put("sinErrores", true);
            payload.put("totalRegistros", listaUsuarios.size());
            payload.put("siguientePaso", "GET /api/cargaMasiva/procesar");

            result.correct = true;
            result.status = 200;
            result.Object = payload;
            return ResponseEntity.status(result.status).body(result);

        } catch (Exception exception) {
            safeDelete(pathDefinitivo);
            result.correct = false;
            result.errorMessage = "Error al validar: " + exception.getMessage();
            result.status = 500;
            return ResponseEntity.status(result.status).body(result);
        }
    }

    @GetMapping("/procesar")
    public ResponseEntity<Result> procesar(HttpSession session) {

        Result result = new Result();

        Object pathObj = session.getAttribute("archivoCargaMasiva");
        if (pathObj == null) {
            result.correct = false;
            result.errorMessage = "No se encontró archivo en sesión. Primero llama POST /api/cargaMasiva/validar";
            result.status = 400;
            return ResponseEntity.status(result.status).body(result);
        }

        String path = pathObj.toString();

        try {
            List<UsuarioJPA> listaUsuarios;

            if (path.toLowerCase(Locale.ROOT).endsWith(".txt")) {
                listaUsuarios = lecturaArchivoTXT(new File(path));
            } else if (path.toLowerCase(Locale.ROOT).endsWith(".xlsx")) {
                listaUsuarios = lecturaArchivoXLSX(new File(path));
            } else {
                result.correct = false;
                result.errorMessage = "Formato no soportado en el archivo guardado";
                result.status = 400;
                return ResponseEntity.status(result.status).body(result);
            }

            if (listaUsuarios == null) {
                result.correct = false;
                result.errorMessage = "No se pudo leer el archivo para procesar";
                result.status = 400;
                return ResponseEntity.status(result.status).body(result);
            }

            for (UsuarioJPA usuarioJPA : listaUsuarios) {

                if (usuarioJPA.getPassword() != null && !usuarioJPA.getPassword().isBlank()) {
                    usuarioJPA.setPassword(passwordEncoder.encode(usuarioJPA.getPassword()));
                }

                String imagenEntrada = usuarioJPA.getImagen();
                if (imagenEntrada == null || imagenEntrada.isBlank()) {
                    usuarioJPA.setImagen(DEFAULT_IMAGE_MARKER);
                } else {
                    usuarioJPA.setImagen(limpiarBase64Imagen(imagenEntrada));
                }
            }

            Result daoResult = usuarioDAO.CargaMasiva(listaUsuarios);

            if (daoResult.correct) {
                Map<String, Object> payload = new HashMap<>();
                payload.put("mensaje", "Carga masiva realizada correctamente");
                payload.put("totalInsertados", listaUsuarios.size());
                payload.put("detalle", daoResult.Object);

                result.correct = true;
                result.status = 200;
                result.Object = payload;
                return ResponseEntity.status(result.status).body(result);
            }

            result.correct = false;
            result.status = (daoResult.status != 0 ? daoResult.status : 500);
            result.errorMessage = "Error al procesar usuarios: " + daoResult.errorMessage;
            result.ex = daoResult.ex;
            return ResponseEntity.status(result.status).body(result);

        } catch (Exception exception) {
            result.correct = false;
            result.errorMessage = "Error inesperado al procesar: " + exception.getMessage();
            result.status = 500;
            return ResponseEntity.status(result.status).body(result);

        } finally {
            session.removeAttribute("archivoCargaMasiva");
            safeDelete(path);
        }
    }

    private List<ErrorCarga> validarDatosArchivo(List<UsuarioJPA> listaUsuarios) {
        List<ErrorCarga> listaErrores = new ArrayList<>();
        int numeroLinea = 0;

        for (UsuarioJPA usuarioJPA : listaUsuarios) {
            numeroLinea++;

            BindingResult bindingResult = validationService.validateObject(usuarioJPA);
            for (ObjectError objectError : bindingResult.getAllErrors()) {
                if (objectError instanceof FieldError fieldError) {
                    listaErrores.add(new ErrorCarga(numeroLinea, fieldError.getField(), fieldError.getDefaultMessage()));
                } else {
                    listaErrores.add(new ErrorCarga(numeroLinea, "general", objectError.getDefaultMessage()));
                }
            }
        }

        return listaErrores;
    }

    private List<UsuarioJPA> lecturaArchivoTXT(File archivoTXT) {
        List<UsuarioJPA> listaUsuarios = new ArrayList<>();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(archivoTXT))) {

            String lineaTexto;
            while ((lineaTexto = bufferedReader.readLine()) != null) {

                if (lineaTexto.isBlank()) continue;

                String[] columnas = lineaTexto.split("\\|", -1);
                if (columnas.length < 21) return null;

                UsuarioJPA usuarioJPA = new UsuarioJPA();
                usuarioJPA.setUserName(columnas[0]);
                usuarioJPA.setNombre(columnas[1]);
                usuarioJPA.setApellidoPaterno(columnas[2]);
                usuarioJPA.setApellidoMaterno(columnas[3]);
                usuarioJPA.setEmail(columnas[4]);
                usuarioJPA.setPassword(columnas[5]);
                usuarioJPA.setFechaNacimiento(parseLocalDateFlexible(columnas[6]));

                String sexoTexto = columnas[7];
                usuarioJPA.setSexo(sexoTexto == null || sexoTexto.isBlank() ? null : sexoTexto.trim().charAt(0));

                usuarioJPA.setTelefono(columnas[8]);
                usuarioJPA.setCelular(columnas[9]);
                usuarioJPA.setCURP(columnas[10]);
                usuarioJPA.setImagen(columnas[11]);

                RolJPA rolJPA = new RolJPA();
                rolJPA.setIdRol(parseEnteroSeguro(columnas[12]));
                usuarioJPA.rol = rolJPA;

                DireccionJPA direccionJPA = construirDireccion(
                        columnas[13], columnas[14], columnas[15],
                        columnas[16], columnas[17], columnas[18],
                        columnas[19], columnas[20]
                );

                usuarioJPA.setDirecciones(new ArrayList<>());
                usuarioJPA.getDirecciones().add(direccionJPA);

                listaUsuarios.add(usuarioJPA);
            }

        } catch (Exception exception) {
            return null;
        }

        return listaUsuarios;
    }

    private List<UsuarioJPA> lecturaArchivoXLSX(File archivoXLSX) {

        List<UsuarioJPA> listaUsuarios = new ArrayList<>();

        try (InputStream inputStream = new FileInputStream(archivoXLSX);
             XSSFWorkbook workbook = new XSSFWorkbook(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);
            DataFormatter dataFormatter = new DataFormatter();

            for (Row row : sheet) {

                if (rowEsVacia(row, dataFormatter)) continue;

                UsuarioJPA usuarioJPA = new UsuarioJPA();

                usuarioJPA.setUserName(getStringCell(row, 0, dataFormatter));
                usuarioJPA.setNombre(getStringCell(row, 1, dataFormatter));
                usuarioJPA.setApellidoPaterno(getStringCell(row, 2, dataFormatter));
                usuarioJPA.setApellidoMaterno(getStringCell(row, 3, dataFormatter));
                usuarioJPA.setEmail(getStringCell(row, 4, dataFormatter));
                usuarioJPA.setPassword(getStringCell(row, 5, dataFormatter));
                usuarioJPA.setFechaNacimiento(getLocalDateCell(row, 6, dataFormatter));

                String sexoTexto = getStringCell(row, 7, dataFormatter);
                usuarioJPA.setSexo(sexoTexto == null || sexoTexto.isBlank() ? null : sexoTexto.trim().charAt(0));

                usuarioJPA.setTelefono(getStringCell(row, 8, dataFormatter));
                usuarioJPA.setCelular(getStringCell(row, 9, dataFormatter));
                usuarioJPA.setCURP(getStringCell(row, 10, dataFormatter));
                usuarioJPA.setImagen(getStringCell(row, 11, dataFormatter));

                int idRol = (int) getNumericCell(row, 12, dataFormatter);
                RolJPA rolJPA = new RolJPA();
                rolJPA.setIdRol(idRol);
                usuarioJPA.rol = rolJPA;

                DireccionJPA direccionJPA = construirDireccion(
                        getStringCell(row, 13, dataFormatter),
                        getStringCell(row, 14, dataFormatter),
                        getStringCell(row, 15, dataFormatter),
                        getStringCell(row, 16, dataFormatter),
                        getStringCell(row, 17, dataFormatter),
                        getStringCell(row, 18, dataFormatter),
                        getStringCell(row, 19, dataFormatter),
                        getStringCell(row, 20, dataFormatter)
                );

                usuarioJPA.setDirecciones(new ArrayList<>());
                usuarioJPA.getDirecciones().add(direccionJPA);

                listaUsuarios.add(usuarioJPA);
            }

        } catch (Exception exception) {
            return null;
        }

        return listaUsuarios;
    }

    private DireccionJPA construirDireccion(
            String calle,
            String numeroExterior,
            String numeroInterior,
            String paisNombre,
            String estadoNombre,
            String municipioNombre,
            String coloniaNombre,
            String codigoPostal
    ) {
        DireccionJPA direccionJPA = new DireccionJPA();
        direccionJPA.setCalle(calle);
        direccionJPA.setNumeroExterior(numeroExterior);
        direccionJPA.setNumeroInterior(numeroInterior);

        PaisJPA paisJPA = new PaisJPA();
        paisJPA.setNombre(paisNombre);

        EstadoJPA estadoJPA = new EstadoJPA();
        estadoJPA.setNombre(estadoNombre);
        estadoJPA.setPais(paisJPA);

        MunicipioJPA municipioJPA = new MunicipioJPA();
        municipioJPA.setNombre(municipioNombre);
        municipioJPA.setEstado(estadoJPA);

        ColoniaJPA coloniaJPA = new ColoniaJPA();
        coloniaJPA.setNombre(coloniaNombre);
        coloniaJPA.setCodigoPostal(codigoPostal);
        coloniaJPA.setMunicipio(municipioJPA);

        direccionJPA.setColonia(coloniaJPA);
        return direccionJPA;
    }

    private boolean rowEsVacia(Row row, DataFormatter dataFormatter) {
        if (row == null) return true;
        for (int indice = 0; indice <= 20; indice++) {
            String valor = getStringCell(row, indice, dataFormatter);
            if (valor != null && !valor.isBlank()) return false;
        }
        return true;
    }

    private String getStringCell(Row row, int indice, DataFormatter dataFormatter) {
        Cell cell = row.getCell(indice, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
        if (cell == null) return "";
        return dataFormatter.formatCellValue(cell).trim();
    }

    private double getNumericCell(Row row, int indice, DataFormatter dataFormatter) {
        Cell cell = row.getCell(indice, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
        if (cell == null) return 0;

        if (cell.getCellType() == CellType.NUMERIC) {
            return cell.getNumericCellValue();
        }

        String texto = dataFormatter.formatCellValue(cell).trim();
        if (texto.isBlank()) return 0;

        try {
            return Double.parseDouble(texto);
        } catch (Exception exception) {
            return 0;
        }
    }

    private LocalDate getLocalDateCell(Row row, int indice, DataFormatter dataFormatter) {
        Cell cell = row.getCell(indice, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
        if (cell == null) return null;

        if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
            Date fecha = cell.getDateCellValue();
            return fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }

        String texto = dataFormatter.formatCellValue(cell).trim();
        return parseLocalDateFlexible(texto);
    }

    private LocalDate parseLocalDateFlexible(String texto) {
        if (texto == null) return null;

        String fecha = texto.trim();
        if (fecha.isBlank()) return null;

        List<DateTimeFormatter> formatos = List.of(
                DateTimeFormatter.ofPattern("yyyy-MM-dd"),
                DateTimeFormatter.ofPattern("dd/MM/yyyy"),
                DateTimeFormatter.ofPattern("d/M/yyyy")
        );

        for (DateTimeFormatter formatter : formatos) {
            try {
                return LocalDate.parse(fecha, formatter);
            } catch (Exception ignore) { }
        }

        return null;
    }

    private int parseEnteroSeguro(String texto) {
        try {
            return Integer.parseInt(texto.trim());
        } catch (Exception exception) {
            return 0;
        }
    }

    private String limpiarBase64Imagen(String imagenEntrada) {
        String texto = imagenEntrada.trim();
        if (texto.isBlank()) return DEFAULT_IMAGE_MARKER;

        if (texto.startsWith("data:image/")) {
            int indiceComa = texto.indexOf(',');
            if (indiceComa >= 0 && indiceComa + 1 < texto.length()) {
                String base64 = texto.substring(indiceComa + 1).trim();
                return base64.isBlank() ? DEFAULT_IMAGE_MARKER : base64;
            }
            return DEFAULT_IMAGE_MARKER;
        }

        return texto;
    }

    private void safeDelete(String path) {
        if (path == null) return;
        try { Files.deleteIfExists(Path.of(path)); } catch (Exception ignore) {}
    }
}
