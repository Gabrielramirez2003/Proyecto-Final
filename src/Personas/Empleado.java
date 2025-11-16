package Personas;

import Archivos_Json.JSONUtiles;
import Archivos_Json.validacionArchivoCuentasCorrientes;
import Archivos_Json.validacionesArchivoUsuarios;
import ENUMS.Eroles;
import Excepciones.ContraseniaIncorrectaException;
import Excepciones.contraseniaNoValidaEx;
import Excepciones.cuentaCorrienteExistente;
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

                JSONUtiles.inicializarArchivo("usuarios");
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
            System.out.println(ex.getMessage());
            return false;
        }

    }




    public boolean loggin(String email,String contrasenia){
        try{
            File file = new File("usuarios.json");
            JSONArray a;

            if(file.exists()){
                a=new JSONArray(JSONUtiles.downloadJSON("usuarios"));
                return validacionesArchivoUsuarios.validarIngreso(email,contrasenia,a);
            }

        }catch(Exception e){
            System.out.println(e.getMessage());

        }
        return false;
    }

    public boolean registrarCliente(String nombre, String email, String telefono, String direccion, String cuit) {
        try {
            File file = new File("cuentasCorrientes.json");
            JSONArray a = new JSONArray();
            JSONArray b;
            Cliente c = new Cliente(nombre, email, telefono, direccion, cuit);
            if (!file.exists()) {
                JSONUtiles.inicializarArchivo("cuentasCorrientes");
                a.put(c.personaToJSONObject());
                JSONUtiles.uploadJSON(a, "cuentasCorrientes");
                System.out.println("Cliente registrado con exito");
                return true;
            } else {
                b = new JSONArray(JSONUtiles.downloadJSON("cuentasCorrientes"));
                b.put(c.personaToJSONObject());
                if (validacionArchivoCuentasCorrientes.cuentaExistente(cuit)) {
                    throw new cuentaCorrienteExistente("No se pudo registrar el usuario porque ya existe alguien con ese cuit");
                } else {
                    JSONUtiles.uploadJSON(b, "cuentasCorrientes");
                    System.out.println("Cliente registrado con exito");
                    return true;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
