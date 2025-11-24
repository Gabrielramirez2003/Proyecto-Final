import Archivos_Json.JSONUtiles;
import Excepciones.*;
import Personas.Cliente;
import org.json.JSONArray;
import org.json.JSONObject;

public class EnvolventePersona {

    public EnvolventePersona() {
    }

    public String verClientes(String nombre_archivo) throws cuentaCorrienteInexistenteEx {
        String contenido = JSONUtiles.downloadJSON(nombre_archivo);
        JSONArray personaJSON = new JSONArray(contenido);

        if (personaJSON == null) {
            throw new cuentaCorrienteInexistenteEx("Error! el archivo json de personas esta vacío!");
        }
        StringBuilder sb = new StringBuilder();

        if (personaJSON != null) {
            for (int i = 0; i < personaJSON.length(); i++) {
                JSONObject obj = personaJSON.getJSONObject(i);

                if (obj.has("cuit")) //Valido que sea un cliente
                {
                    sb.append(obj);
                    sb.append("\n\n");
                }
            }
        }

        return sb.toString();
    }

    public void eliminarEmpleado(int id_empleado) throws PersonaNoEncontradaEx {

        String contenido = JSONUtiles.downloadJSON("personas");
        JSONArray personasJSON = new JSONArray(contenido);
        boolean encontrado = false;

        for (int i = 0; i < personasJSON.length(); i++) {
            JSONObject obj = personasJSON.getJSONObject(i);
            if (obj.has("contrasenia")) { //si tiene contraseña es un empleado
                if (id_empleado == obj.getInt("idEmpleado")) {
                    personasJSON.remove(i); //elimino el empleado
                    encontrado = true;
                }

            }
        }

        if (!encontrado) {
            throw new PersonaNoEncontradaEx("La persona no se encontró en al archivo");
        }

        JSONUtiles.uploadJSON(personasJSON, "personas"); //Subo el archivo json actualizado
    }

    public void eliminarCliente(String id_cliente) throws PersonaNoEncontradaEx {
        String contenido = JSONUtiles.downloadJSON("personas");
        JSONArray personasJSON = new JSONArray(contenido);
        boolean encontrado = false;

        for (int i = 0; i < personasJSON.length(); i++) {
            JSONObject obj = personasJSON.getJSONObject(i);
            if (obj.has("cuit")) // si tiene cuit, es un cliente
            {
                if (id_cliente.equals(obj.getString("idcliente"))) {
                    personasJSON.remove(i);
                    encontrado = true;
                }
            }
        }
        if (!encontrado) {
            throw new PersonaNoEncontradaEx("La persona ingresada no se encuentra!");
        }
        JSONUtiles.uploadJSON(personasJSON, "personas");
    }

    public void empleadoAEncargado(int id_empleado) throws PersonaNoEncontradaEx {
        String contenido = JSONUtiles.downloadJSON("personas");
        JSONArray personasJSON = new JSONArray(contenido);
        boolean encontrado = false;

        for (int i = 0; i < personasJSON.length(); i++) {
            JSONObject obj = personasJSON.getJSONObject(i);
            if (obj.has("contrasenia")) {
                
            }
        }

    }

    public JSONArray leerPersonas() {
        String contenido = JSONUtiles.downloadJSON("personas");
        if (contenido == null || contenido.isEmpty()) {
            return new JSONArray();
        }
        return new JSONArray(contenido);
    }
}
