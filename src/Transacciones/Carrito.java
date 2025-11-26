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
        // El constructor está vacío
    }

    //metodos


    public void agregarProducto(Producto pInventario, int cantidadAAgregar) {
        if (cantidadAAgregar <= 0) {
            throw new IllegalArgumentException("La cantidad a agregar debe ser mayor a cero.");
        }

        if (this.items.containsKey(pInventario)) {
            int cantidadActual = this.items.get(pInventario);
            this.items.put(pInventario, cantidadActual + cantidadAAgregar);
            System.out.println("Cantidad actualizada: " + pInventario.getNombre() + " x" + (cantidadActual + cantidadAAgregar));
        } else {
            this.items.put(pInventario, cantidadAAgregar);
            System.out.println("Producto agregado al carrito: " + pInventario.getNombre() + " x" + cantidadAAgregar);
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

    //getters & setters

    public HashMap<Producto, Integer> getItems() {
        return new HashMap<>(items);
    }
}