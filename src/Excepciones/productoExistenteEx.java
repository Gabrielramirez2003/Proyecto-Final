package Excepciones;

public class productoExistenteEx extends RuntimeException {
    public productoExistenteEx(String message) {
        super(message);
    }
}
