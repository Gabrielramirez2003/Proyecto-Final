package Personas;

import ENUMS.Eroles;
import Excepciones.ContraseniaIncorrectaException;
import org.json.JSONObject;

public class Empleado extends Persona{
    private String idEmpleado = "E" + super.getId();
    private String contrasenia;
    private Eroles rol ;

    //constructors


    public Empleado() {
    }

    public Empleado(String nombre, String email, String telefono, String contrasenia, Eroles rol) {
        super(nombre, email, telefono);
        this.contrasenia = contrasenia;
        this.rol = rol;
    }

    //getters & setters

    public String getIdEmpleado() {
        return idEmpleado;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setRol(Eroles rol) {
        this.rol = rol;
    }

    public Eroles getRol() {
        return rol;
    }

    //methods


    @Override
    public JSONObject personaToJSONObject() {
        JSONObject o = new JSONObject();
        o.put("nombre",this.getNombre());
        o.put("email",this.getEmail());
        o.put("telefono",this.getTelefono());
        o.put("contrasenia", this.getContrasenia());
        o.put("rol", this.getRol());
        return o;
    }

    public boolean register(String nombre, String email, String telefono, String contrasenia){

    }
    public boolean loggin(String contrasenia)throws ContraseniaIncorrectaException {

    }
}
