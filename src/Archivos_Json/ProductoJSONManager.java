package Archivos_Json;

import Productos.Producto;
import org.json.JSONArray;

import java.io.IOException;
import java.util.HashMap;

public class ProductoJSONManager {
    private static final String nombre_archivo = "producto";


    public void agregarProductos(String nombre_archivo, HashMap<String, Producto> Productos) throws IOException {

        JSONArray j = new JSONArray();


        for (Producto p : Productos.values()) {

            j.put(p.toJSON());

        }

        JSONUtiles.uploadJSON(j, nombre_archivo);

    }


    public static boolean corroborarNombre(JSONArray j, String nombre) {

        for (int i = 0; i < j.length(); i++) {

            if (j.getJSONObject(i).getString("nombre").equals(nombre)) {

                return true;

            }

        }

        return false;

    }


    public static boolean corroborarCodigo(JSONArray j, String codigo) {

        for (int i = 0; i < j.length(); i++) {

            if (j.getJSONObject(i).getString("codigo").equals(codigo)) {

                return true;

            }

        }

        return false;

    }

}

