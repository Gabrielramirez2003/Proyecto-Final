package Archivos_Json;

import org.json.JSONArray;
import org.json.JSONObject;

public class validacionArchivoProductos {

    public static boolean codigoExistente(){
        JSONObject o = new JSONObject(JSONUtiles.downloadJSON("productos"));
        return true;
    }
}
