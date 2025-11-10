package Archivos_Json;

import Personas.Cliente;
import Transacciones.Credito;
import Transacciones.Tarjeta;
import org.json.JSONArray;

import java.util.HashSet;

public class validacionesArchivoTarjetas {


    //ESTA CLASE CONTIENE VALIDACIONES Y METODOS UTILES PARA EL REGISTRO DE TARJETAS

    public static boolean tarjetaExistente(String numeroTarjeta){
        JSONArray a = new JSONArray(JSONUtiles.downloadJSON("cuentasCorrientes"));
        HashSet<Tarjeta> tarjetasDebito= new HashSet<>();
        HashSet<Credito> tarjetasCredito= new HashSet<>();
        for (int i = 0; i < a.length(); i++) {
            Cliente c = new Cliente(a.getJSONObject(i));
            for (int q = 0; q < a.length(); q++){
                tarjetasDebito = c.getTarjetasDebito().getTarjetas();
                tarjetasCredito =c.getTarjetasCredito().getTarjetas();
            }
        }

        for(Tarjeta t : tarjetasDebito){
            if(t.getNumeroTarjeta().equals(numeroTarjeta)){
                return false;
            }
        }

        for(Credito t : tarjetasCredito){
            if(t.getNumeroTarjeta().equals(numeroTarjeta)){
                return false;
            }
        }

        return true;

    }
}
