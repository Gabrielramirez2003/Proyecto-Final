package Personas;

import Archivos_Json.JSONUtiles;
import Archivos_Json.validacionesArchivoUsuarios;
import ENUMS.Eroles;
import Excepciones.ContraseniaIncorrectaException;
import Excepciones.contraseniaNoValidaEx;
import Excepciones.emailInvalidoEx;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Empleado extends Persona{
    private String idEmpleado = "E" + super.getId();
    private String contrasenia;
    private Eroles rol = Eroles.EMPLEADO;

    //constructors


    public Empleado() {
    }

    public Empleado(String nombre, String email, String telefono, String contrasenia) {
        super(nombre, email, telefono);
        this.contrasenia = contrasenia;
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


        try{
            File file = new File("usuarios.json");
            Empleado e = new Empleado(nombre, email, telefono, contrasenia);
            JSONArray a = new JSONArray();
            JSONArray d;
            if(!file.exists()) {

                JSONUtiles.inicializarArchivoUsuarios();
                validacionesArchivoUsuarios.validarEmail(e.getEmail());
                validacionesArchivoUsuarios.validarContrasenia(e.getContrasenia());
                a.put(e.personaToJSONObject());
                JSONUtiles.uploadJSON(a,"usuarios");
                System.out.println("El usuario fue registrado con exito");
                return true;
            }else{
                d = new JSONArray(JSONUtiles.downloadJSON("usuarios"));
                validacionesArchivoUsuarios.validarEmail(e.getEmail());
                validacionesArchivoUsuarios.validarContrasenia(e.getContrasenia());
                validacionesArchivoUsuarios.corroborarEmail(e.getEmail(), d);
                d.put(e.personaToJSONObject());
                JSONUtiles.uploadJSON(d,"usuarios");
                System.out.println("El usuario fue registrado con exito");
                return true;
            }

        }catch (Exception ex){
            ex.printStackTrace();
            return false;
        }

    }




    public boolean loggin(String contrasenia)throws ContraseniaIncorrectaException {
        return false;
    }
}
