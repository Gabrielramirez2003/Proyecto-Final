import Archivos_Json.JSONUtiles;
import Archivos_Json.validacionArchivoProductos;
import ENUMS.EtipoProducto;
import Excepciones.*;
import Productos.Producto;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class EnvolventeProductos {

    public void agregarProducto(String codigo, String nombre, double precio, int cantidad, EtipoProducto tipo) throws CodigoExistenteEx, NombreExistenteEx {
        try {
            Producto p = new Producto(codigo, nombre, precio, cantidad, tipo);
            if (validacionArchivoProductos.codigoExistente(p)) {

                return;
            }
            JSONArray productos = leerProductos();
            productos.put(p.toJSON());
            guardarProductos(productos);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    //Descarga todos los productos de el archivo y los guarda en un JSONArray que devuelve
    public JSONArray leerProductos(){
        String contenido = JSONUtiles.downloadJSON("productos");
        if (contenido == null || contenido.isEmpty()) {
            return new JSONArray();
        }
        return new JSONArray(contenido);
    }



    //Escribe el archivo productos con un JSONArray que recibe por parametros
    public void guardarProductos(JSONArray aux) {
        JSONUtiles.uploadJSON(aux, "productos");
    }




    public ArrayList<Producto> guardarProductoXtipo(EtipoProducto tipoBuscado) {
        ArrayList<Producto> lista = new ArrayList<>();

        String contenido = JSONUtiles.downloadJSON("productos");
        if (contenido == null || contenido.isEmpty()) {
            return lista;
        }

        JSONArray array = new JSONArray(contenido);

        for (int i = 0; i < array.length(); i++) {
            JSONObject o = array.getJSONObject(i);


            if (o.getString("tipo").equals(tipoBuscado.toString())) {
                Producto p = new Producto(o);
                lista.add(p);
            }
        }

        return lista;
    }

    



}
