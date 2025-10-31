package Excepciones;

public class ContraseniaIncorrectaException extends RuntimeException {
    public ContraseniaIncorrectaException(String message) {
        super("Esa contrasenia no existe");
    }
}
