package Archivos_Json;

import Excepciones.cuentaCorrienteInexistenteEx;
import org.json.JSONArray;

public class validacionArchivoCuentasCorrientes {

    //metodos



    //Metodo para verificar si el cliente tiene o no una cuenta creada
    public static boolean cuentaExistente(String cuit)throws cuentaCorrienteInexistenteEx {
        JSONArray a = new JSONArray(JSONUtiles.downloadJSON("cuentasCorrientes"));
        for(int i=0;i<a.length();i++){
            if(a.getJSONObject(i).getString("cuit").equals(cuit)){
                return true;
            }
        }
        return false;
    }

}
