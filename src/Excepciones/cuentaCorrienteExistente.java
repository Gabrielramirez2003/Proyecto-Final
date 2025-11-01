package Excepciones;

public class cuentaCorrienteExistente extends RuntimeException {
    public cuentaCorrienteExistente(String message) {
        super(message);
    }
}
