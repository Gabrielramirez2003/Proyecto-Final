import Archivos_Json.JSONUtiles;
import Excepciones.DNIinexistenteEx;
import Excepciones.PersonaNoEncontradaEx;
import Excepciones.codigoDeSeguridadIncorrectoEx;
import Excepciones.cuentaCorrienteInexistenteEx;
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

    public void eliminarEmpleado(String id_empleado, String clave_ingresada) throws PersonaNoEncontradaEx {

        JSONArray personasJSON = new JSONArray(JSONUtiles.downloadJSON("usuarios"));
        boolean encontrado = false;

        for (int i = 0; i < personasJSON.length(); i++) {
            JSONObject obj = personasJSON.getJSONObject(i);
            if (obj.has("contrasenia")) { //si tiene contraseña es un empleado
                if (id_empleado.equals(obj.getString("id_Empleado"))) {
                    personasJSON.remove(i); //elimino el empleado
                    encontrado = true;
                }

            }
        }

        if (!encontrado) {
            throw new PersonaNoEncontradaEx("La persona no se encontró en al archivo");
        }

        JSONUtiles.uploadJSON(personasJSON, "usuarios"); //Subo el archivo json actualizado
    }

    public JSONArray leerPersonas() {
        String contenido = JSONUtiles.downloadJSON("personas");
        if (contenido == null || contenido.isEmpty()) {
            return new JSONArray();
        }
        return new JSONArray(contenido);
    }
}
