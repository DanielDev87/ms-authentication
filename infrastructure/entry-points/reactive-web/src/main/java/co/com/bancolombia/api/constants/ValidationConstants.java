package co.com.bancolombia.api.constants;

public class ValidationConstants {
    // --- Mensajes Generales ---
    private static final String NOT_BLANK = " no puede estar vacío";
    private static final String NOT_NULL = " no puede ser nulo";
    private static final String NOT_NEGATIVE = " no puede ser negativo";

    // --- Mensajes para UserDTO ---
    public static final String DOCUMENT_NUMBER_NOT_BLANK = "El número de documento" + NOT_BLANK;
    public static final String FIRST_NAME_NOT_BLANK = "El nombre" + NOT_BLANK;
    public static final String LAST_NAME_NOT_BLANK = "El apellido" + NOT_BLANK;
    public static final String PASSWORD_NOT_BLANK = "La contraseña" + NOT_BLANK;
    public static final String EMAIL_NOT_BLANK = "El email" + NOT_BLANK;
    public static final String EMAIL_FORMAT_INVALID = "El formato del email es inválido";
    public static final String SALARY_NOT_NULL = "El salario base" + NOT_NULL;
    public static final String SALARY_NOT_NEGATIVE = "El salario base" + NOT_NEGATIVE;
    public static final String SALARY_MAX_EXCEEDED = "El salario base no puede exceder 15,000,000";

    private ValidationConstants() {
    }
}
