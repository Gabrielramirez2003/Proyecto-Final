package Archivos_Json;

import Personas.Cliente;
import Transacciones.Credito;
import Transacciones.Tarjeta;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.HashSet;

public class validacionesArchivoTarjetas {

    public static boolean tarjetaExistente(String numeroTarjeta) throws IOException, JSONException {
        JSONArray a = new JSONArray(JSONUtiles.downloadJSON("cuentasCorrientes"));

        for (int i = 0; i < a.length(); i++) {
            Cliente c = new Cliente(a.getJSONObject(i));

            for(Tarjeta t : c.getTarjetasDebito()){
                if(t.getNumeroTarjeta().equals(numeroTarjeta)){
                    return false;
                }
            }

            for(Credito t : c.getTarjetasCredito()){
                if(t.getNumeroTarjeta().equals(numeroTarjeta)){
                    return false;
                }
            }
        }

        return true;
    }
}
