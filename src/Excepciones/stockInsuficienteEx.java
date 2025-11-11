package Excepciones;

public class stockInsuficienteEx extends RuntimeException {
    public stockInsuficienteEx(String message) {
        super(message);
    }
}
