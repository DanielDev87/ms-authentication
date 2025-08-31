package co.com.bancolombia.api.constants;

public class ApiConstants {
    // --- General ---
    public static final String USER_TAG = "Usuarios";
    public static final String RESPONSE_200 = "Operación exitosa";
    public static final String RESPONSE_201 = "Recurso creado exitosamente";
    public static final String RESPONSE_400 = "Datos de entrada inválidos";
    public static final String RESPONSE_404 = "Recurso no encontrado";

    // --- Crear Usuario ---
    public static final String CREATE_USER_SUMMARY = "Crear un nuevo usuario";
    public static final String CREATE_USER_DESC = "Crea un nuevo registro de usuario en el sistema.";
    public static final String CREATE_USER_REQ_BODY_DESC = "Datos del nuevo usuario a crear.";

    // --- Buscar Usuario por Documento ---
    public static final String GET_USER_BY_DOC_SUMMARY = "Buscar usuario por documento";
    public static final String GET_USER_BY_DOC_DESC = "Busca y retorna un usuario basado en su número de documento.";
    public static final String GET_USER_BY_DOC_PARAM_DESC = "Número de documento del usuario a buscar.";

    private ApiConstants() {
        // Constructor privado para evitar instanciación
    }
}
