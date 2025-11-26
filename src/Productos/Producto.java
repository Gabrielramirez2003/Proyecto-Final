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

    // constructor

    public Producto() {

    }


    // Constructor principal con validaciones de negocio
    // Asegura que código y nombre no sean nulos y que el precio sea válido
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


    // Constructor desde JSON con validaciones completas
    // Usa optString/optDouble para evitar excepciones si falta algún campo
    public Producto(JSONObject o) throws PrecioInvalidoEx, CampoNuloEx {

        this.codigo = o.optString("codigo", null);

        if (codigo == null || codigo.trim().isEmpty()) {

            throw new CampoNuloEx("El código no puede estar vacío!");

        }

        this.nombre = o.optString("nombre", null);

        if (nombre == null || nombre.trim().isEmpty()) {

            throw new CampoNuloEx("El nombre no puede estar vacío!");

        }

        this.precio = o.optDouble("precio", 0.0);

        if (precio <= 0) {

            throw new PrecioInvalidoEx("El precio debe ser mayor a 0!");

        }

        this.cantidad = o.optInt("cantidad", 0);

        String tipoStr = o.optString("tipo", null);

        if (tipoStr == null || tipoStr.trim().isEmpty()) {

            throw new CampoNuloEx("El tipo no puede estar vacío!");

        }

        this.tipo = EtipoProducto.valueOf(tipoStr);

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

        if (this.cantidad < cantidad) {

            throw new stockInsuficienteEx("Stock insuficiente");

        } else {

            this.cantidad -= cantidad;

            return true;

        }

    }


    public JSONObject toJSON() {

        JSONObject o = new JSONObject();

        o.put("codigo", this.codigo);

        o.put("nombre", this.nombre);

        o.put("cantidad", this.cantidad);

        o.put("precio", this.precio);

        o.put("tipo", this.tipo);

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

        return Objects.equals(codigo, producto.codigo);

    }


    @Override

    public int hashCode() {

        return Objects.hash(codigo);

    }


    @Override
    public String toString() {
        return "ID: " + codigo +
                " | Nombre: " + nombre +
                " | Precio: $" + String.format("%.2f", precio) +
                " | Stock: " + cantidad +
                " | Categoría: " + tipo.name().replace("_", " ");
    }
}

