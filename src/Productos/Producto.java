package Productos;

import Excepciones.stockInsuficienteEx;
import org.json.JSONObject;

public class Producto {
    private String codigo;
    private String nombre;
    private double precio;
    private int cantidad;


    //constructor

    public Producto() {
    }

    public Producto(String codigo, String nombre, double precio, int cantidad) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.precio = precio;
        this.cantidad = cantidad;
    }

    public Producto (JSONObject o){
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

    //metodos

    public void agregarStock(int cantidad) {
        this.cantidad += cantidad;
    }

    public boolean restarStock(int  cantidad) throws stockInsuficienteEx {
        if(this.cantidad <= cantidad) {
            this.cantidad -= cantidad;
            return true;
        }else {
            throw new stockInsuficienteEx("Stock insuficiente");
        }
    }

    @Override
    public String toString() {
        return "Producto{" +
                "nombre='" + nombre + '\'' +
                ", precio=" + precio +
                '}';
    }
}
