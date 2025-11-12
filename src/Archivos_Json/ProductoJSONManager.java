package Archivos_Json;

import Productos.Producto;
import org.json.JSONArray;

import java.util.HashMap;

public class ProductoJSONManager {
    private static final String nombre_archivo = "producto";

    public void agregarProductos(String nombre_archivo, HashMap <String, Producto>Productos)
    {
        JSONArray j = new JSONArray();

        for(Producto p : Productos.values())
        {
            j.put(p);
        }
        JSONUtiles.uploadJSON(j, nombre_archivo);
    }
}
