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
import java.io.IOException;
import java.util.*;

public class EnvolventeProductos {

    public void agregarProducto(Producto p) throws CodigoExistenteEx, NombreExistenteEx, IOException, JSONException {

        // Validación de negocio (lanza excepción si ya existe)
        validacionArchivoProductos.codigoExistente(p);

        JSONArray productos = leerProductos();

        productos.put(p.toJSON());
        guardarProductos(productos);
    }

    public JSONArray leerProductos() throws IOException, JSONException {
        String contenido = JSONUtiles.downloadJSON("productos");
        if (contenido == null || contenido.isEmpty()) {
            return new JSONArray();
        }
        return new JSONArray(contenido);
    }

    public void guardarProductos(JSONArray aux) throws IOException {
        JSONUtiles.uploadJSON(aux, "productos");
    }

    public ArrayList<Producto> guardarProductoXtipo(EtipoProducto tipoBuscado) throws IOException, JSONException, PrecioInvalidoEx, CampoNuloEx {
        ArrayList<Producto> lista = new ArrayList<>();

        String contenido = JSONUtiles.downloadJSON("productos");
        if (contenido == null || contenido.isEmpty()) {
            return lista;
        }

        JSONArray array = new JSONArray(contenido);

        for (int i = 0; i < array.length(); i++) {
            JSONObject o = array.getJSONObject(i);

            if (o.getString("tipo").equalsIgnoreCase(tipoBuscado.toString())) {
                Producto p = new Producto(o);
                lista.add(p);
            }
        }
        return lista;
    }

    public void buscarXid(String id) throws ProductoNoEncontradoEx, IOException, JSONException, PrecioInvalidoEx, CampoNuloEx {
        JSONArray a = leerProductos();
        JSONObject o;
        for (int i = 0; i < a.length(); i++) {
            o = a.getJSONObject(i);
            if (o.getString("codigo").equalsIgnoreCase(id)) {
                System.out.println(o.toString());
                return;
            }
        }
        throw new ProductoNoEncontradoEx("Nose encontro el producto solicitado");
    }

    public void buscarXnombre(String nombre) throws ProductoNoEncontradoEx, IOException, JSONException {
        JSONArray a = leerProductos();
        JSONObject o;

        for (int i = 0; i < a.length(); i++) {
            o = a.getJSONObject(i);
            if (o.getString("nombre").equalsIgnoreCase(nombre)) {
                System.out.println(o.toString());
                return;
            }
        }
        throw new ProductoNoEncontradoEx("No se encontro el producto solicitado");
    }

    public void eliminarProductoPorId(String id) throws ProductoNoEncontradoEx, IOException, JSONException, CampoNuloEx {
        JSONArray productos = leerProductos();
        JSONArray nuevos = new JSONArray();
        boolean encontrado = false;

        for (int i = 0; i < productos.length(); i++) {
            JSONObject o = productos.getJSONObject(i);

            if (o.getString("codigo").equalsIgnoreCase(id)) {
                encontrado = true;
                continue;
            }
            nuevos.put(o);
        }

        if (!encontrado) {
            throw new ProductoNoEncontradoEx("No se encontró el producto con ID: " + id);
        }

        guardarProductos(nuevos);
        System.out.println("Producto eliminado con exito");
    }

    public void eliminarProductoPorNombre(String nombre) throws ProductoNoEncontradoEx, IOException, JSONException {
        JSONArray productos = leerProductos();
        JSONArray nuevos = new JSONArray();
        boolean encontrado = false;

        for (int i = 0; i < productos.length(); i++) {
            JSONObject o = productos.getJSONObject(i);

            if (o.getString("nombre").equalsIgnoreCase(nombre)) {
                encontrado = true;
                continue;
            }
            nuevos.put(o);
        }

        if (!encontrado) {
            throw new ProductoNoEncontradoEx("No se encontró el producto con nombre: " + nombre);
        }

        guardarProductos(nuevos);
        System.out.println("Producto eliminado con exito");
    }

    public void modificarStock(String codigo, int stockNuevo) throws ProductoNoEncontradoEx, IOException, JSONException, opcionInvalidaEx {
        if (stockNuevo < 0) {
            throw new opcionInvalidaEx("No se puede ingresar un valor negativo");
        }

        JSONArray a = leerProductos();
        boolean flag = false;

        for (int i = 0; i < a.length(); i++) {
            JSONObject o = a.getJSONObject(i);

            if (o.getString("codigo").equalsIgnoreCase(codigo)) {
                o.put("cantidad", stockNuevo);
                flag = true;
                break;
            }
        }

        if (flag == false) {
            throw new ProductoNoEncontradoEx("El codigo que ingreso no existe");
        }

        guardarProductos(a);
    }

    public Producto buscarProductoPorCodigo(String codigo) throws ProductoNoEncontradoEx, IOException, JSONException, PrecioInvalidoEx, CampoNuloEx {
        JSONArray a = leerProductos();
        JSONObject o;

        for (int i = 0; i < a.length(); i++) {
            o = a.getJSONObject(i);
            if (o.getString("codigo").equalsIgnoreCase(codigo)) {
                return new Producto(o);
            }
        }
        throw new ProductoNoEncontradoEx("Nose encontro el producto solicitado");
    }

    public Producto buscarProductoPorNombre(String nombre) throws ProductoNoEncontradoEx, IOException, JSONException
    {
        String nombreBuscado = nombre.trim().toLowerCase();

        JSONArray productosJson = new JSONArray(JSONUtiles.downloadJSON("productos"));

        for (int i = 0; i < productosJson.length(); i++) {
            JSONObject obj = productosJson.getJSONObject(i);

            String nombreProductoActual = obj.getString("nombre").trim().toLowerCase();

            if (nombreProductoActual.equals(nombreBuscado)) {
                return new Producto(obj);
            }
        }

        throw new ProductoNoEncontradoEx("No se encontró ningún producto con el nombre: " + nombre);
    }

    public void restarStockYPersistir(String codigo, int cantidad) throws IOException, JSONException, stockInsuficienteEx, ProductoNoEncontradoEx {

        JSONArray a = leerProductos();
        boolean flag = false;

        for (int i = 0; i < a.length(); i++) {
            JSONObject o = a.getJSONObject(i);

            if (o.getString("codigo").equalsIgnoreCase(codigo)) {
                int stockActual = o.getInt("cantidad");
                if (stockActual < cantidad) {
                    throw new stockInsuficienteEx("Stock insuficiente para producto: " + o.getString("nombre"));
                }

                o.put("cantidad", stockActual - cantidad);
                flag = true;
                break;
            }
        }

        if (!flag) {
            throw new ProductoNoEncontradoEx("El código de producto no existe.");
        }

        guardarProductos(a);
    }
}
