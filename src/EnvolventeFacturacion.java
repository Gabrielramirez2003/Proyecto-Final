import Archivos_Json.FacturaJSONManager;
import ENUMS.Ecuotas;
import Excepciones.ProductoNoEncontradoEx;
import Excepciones.stockInsuficienteEx;
import Excepciones.tarjetaInexistenteEx;
import Facturas.Factura;
import Interfaces.IPago;
import Personas.Cliente;
import Productos.Producto;
import Transacciones.Carrito;
import Transacciones.Credito;
import Transacciones.Tarjeta;
import org.json.JSONException;


import java.io.IOException;
import java.util.Map;


public class EnvolventeFacturacion {

    //constructor

    public EnvolventeFacturacion() {
    }

    //metodos

    public static void finalizarVenta(Cliente cliente, Carrito carrito, IPago medioDePago, Ecuotas cuotas, EnvolventeProductos inventario) throws tarjetaInexistenteEx, stockInsuficienteEx, IOException, ProductoNoEncontradoEx, JSONException {

        Factura factura = new Factura(cliente, carrito);
        double total = factura.getTotal();

        medioDePago.procesarPago(total, cuotas);

        for (Map.Entry<Producto, Integer> entry : factura.getItemsFacturados().entrySet()) {
            Producto pFacturado = entry.getKey();
            Integer cantidadVendida = entry.getValue();

            inventario.restarStockYPersistir(pFacturado.getCodigo(), cantidadVendida);

        }

        factura.setPagada(true);
        factura.setTarjetaUtilizada((Tarjeta) medioDePago);
        if (medioDePago instanceof Transacciones.Credito) {
            factura.setCuotasElegidas(cuotas);
        }

        FacturaJSONManager.guardarFactura(factura);

        System.out.println("¡VENTA EXITOSA! Factura guardada");
        carrito.vaciarCarrito();
    }
}
