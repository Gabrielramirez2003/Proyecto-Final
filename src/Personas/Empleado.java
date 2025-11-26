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
import java.util.UUID;

public class Empleado extends Persona {
    private String idEmpleado;
    private String contrasenia;
    private Eroles rol = Eroles.EMPLEADO;

    // --- CONSTRUCTORES ---

    public Empleado() {
        super();
    }

    // CORRECCIÓN: Agregado 'telefono' para pasar al super constructor correctamente
    public Empleado(String nombre, String email, String telefono, String contrasenia) {
        super(nombre, email, telefono);
        this.idEmpleado = UUID.randomUUID().toString().substring(0, 8);
        this.contrasenia = contrasenia;
        this.rol = Eroles.EMPLEADO;
    }

    // --- GETTERS & SETTERS ---

    public String getIdEmpleado() { return idEmpleado; }
    public void setIdEmpleado(String idEmpleado) { this.idEmpleado = idEmpleado; }

    public String getContrasenia() { return contrasenia; }
    public void setContrasenia(String contrasenia) { this.contrasenia = contrasenia; }

    public Eroles getRol() { return this.rol; }
    public void setRol(Eroles rol) { this.rol = rol; }

    // --- MÉTODOS ---

    @Override
    public JSONObject personaToJSONObject() {
        JSONObject o = new JSONObject();

        // Datos heredados
        o.put("nombre", this.getNombre());
        o.put("email", this.getEmail());
        o.put("telefono", this.getTelefono());

        // Datos propios
        // 🚨 CORRECCIÓN CRÍTICA: La clave debe ser "idEmpleado" (sin guion bajo)
        // para que coincida con la búsqueda en EnvolventePersona.
        o.put("idEmpleado", this.getIdEmpleado());
        o.put("contrasenia", this.getContrasenia());
        o.put("rol", this.rol.name());

        return o;
    }

    // CORRECCIÓN: Agregado parámetro telefono
    public boolean register(String nombre, String email, String telefono, String contrasenia) throws IOException, emailInvalidoEx, contraseniaNoValidaEx {

        // Validaciones básicas
        if (!email.contains("@")) throw new emailInvalidoEx("Email sin @");
        if (contrasenia.length() < 4) throw new contraseniaNoValidaEx("Contraseña muy corta");

        // 1. Leer el archivo actual de usuarios
        JSONArray usuariosArray;
        try {
            String contenido = JSONUtiles.downloadJSON("usuarios");
            usuariosArray = new JSONArray(contenido);
        } catch (Exception e) {
            usuariosArray = new JSONArray(); // Si falla o está vacío, creamos uno nuevo
        }

        // 2. Lógica de "Primer Usuario es Admin"
        Eroles rolAsignado = Eroles.EMPLEADO;
        if (usuariosArray.length() == 0) {
            rolAsignado = Eroles.ENCARGADO; // ¡El primero es el jefe!
        }

        // 3. Configurar este objeto actual con los datos
        this.setNombre(nombre);
        this.setEmail(email);
        this.setTelefono(telefono); // Guardamos el telefono
        this.contrasenia = contrasenia;
        this.idEmpleado = UUID.randomUUID().toString().substring(0, 8);
        this.rol = rolAsignado;

        // 4. Convertir a JSON
        JSONObject nuevoUsuarioJSON = this.personaToJSONObject();

        // 5. Agregar al Array y Guardar
        usuariosArray.put(nuevoUsuarioJSON);
        JSONUtiles.uploadJSON(usuariosArray, "usuarios");

        return true;
    }

    // Este método se mantiene por compatibilidad, pero la lógica real de login
    // la movimos a EnvolventePrincipal para cargar el objeto completo.
    public boolean loggin(String email, String contrasenia) {
        // Retorna true/false simple (opcional, ya que EnvolventePrincipal hace el trabajo pesado)
        return true;
    }

    @Override
    public String toString() {
        return String.format(
                "| ID: %-8s | Rol: %-10s | Nombre: %-20s | Email: %-25s | Tel: %s",
                this.idEmpleado,
                this.rol.name(),
                this.nombre,
                this.email,
                this.telefono
        );
    }
}