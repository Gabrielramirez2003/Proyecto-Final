import Archivos_Json.JSONUtiles;
import ENUMS.EtipoProducto;
import Excepciones.*;
import Productos.Producto;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

public class EnvolventeProductos {
    private HashMap<EtipoProducto, HashSet<Producto>> productos = new HashMap<>();

    public void agregarProducto(Producto producto) throws CodigoExistenteEx, NombreExistenteEx {
        HashSet<Producto> todos = todosLosProductos();
        if (productos.get(producto.getTipo()) != null) {

            for (Producto p : todos) {
                if (p.equals(producto)) {
                    throw new NombreExistenteEx("El nombre del producto ya existe");
                }
            }


            for (Producto prod : todos) {
                if (todos.contains(producto)) {
                    throw new CodigoExistenteEx("Ya existe un producto con ese codigo");
                }
            }


            HashSet<Producto> nuevoHash = new HashSet<>();
            nuevoHash = actualizarValores(producto.getTipo(), producto);
            productos.put(producto.getTipo(), nuevoHash);
            guardarProductosJSON();
        } else {
            for (Producto p : todos) {
                if (p.equals(producto)) {
                    throw new NombreExistenteEx("El producto ya existe");
                }
            }

            HashSet<Producto> nuevo = new HashSet<>();
            nuevo.add(producto);
            productos.put(producto.getTipo(), nuevo);
            guardarProductosJSON();

        }
    }



    public HashSet<Producto> actualizarValores(EtipoProducto tipo, Producto producto) {
        HashSet<Producto> actualizado = new HashSet<>();
        actualizado.add(producto);
        if (productos.get(tipo) != null) {
            actualizado.addAll(productos.get(tipo));
        }


        return actualizado;

    }




    public HashSet<Producto> todosLosProductos() {
        HashSet<Producto> productos = new HashSet<>();
        JSONArray a = new JSONArray();
        try {
            String contenido = JSONUtiles.downloadJSON("productos");


            if (contenido == null || contenido.isEmpty()) {
                return productos;
            }

            JSONArray array = new JSONArray(contenido);

            for (int i = 0; i < array.length(); i++) {
                JSONObject o = array.getJSONObject(i);
                productos.add(new Producto(o));
            }

        } catch (Exception e) {
            // Si el archivo NO existe → lo creamos vacío
            JSONUtiles.uploadJSON(a,"productos");  // Guarda un arreglo vacío

        }

        return productos;
    }



    public void cambiarStock(String codigo, int stock_nuevo) throws ProductoNoEncontradoEx {
        try {
            String contenido = JSONUtiles.downloadJSON("productos");
            JSONArray array = new JSONArray(contenido);
            boolean encontrado = false;

            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);

                if (obj.getString("codigo").equals(codigo)) {

                    obj.put("cantidad", stock_nuevo);
                    encontrado = true;
                    break;
                }
            }

            if (!encontrado) {
                throw new ProductoNoEncontradoEx(
                        "El producto con código " + codigo + " no se encuentra en el archivo de productos."
                );
            }

            JSONUtiles.uploadJSON(array,"productos");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Producto buscarProductoPorCodigo(String codigo) {
        HashSet<Producto> todos = todosLosProductos();
        for (Producto p : todos) {
            if (p.getCodigo().equals(codigo)) {
                return p;
            }
        }
        return null;
    }


    public void guardarProductosJSON(){
        Producto p = new Producto();
        HashSet<Producto> aux = new HashSet<>();
        for(EtipoProducto tipo : EtipoProducto.values()){
            aux = productos.get(tipo);
            if(aux != null) {
                JSONUtiles.uploadJSON(p.toJSONArray(aux), "productos");
            }
        }
    }


    public void mostrarProductosDesdeArchivo() {
        try {
            String contenido = JSONUtiles.downloadJSON("productos");

            if (contenido.isEmpty()) {
                System.out.println("El archivo está vacío.");
                return;
            }

            JSONArray array = new JSONArray(contenido);

            System.out.println("=== Productos Disponibles===");

            for (int i = 0; i < array.length(); i++) {
                JSONObject o = array.getJSONObject(i);

                String codigo = o.optString("codigo", "N/A");
                String nombre = o.optString("nombre", "N/A");
                double precio = o.optDouble("precio", 0.0);
                int cantidad = o.optInt("cantidad", 0);

                System.out.println(
                        "Código: " + codigo +
                                " | Nombre: " + nombre +
                                " | Precio: $" + precio +
                                " | Cantidad: " + cantidad
                );
            }

            System.out.println("=======================================");

        } catch (Exception e) {
            System.out.println("Error al leer los productos: " + e.getMessage());
        }
    }


    public static void modificarStock(String codigoBuscado, int nuevoStock) {
        try {

            String contenido = JSONUtiles.downloadJSON("productos");
            JSONArray array = new JSONArray(contenido);

            boolean encontrado = false;


            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);

                if (obj.getString("codigo").equals(codigoBuscado)) {

                    obj.put("cantidad", nuevoStock);
                    encontrado = true;
                    break;
                }
            }

            if (!encontrado) {
                System.out.println("Producto con código " + codigoBuscado + " no encontrado.");
                return;
            }


            JSONUtiles.uploadJSON(array, "productos");

            //System.out.println("Stock actualizado correctamente.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
