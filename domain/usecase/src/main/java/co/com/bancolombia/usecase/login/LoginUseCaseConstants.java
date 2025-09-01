package co.com.bancolombia.usecase.login;

public final class LoginUseCaseConstants {

    // --- Mensajes de Error ---
    public static final String ERROR_INVALID_CREDENTIALS = "Credenciales inválidas";
    public static final String ERROR_ACCOUNT_LOCKED = "Cuenta bloqueada. Intente más tarde.";

    // --- Configuración de Bloqueo ---
    public static final int MAX_FAILED_ATTEMPTS = 3;
    public static final int LOCK_DURATION_MINUTES = 15;

    private LoginUseCaseConstants() {
    }
}
