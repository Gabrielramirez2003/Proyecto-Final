import Archivos_Json.JSONUtiles;
import ENUMS.EtipoProducto;
import Excepciones.CodigoExistenteEx;
import Excepciones.NombreExistenteEx;
import Excepciones.ProductoNoEncontradoEx;
import Excepciones.productoExistenteEx;
import Productos.Producto;
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
        } else {
            for (Producto p : todos) {
                if (p.equals(producto)) {
                    throw new NombreExistenteEx("El producto ya existe");
                }
            }

            HashSet<Producto> nuevo = new HashSet<>();
            nuevo.add(producto);
            productos.put(producto.getTipo(), nuevo);


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


    public void imprimirProductos() {
        for (Map.Entry<EtipoProducto, HashSet<Producto>> entrada : productos.entrySet()) {
            EtipoProducto tipo = entrada.getKey();
            HashSet<Producto> productos = entrada.getValue();

            System.out.println("=== " + tipo + " ===");

            if (productos.isEmpty()) {
                System.out.println("  (sin productos)");
            } else {
                for (Producto p : productos) {
                    System.out.println("  - " + p);
                }
            }

            System.out.println(); // línea en blanco entre tipos
        }
    }

    public HashSet<Producto> todosLosProductos() {
        HashSet<Producto> todos = new HashSet<>();
        for (Map.Entry<EtipoProducto, HashSet<Producto>> entrada : productos.entrySet()) {
            EtipoProducto tipo = entrada.getKey();
            todos.addAll(productos.get(tipo));
        }
        return todos;
    }

    public void imprimirtodos() {
        for (Producto p : todosLosProductos()) {
            System.out.println(p.toString());
        }
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
        if(encontrado == false)
        {
            throw new ProductoNoEncontradoEx("El producto al que se le quiere cambiar el stock no se encuentra en el inventario");
        }
    }
}
