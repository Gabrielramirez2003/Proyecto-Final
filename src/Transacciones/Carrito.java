package Transacciones;

import Excepciones.PrecioInvalidoEx;
import Productos.Producto;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Carrito {
    private HashMap<Producto, Integer> items = new HashMap<>();

    //constructor

    public Carrito() {

    }

    //getters

    public HashMap<Producto, Integer> getItems() {
        return new HashMap<>(items);
    }

    //metodos

    public void agregarProducto(Producto p, int cantidadAAgregar) {

        if (cantidadAAgregar <= 0) {
            throw new IllegalArgumentException("La cantidad a agregar debe ser mayor a cero");
        }

        if (this.items.containsKey(p)) {
            int cantidadActual = this.items.get(p);
            this.items.put(p, cantidadActual + cantidadAAgregar);
            System.out.println("Cantidad actualizada: " + p.getNombre() + " x" + (cantidadActual + cantidadAAgregar));
        } else {
            this.items.put(p, cantidadAAgregar);
            System.out.println("Producto agregado al carrito: " + p.getNombre() + " x" +  cantidadAAgregar);
        }
    }

    public double calcularTotal() {
        double subtotal = 0;

        for (Map.Entry<Producto, Integer> entry : items.entrySet()) {
            Producto p = entry.getKey();
            Integer cantidad = entry.getValue();
            subtotal += p.getPrecio() * cantidad;
        }
        return subtotal;
    }

    public void vaciarCarrito() {
        this.items.clear();
    }
}
