package co.com.bancolombia.model.constants;

public final class BusinessErrorMessageConstants {

    private BusinessErrorMessageConstants() {
    }

    // --- Errores de Negocio para CreateUserUseCase ---
    public static final String EMAIL_ALREADY_IN_USE = "El correo electrónico ya está en uso.";
    public static final String DOCUMENT_NUMBER_ALREADY_IN_USE = "El número de documento ya está en uso.";
}
