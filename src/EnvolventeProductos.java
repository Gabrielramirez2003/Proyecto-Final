import Archivos_Json.JSONUtiles;
import ENUMS.EtipoProducto;
import Excepciones.*;
import Productos.Producto;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

public class EnvolventeProductos {
    private HashMap<EtipoProducto, HashSet<Producto>> productos = new HashMap<>(); //El value debe ser otro hashmap de los productos del mismo tipo

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

    public void venderProducto(String valor) throws stockInsuficienteEx {
        for (HashSet<Producto> set : productos.values()) {
            for (Producto p : set) {
                if (p.getCodigo().equals(valor) || p.getNombre().equals(valor)) {
                    //Se encontró el producto, hay que agregarlo al carrito del usuario, bajarle el stock al producto y generar la factura
                }
            }
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

        try {
            String contenido = JSONUtiles.downloadJSON("productos");

            if (contenido.isEmpty()) {
                System.out.println("El archivo de productos está vacío.");
                return productos;
            }

            JSONArray array = new JSONArray(contenido);

            for (int i = 0; i < array.length(); i++) {
                JSONObject o = array.getJSONObject(i);

                Producto p = new Producto(o);

                productos.add(p);
            }

        } catch (Exception e) {
            System.out.println("Error al descargar productos: " + e.getMessage());
        }

        return productos;
    }



    public void cambiarStock(String codigo, int stock_nuevo) throws ProductoNoEncontradoEx {
        Iterator<Map.Entry<EtipoProducto, HashSet<Producto>>> it = productos.entrySet().iterator();
        boolean encontrado = false;

        while (it.hasNext()) {
            Map.Entry<EtipoProducto, HashSet<Producto>> entrada = it.next();
            HashSet<Producto> set_productos = entrada.getValue();

            Iterator<Producto> it_prod = set_productos.iterator();
            while (it_prod.hasNext()) {
                Producto p = it_prod.next();
                if (p.getCodigo().equals(codigo)) {
                    p.cambiarStock(codigo, stock_nuevo);
                    encontrado = true;
                }
            }
        }
        if (encontrado == false) {
            throw new ProductoNoEncontradoEx("El producto al que se le quiere cambiar el stock no se encuentra en el inventario");
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


}
