package Excepciones;

public class cuentaCorrienteInexistenteEx extends RuntimeException {
    public cuentaCorrienteInexistenteEx(String message) {
        super(message);
    }
}
