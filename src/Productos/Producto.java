package Productos;

import ENUMS.EtipoProducto;
import Excepciones.CampoNuloEx;
import Excepciones.PrecioInvalidoEx;
import Excepciones.stockInsuficienteEx;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Objects;

public class Producto {
    private String codigo;
    private String nombre;
    private double precio;
    private int cantidad;
    private EtipoProducto tipo;


    //constructor

    public Producto() {
    }

    public Producto(String codigo, String nombre, double precio, int cantidad, EtipoProducto tipo) throws PrecioInvalidoEx, CampoNuloEx {
        this.codigo = codigo;
        if (codigo == null) {
            throw new CampoNuloEx("El codigo no puede estar vacio!");
        }
        this.nombre = nombre;
        if (nombre == null) {
            throw new CampoNuloEx("El nombre no puede estar vacio");
        }
        this.precio = precio;
        if (precio <= 0) {
            throw new PrecioInvalidoEx("El precio debe ser mayor a 0!");
        }
        this.cantidad = cantidad;
        this.tipo = tipo;
    }

    public Producto(JSONObject o) {
        this.codigo = o.getString("codigo");
        this.nombre = o.getString("nombre");
        this.precio = o.getDouble("precio");
        this.cantidad = o.getInt("cantidad");
    }

    //getters & setters

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public EtipoProducto getTipo() {
        return tipo;
    }

    public void setTipo(EtipoProducto tipo) {
        this.tipo = tipo;
    }

    //metodos

    public void agregarStock(int cantidad) {
        this.cantidad += cantidad;
    }

    public boolean restarStock(int cantidad) throws stockInsuficienteEx {
        if (this.cantidad <= cantidad) {
            this.cantidad -= cantidad;
            return true;
        } else {
            throw new stockInsuficienteEx("Stock insuficiente");
        }
    }

    public void cambiarStock(String id, int stock_nuevo) {
        if (this.codigo.equals(id)) {
            this.cantidad = stock_nuevo;
        }
    }

    public void cambiarStock(int stock_nuevo, String name) {
        if (this.nombre.equals(name)) {
            this.cantidad = stock_nuevo;
        }
    }

    public JSONObject toJSON() {
        JSONObject o = new JSONObject();
        o.put("codigo", this.codigo);
        o.put("nombre", this.nombre);
        o.put("cantidad", this.cantidad);
        o.put("precio", this.precio);

        return o;
    }

    //hashset to jsonarray
    public JSONArray toJSONArray(HashSet<Producto> h) {
        JSONArray a = new JSONArray();
        for (Producto p : h) {
            a.put(p.toJSON());
        }

        return a;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Producto producto = (Producto) o;
        return Objects.equals(codigo, producto.codigo) || Objects.equals(nombre, producto.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo, nombre);
    }

    @Override
    public String toString() {
        return "Producto{" +
                "nombre='" + nombre + '\'' +
                ", precio=" + precio +
                '}';
    }
}
