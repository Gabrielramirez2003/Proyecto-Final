import Archivos_Json.JSONUtiles;
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

        if(personaJSON == null)
        {
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

    public JSONArray leerPersonas() {
        String contenido = JSONUtiles.downloadJSON("personas");
        if (contenido == null || contenido.isEmpty()) {
            return new JSONArray();
        }
        return new JSONArray(contenido);
    }
}
