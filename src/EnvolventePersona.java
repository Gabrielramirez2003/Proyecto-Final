import Archivos_Json.JSONUtiles;
import ENUMS.Eroles;
import Excepciones.*;
import Personas.Cliente;
import Personas.Empleado;
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

        String idBuscado = id_empleado.trim();

        for (int i = 0; i < personasJSON.length(); i++) {
            JSONObject obj = personasJSON.getJSONObject(i);
            if (obj.has("contrasenia") && obj.has("idEmpleado")) {
                String idEnJSON = obj.getString("idEmpleado").trim();
                if (idBuscado.equalsIgnoreCase(idEnJSON)) {
                    personasJSON.remove(i);
                    encontrado = true;
                    break;
                }
            }
        }

        if (!encontrado) {
            throw new PersonaNoEncontradaEx("No se encontró el empleado con ID: " + id_empleado);
        }

        JSONUtiles.uploadJSON(personasJSON, "usuarios");
    }

    public void eliminarCliente(String cuit_cliente) throws IOException, JSONException, PersonaNoEncontradaEx {
        String contenidoClientes = JSONUtiles.downloadJSON("cuentasCorrientes");
        JSONArray clientesJSON = new JSONArray(contenidoClientes);
        boolean encontrado = false;
        String cuitBuscado = cuit_cliente.trim().replace("-", "");

        for (int i = 0; i < clientesJSON.length(); i++) {
            JSONObject obj = clientesJSON.getJSONObject(i);
            String cuitActual = obj.getString("cuit").trim().replace("-", "");

            if (cuitBuscado.equalsIgnoreCase(cuitActual)) {
                clientesJSON.remove(i);
                encontrado = true;
                break;
            }
        }

        if (!encontrado) {
            throw new PersonaNoEncontradaEx("La persona ingresada no se encuentra!");
        }

        JSONUtiles.uploadJSON(clientesJSON, "cuentasCorrientes");
    }

    // Asciende un empleado a rol de encargado
    // Modifica el rol en el archivo usuarios.json después de validar que sea empleado
    public void empleadoAEncargado(String id_empleado)
            throws IOException, JSONException, PersonaNoEncontradaEx, RolMalAsignadoEx, IllegalArgumentException
    {
        // 1. Validación de la entrada
        if (id_empleado == null || id_empleado.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID del empleado no puede estar vacío.");
        }

        // Normalizamos la entrada una sola vez
        String idBuscadoNormalizado = id_empleado.trim();

        JSONArray personasJSON = leerPersonas();
        boolean encontrado = false;

        for (int i = 0; i < personasJSON.length(); i++) {
            JSONObject empleado = personasJSON.getJSONObject(i);

            // 2. Verificación del JSON: Solo procesamos si tiene ambas claves necesarias
            if (empleado.has("contrasenia") && empleado.has("idEmpleado")) {

                // Leemos el ID del JSON de forma segura y normalizamos
                String idActual = empleado.optString("idEmpleado", "").trim();

                // 3. Comparación de IDs
                if (idActual.equalsIgnoreCase(idBuscadoNormalizado)) {
                    encontrado = true;

                    // 4. Lógica de cambio de Rol
                    // Usamos optString y Eroles para evitar errores si el rol no está bien guardado
                    String rolActualStr = empleado.optString("rol", Eroles.EMPLEADO.name());
                    Eroles rolActual = Eroles.valueOf(rolActualStr.toUpperCase());

                    if (rolActual == Eroles.EMPLEADO) {
                        // Ascenso
                        empleado.put("rol", Eroles.ENCARGADO.name());
                        break; // Salir del bucle al hacer el cambio
                    } else if (rolActual == Eroles.ENCARGADO) {
                        throw new RolMalAsignadoEx("El empleado ya es un encargado.");
                    } else {
                        // Caso de rol inconsistente o nulo
                        throw new RolMalAsignadoEx("El empleado tiene un rol no válido: " + rolActualStr);
                    }
                }
            }
        }

        // 5. Manejo de 'Not Found'
        if (!encontrado) {
            throw new PersonaNoEncontradaEx("No se encontró el empleado con ID: " + id_empleado);
        }

        // 6. Persistencia (Guardar los cambios)
        JSONUtiles.uploadJSON(personasJSON, "usuarios");
    }

    // Desciende un encargado a rol de empleado
    // Revierte permisos administrativos en el sistema
    public void encargadoAEmpleado(String id_encargado)
            throws IOException, JSONException, PersonaNoEncontradaEx, RolMalAsignadoEx, IllegalArgumentException
    {
        // 1. Validación de Entrada
        if (id_encargado == null || id_encargado.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID del encargado no puede estar vacío.");
        }

        // Normalizamos la entrada para una búsqueda limpia
        String idBuscadoNormalizado = id_encargado.trim();

        JSONArray personasJSON = leerPersonas();
        boolean encontrado = false;

        for (int i = 0; i < personasJSON.length(); i++) {
            JSONObject encargado = personasJSON.getJSONObject(i);

            // 2. Robustez del JSON: Solo procesamos si es un usuario que tiene contrasenia e idEmpleado
            if (encargado.has("contrasenia") && encargado.has("idEmpleado")) {

                // Leemos el ID del JSON de forma segura (optString) y normalizamos
                String idActual = encargado.optString("idEmpleado", "").trim();

                // 3. Comparación de IDs
                if (idActual.equalsIgnoreCase(idBuscadoNormalizado)) {
                    encontrado = true;

                    // 4. Lógica de cambio de Rol
                    String rolActualStr = encargado.optString("rol", Eroles.EMPLEADO.name());
                    Eroles rolActual = Eroles.valueOf(rolActualStr.toUpperCase());

                    if (rolActual == Eroles.ENCARGADO) {
                        // Descenso
                        encargado.put("rol", Eroles.EMPLEADO.name());
                        break; // Salir del bucle tras el cambio exitoso
                    } else if (rolActual == Eroles.EMPLEADO) {
                        throw new RolMalAsignadoEx("El usuario ya es un empleado.");
                    } else {
                        // Rol no reconocido o inconsistente
                        throw new RolMalAsignadoEx("El usuario tiene un rol no válido: " + rolActualStr);
                    }
                }
            }
        }

        // 5. Manejo de 'Not Found'
        if (!encontrado) {
            throw new PersonaNoEncontradaEx("No se encontró el encargado con ID: " + id_encargado);
        }

        // 6. Persistencia
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

    public ArrayList<Empleado> verEmpleados() throws IOException, JSONException {
        ArrayList<Empleado> listaEmpleados = new ArrayList<>();

        // Asumo que leerPersonas() carga el JSONArray del archivo "usuarios.json"
        JSONArray empleadosJson = leerPersonas();

        for (int i = 0; i < empleadosJson.length(); i++) {
            JSONObject obj = empleadosJson.getJSONObject(i);

            // Solo incluimos objetos que tienen ID de empleado (son usuarios del sistema)
            if (obj.has("idEmpleado")) {
                // Asumo que Empleado tiene un constructor que acepta JSONObject o un método de carga
                Empleado e = new Empleado();
                // Esto es solo un ejemplo, deberías usar un constructor o un setter de carga real.

                // Llenar manualmente (requiere setters, como en tu login fix)
                e.setNombre(obj.getString("nombre"));
                e.setIdEmpleado(obj.getString("idEmpleado"));
                e.setRol(Eroles.valueOf(obj.getString("rol").toUpperCase()));

                listaEmpleados.add(e);
            }
        }

        return listaEmpleados;
    }
}