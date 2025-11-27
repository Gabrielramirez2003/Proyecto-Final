package Archivos_Json;

import Excepciones.cuentaCorrienteInexistenteEx;
import Excepciones.emailInvalidoEx;
import org.json.JSONArray;

import java.io.IOException;

public class validacionArchivoCuentasCorrientes {

    //metodos

    public static boolean cuentaExistente(String cuit) throws IOException {

        JSONArray a = new JSONArray(JSONUtiles.downloadJSON("cuentasCorrientes"));

        for (int i = 0; i < a.length(); i++) {

            if (a.getJSONObject(i).getString("cuit").equals(cuit)) {

                return true;

            }

        }

        return false;

    }


    public static boolean corroborarEmail(String email) throws emailInvalidoEx, IOException {

        JSONArray a = new JSONArray(JSONUtiles.downloadJSON("cuentasCorrientes"));
        boolean aux= true;
        for (int i = 0; i < a.length(); i++) {

            if (a.getJSONObject(i).getString("email").equals(email)) {

                throw new emailInvalidoEx("Ese email ya esta registrado");
            }

        }
        return aux;
    }


}
