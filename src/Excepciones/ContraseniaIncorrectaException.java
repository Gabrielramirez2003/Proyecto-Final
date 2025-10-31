package Excepciones;

public class ContraseniaIncorrectaException extends Exception {
    public ContraseniaIncorrectaException(String message) {
        super("Esa contrasenia no existe");
    }
}
