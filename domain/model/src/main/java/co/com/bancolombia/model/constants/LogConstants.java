package co.com.bancolombia.model.constants;


public final class LogConstants {

    private LogConstants() {
    }

    // --- Mensajes Positivos ---
    public static final String USER_CREATED_SUCCESSFULLY = "Usuario {} guardado exitosamente.";
    public static final String USER_CREATED_SUCCESSFULLY_WITH_ID = "Usuario creado exitosamente con ID: {}";

    // --- Mensajes de Inicio de Proceso ---
    public static final String CREATE_USER_USE_CASE_STARTED = "Iniciando creación para usuario con email: {}";
    public static final String ENCRYPTING_PASSWORD = "Email y documento disponibles. Encriptando contraseña para el usuario: {}";

    // --- Mensajes de Advertencia (WARN) ---
    public static final String EMAIL_ALREADY_EXISTS_WARN = "El email {} ya está registrado.";
    public static final String DOCUMENT_ALREADY_EXISTS_WARN = "El número de documento {} ya está registrado.";
    public static final String CONFLICT_CREATING_USER = "Conflicto al crear usuario con email {}: {}";
    public static final String UNEXPECTED_ERROR_CREATING_USER_DETAIL = "Error inesperado al crear usuario: {} - {}";

    // --- Mensajes de Error (ERROR) ---
    public static final String UNEXPECTED_ERROR_CREATING_USER = "Error inesperado al crear usuario: {}";
    public static final String USER_SEARCH_BY_DOCUMENT_STARTED = "Buscando usuario con documento: {}";
    public static final String USER_CREATION_REQUEST_RECEIVED = "Recibida petición para crear usuario con email: {}";

    // --- Mensajes LOGIN ---
    public static final String LOGIN_REQUEST_RECEIVED = "Solicitud de login recibida para el usuario: {}";
    public static final String LOGIN_SUCCESSFUL = "Login exitoso para el usuario: {}";
    public static final String LOGIN_ATTEMPT_FAILED = "Intento de login fallido para {}: {}";
    public static final String JSON_ERROR_KEY = "error";


}