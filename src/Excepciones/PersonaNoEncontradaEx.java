package Excepciones;

public class PersonaNoEncontradaEx extends RuntimeException {
    public PersonaNoEncontradaEx(String message) {
        super(message);
    }
}
