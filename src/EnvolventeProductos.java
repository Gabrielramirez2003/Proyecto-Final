import Archivos_Json.JSONUtiles;
import Archivos_Json.validacionArchivoProductos;
import ENUMS.EtipoProducto;
import Excepciones.*;
import Productos.Producto;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class EnvolventeProductos {

    public void agregarProducto(Producto p) throws CodigoExistenteEx, NombreExistenteEx {
        try {

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

    public void buscarXid(String id) throws ProductoNoEncontradoEx {
        try {
        JSONArray a = leerProductos();
        JSONObject o;
            for (int i = 0; i < a.length(); i++) {
                o =  a.getJSONObject(i);
                if(o.getString("codigo").equalsIgnoreCase(id)){
                    System.out.println(o.toString());
                    return;
                }
            }
                throw new ProductoNoEncontradoEx("Nose encontro el producto solicitado");
        } catch (JSONException e) {
            System.out.println(e.getMessage());
        }
    }

    public void buscarXnombre(String nombre) throws ProductoNoEncontradoEx {
        try {
            JSONArray a = leerProductos();
            JSONObject o;

            for (int i = 0; i < a.length(); i++) {
                o =  a.getJSONObject(i);
                if(o.getString("nombre").equalsIgnoreCase(nombre)){
                    System.out.println(o.toString());
                    return;
                }
            }
                throw new ProductoNoEncontradoEx("Nose encontro el producto solicitado");
        } catch (JSONException e) {
            System.out.println(e.getMessage());
        }
    }


    public void eliminarProductoPorId(String id) throws ProductoNoEncontradoEx {
        try {
            JSONArray productos = leerProductos();
            JSONArray nuevos = new JSONArray();
            boolean encontrado = false;

            for (int i = 0; i < productos.length(); i++) {
                JSONObject o = productos.getJSONObject(i);

                if (o.getString("codigo").equals(id)) {
                    encontrado = true;
                    continue; // NO lo agrego → queda eliminado
                }

                nuevos.put(o); // Lo mantengo
            }

            if (!encontrado) {
                throw new ProductoNoEncontradoEx("No se encontró el producto con ID: " + id);
            }


            guardarProductos(nuevos);
            System.out.println("Producto eliminado con exito");
        } catch (JSONException e) {
            throw new ProductoNoEncontradoEx("Error procesando el archivo JSON.");
        }
    }


    public void eliminarProductoPorNombre(String nombre) throws ProductoNoEncontradoEx {
        try {
            JSONArray productos = leerProductos();
            JSONArray nuevos = new JSONArray();
            boolean encontrado = false;

            for (int i = 0; i < productos.length(); i++) {
                JSONObject o = productos.getJSONObject(i);

                if (o.getString("nombre").equals(nombre)) {
                    encontrado = true;
                    continue; // NO lo agrego → queda eliminado
                }

                nuevos.put(o); // Lo mantengo
            }

            if (!encontrado) {
                throw new ProductoNoEncontradoEx("No se encontró el producto con nombre: " + nombre);
            }


            guardarProductos(nuevos);
            System.out.println("Producto eliminado con exito");
        } catch (JSONException e) {
            throw new ProductoNoEncontradoEx("Error procesando el archivo JSON.");
        }
    }

    public void modificarStock(String codigo, int stockNuevo){
        try{
            JSONArray a = leerProductos();
            boolean flag =  false;
            if(stockNuevo < 0 ){
                throw new opcionInvalidaEx("No se puede ingresar un valor negativo");
            }
            for(int i = 0; i < a.length(); i++){
                JSONObject o = a.getJSONObject(i);

                if(o.getString("codigo").equalsIgnoreCase(codigo)){
                    o.put("cantidad", stockNuevo);
                    flag = true;
                    break;
                }
            }

            if(flag == false){
                throw new ProductoNoEncontradoEx("El codigo que ingreso no existe");
            }

            guardarProductos(a);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }


    public Producto buscarProductoPorCodigo(String codigo) throws ProductoNoEncontradoEx {
        try{
            JSONArray a = leerProductos();
            JSONObject o;
            for(int i = 0; i < a.length(); i++){
                o = a.getJSONObject(i);
                if (o.getString("codigo").equalsIgnoreCase(codigo)) {
                    return new Producto(o);
                }
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }


}
