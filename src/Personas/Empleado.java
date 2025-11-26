package Personas;

import Archivos_Json.JSONUtiles;
import Archivos_Json.validacionArchivoCuentasCorrientes;
import Archivos_Json.validacionesArchivoUsuarios;
import ENUMS.Eroles;
import Excepciones.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Empleado extends Persona {
    private String idEmpleado;
    private String contrasenia;
    private Eroles rol = Eroles.EMPLEADO;

    //constructors


    public Empleado() {
        idEmpleado = "E" + super.getId();
    }

    public Empleado(String nombre, String email, String telefono, String contrasenia) {
        super(nombre, email, telefono);
        idEmpleado = "E" + super.getId();
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
        o.put("nombre", this.getNombre());
        o.put("id_Empleado", this.getIdEmpleado());
        o.put("email", this.getEmail());
        o.put("telefono", this.getTelefono());
        o.put("contrasenia", this.getContrasenia());
        o.put("rol", this.getRol());
        return o;
    }

    public boolean register(String nombre, String email, String telefono, String contrasenia) throws IOException, emailInvalidoEx, contraseniaNoValidaEx {

        File file = new File("usuarios.json");
        Empleado e = new Empleado(nombre, email, telefono, contrasenia);
        JSONArray a = new JSONArray();
        JSONArray d;

        validacionesArchivoUsuarios.validarEmail(e.getEmail());
        validacionesArchivoUsuarios.validarContrasenia(e.getContrasenia());

        if (!file.exists()) {
            JSONUtiles.inicializarArchivo("usuarios");
            a.put(e.personaToJSONObject());
            JSONUtiles.uploadJSON(a, "usuarios");
            System.out.println("El usuario fue registrado con exito");
            return true;
        } else {
            d = new JSONArray(JSONUtiles.downloadJSON("usuarios"));
            validacionesArchivoUsuarios.corroborarEmail(e.getEmail(), d);
            d.put(e.personaToJSONObject());
            JSONUtiles.uploadJSON(d, "usuarios");
            System.out.println("El usuario fue registrado con exito");
            return true;
        }
    }


    public boolean loggin(String email, String contrasenia) throws IOException, ContraseniaIncorrectaException, emailIncorrectoEx {
        File file = new File("usuarios.json");
        JSONArray a;

        if (file.exists()) {
            a = new JSONArray(JSONUtiles.downloadJSON("usuarios"));
            return validacionesArchivoUsuarios.validarIngreso(email, contrasenia, a);
        }

        return false;
    }

    public boolean registrarCliente(String nombre, String email, String telefono, String direccion, String cuit) throws IOException, cuentaCorrienteExistente {

        File file = new File("cuentasCorrientes.json");
        JSONArray a = new JSONArray();
        JSONArray b;
        Cliente c = new Cliente(nombre, email, telefono, direccion, cuit);

        if (validacionArchivoCuentasCorrientes.cuentaExistente(cuit)) {
            throw new cuentaCorrienteExistente("No se pudo registrar el usuario porque ya existe alguien con ese cuit");
        }

        if (!file.exists()) {
            JSONUtiles.inicializarArchivo("cuentasCorrientes");
            a.put(c.personaToJSONObject());
            JSONUtiles.uploadJSON(a, "cuentasCorrientes");
            System.out.println("Cliente registrado con exito");
            return true;
        } else {
            b = new JSONArray(JSONUtiles.downloadJSON("cuentasCorrientes"));
            b.put(c.personaToJSONObject());
            JSONUtiles.uploadJSON(b, "cuentasCorrientes");
            System.out.println("Cliente registrado con exito");
            return true;
        }
    }
}
