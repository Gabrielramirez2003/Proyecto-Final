import Archivos_Json.JSONUtiles;
import ENUMS.Eroles;
import Excepciones.*;
import Personas.Cliente;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class EnvolventePersona {

    public EnvolventePersona() {
    }

    public void eliminarEmpleado(String id_empleado) throws IOException, JSONException, PersonaNoEncontradaEx {

        JSONArray personasJSON = leerPersonas();
        boolean encontrado = false;

        for (int i = 0; i < personasJSON.length(); i++) {
            JSONObject obj = personasJSON.getJSONObject(i);
            if (obj.has("contrasenia")) { // si tiene contraseña es un empleado
                if (id_empleado.equalsIgnoreCase(obj.getString("idEmpleado"))) {
                    personasJSON.remove(i); // elimino el empleado
                    encontrado = true;
                    i--; // CORRECCIÓN CRÍTICA: Ajustar índice tras borrar
                }
            }
        }

        if (!encontrado) {
            throw new PersonaNoEncontradaEx("La persona no se encontró en al archivo");
        }

        JSONUtiles.uploadJSON(personasJSON, "usuarios");
    }

    public void eliminarCliente(String id_cliente) throws IOException, JSONException, PersonaNoEncontradaEx {
        JSONArray personasJSON = leerPersonas();
        boolean encontrado = false;

        for (int i = 0; i < personasJSON.length(); i++) {
            JSONObject obj = personasJSON.getJSONObject(i);
            if (obj.has("cuit")) // si tiene cuit, es un cliente
            {
                if (id_cliente.equalsIgnoreCase(obj.getString("cuit"))) {
                    personasJSON.remove(i);
                    encontrado = true;
                    i--; // CORRECCIÓN CRÍTICA
                }
            }
        }
        if (!encontrado) {
            throw new PersonaNoEncontradaEx("La persona ingresada no se encuentra!");
        }
        JSONUtiles.uploadJSON(personasJSON, "usuarios");
    }

    public void empleadoAEncargado(String id_empleado) throws IOException, JSONException, PersonaNoEncontradaEx, RolMalAsignadoEx, IllegalArgumentException {
        JSONArray personasJSON = leerPersonas();
        boolean encontrado = false;

        for (int i = 0; i < personasJSON.length(); i++) {
            JSONObject obj = personasJSON.getJSONObject(i);
            if (obj.has("contrasenia")) {
                if (obj.getString("idEmpleado").equalsIgnoreCase(id_empleado)) {
                    Eroles rol_actual = Eroles.valueOf(obj.getString("rol"));
                    if (rol_actual == Eroles.EMPLEADO) {
                        obj.put("rol", Eroles.ENCARGADO.name());
                        encontrado = true;
                    } else {
                        throw new RolMalAsignadoEx("El empleado ya es encargado!");
                    }
                }
            }
        }
        if (!encontrado) {
            throw new PersonaNoEncontradaEx("No se encontró el empleado");
        }

        JSONUtiles.uploadJSON(personasJSON, "usuarios");
    }

    public void encargadoAEmpleado(String id_empleado) throws IOException, JSONException, PersonaNoEncontradaEx, RolMalAsignadoEx, IllegalArgumentException {
        JSONArray personasJSON = leerPersonas();
        boolean encontrado = false;

        for (int i = 0; i < personasJSON.length(); i++) {
            JSONObject obj = personasJSON.getJSONObject(i);

            if (obj.has("contrasenia")) {
                if (obj.getString("idEmpleado").equalsIgnoreCase(id_empleado)) {

                    Eroles rol_actual = Eroles.valueOf(obj.getString("rol"));

                    if (rol_actual == Eroles.ENCARGADO) {
                        obj.put("rol", Eroles.EMPLEADO.name());
                        encontrado = true;
                        break;
                    } else {
                        throw new RolMalAsignadoEx("El empleado ya tiene rol EMPLEADO!");
                    }
                }
            }
        }

        if (!encontrado) {
            throw new PersonaNoEncontradaEx("No se encontró el empleado");
        }

        JSONUtiles.uploadJSON(personasJSON, "usuarios");
    }

    public ArrayList<Cliente> verClientes(String fileName) throws IOException, JSONException {

        ArrayList<Cliente> listaClientes = new ArrayList<>();

        String jsonString = JSONUtiles.downloadJSON(fileName);
        JSONArray clientesJson = new JSONArray(jsonString);

        for (int i = 0; i < clientesJson.length(); i++) {
            JSONObject obj = clientesJson.getJSONObject(i);
            // Asumiendo que Cliente tiene un constructor que acepta un JSONObject
            Cliente c = new Cliente(obj);
            listaClientes.add(c);
        }
        return listaClientes;
    }

    public JSONArray leerPersonas() throws IOException, JSONException {
        String contenido = JSONUtiles.downloadJSON("usuarios");
        if (contenido == null || contenido.isEmpty()) {
            return new JSONArray();
        }
        return new JSONArray(contenido);
    }
}