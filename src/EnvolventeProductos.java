import Archivos_Json.JSONUtiles;
import ENUMS.EtipoProducto;
import Excepciones.CodigoExistenteEx;
import Excepciones.NombreExistenteEx;
import Excepciones.productoExistenteEx;
import Productos.Producto;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class EnvolventeProductos {
    private HashMap<String, Producto> productos = new HashMap<>(); //El value debe ser otro hashmap de los productos del mismo tipo

    public void agregarProducto(Producto p) throws CodigoExistenteEx, NombreExistenteEx {
        if (productos.containsKey(p.getCodigo())) {
            throw new CodigoExistenteEx("Ya existe un producto con ese codigo");
        }

        for (Producto prod : productos.values()) {
            if (prod.getNombre().equalsIgnoreCase(p.getCodigo())) {
                throw new NombreExistenteEx("El nombre del producto ya existe");
            }
        }

        productos.put(p.getCodigo(), p);
    }


}
