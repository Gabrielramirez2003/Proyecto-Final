package Facturas;

import ENUMS.Ecuotas;
import Personas.Cliente;
import Productos.Producto;
import Transacciones.Carrito;
import Transacciones.Tarjeta;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Factura {
    private int idFactura;
    private static int contadorFacturas = 1;
    private double total;
    private boolean pagada;
    private Cliente cliente;
    private HashMap<Producto, Integer> itemsFacturados = new HashMap<>();
    private Tarjeta tarjetaUtilizada;
    private Ecuotas cuotasElegidas;

    // constructor

    public Factura(Cliente cliente, Carrito carrito) {
        this.idFactura = contadorFacturas++;
        this.cliente = cliente;
        this.pagada = false;
        this.total = carrito.calcularTotal();
        this.itemsFacturados.putAll(carrito.getItems());
    }

    //getters && setters

    public HashMap<Producto, Integer> getItemsFacturados() {
        return itemsFacturados;
    }

    public boolean isPagada() {
        return pagada;
    }

    public void setPagada(boolean pagada) {
        this.pagada = pagada;
    }

    public void setTarjetaUtilizada(Tarjeta tarjetaUtilizada) {
        this.tarjetaUtilizada = tarjetaUtilizada;
    }

    public void setCuotasElegidas(Ecuotas cuotasElegidas) {
        this.cuotasElegidas = cuotasElegidas;
    }

    public int getIdFactura() {
        return idFactura;
    }

    public double getTotal() {
        return total;
    }

    //metodos

    public JSONObject toJSON(){
        JSONObject jsonFactura = new JSONObject();
        jsonFactura.put("idFactura", this.idFactura);
        jsonFactura.put("total", this.total);
        jsonFactura.put("pagada", this.pagada);

        if(cliente != null){
            jsonFactura.put("cuit_cliente", this.cliente.getCuit());
        } else {
            jsonFactura.put("cuit_cliente", "Consumidor Final");
        }

        JSONArray jsonProductos = new JSONArray();
        for (Map.Entry<Producto, Integer> entry : itemsFacturados.entrySet()) {
            Producto p = entry.getKey();
            Integer cantidadVendida = entry.getValue();

            JSONObject itemJson = p.toJSON();
            itemJson.put("cantidad_vendida", cantidadVendida);
            jsonProductos.put(itemJson);
        }
        jsonFactura.put("productos", jsonProductos);

        return jsonFactura;
    }
}
