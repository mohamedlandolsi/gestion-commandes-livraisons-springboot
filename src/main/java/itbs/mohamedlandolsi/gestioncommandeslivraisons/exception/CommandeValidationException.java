package itbs.mohamedlandolsi.gestioncommandeslivraisons.exception;

public class CommandeValidationException extends RuntimeException {
    
    public CommandeValidationException(String message) {
        super(message);
    }
    
    public CommandeValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
