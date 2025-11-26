package Archivos_Json;

import Excepciones.CodigoExistenteEx;
import Excepciones.NombreExistenteEx;
import Productos.Producto;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class validacionArchivoProductos {

    public static boolean codigoExistente(Producto p) throws CodigoExistenteEx, NombreExistenteEx, IOException {

        String contenido = JSONUtiles.downloadJSON("productos");


        if (contenido == null || contenido.isEmpty()) {

            return false;

        }


        JSONArray j = new JSONArray(contenido);


        for (int i = 0; i < j.length(); i++) {

            JSONObject objeto = j.getJSONObject(i);


            if (objeto.getString("codigo").equals(p.getCodigo())) {

                throw new CodigoExistenteEx("Ese producto ya existe");

            }


            if (objeto.getString("nombre").equals(p.getNombre())) {

                throw new NombreExistenteEx("Ese producto ya existe");

            }

        }


        return false;

    }


}