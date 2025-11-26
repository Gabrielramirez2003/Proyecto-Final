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

    private EnvolventeProductos inventario;

    public EnvolventeFacturacion(EnvolventeProductos inventario)
    {
        this.inventario = inventario;
    }

    // Proceso completo de finalización de venta:
    // 1. Genera factura con los items del carrito
    // 2. Procesa el pago con el medio elegido (débito/crédito)
    // 3. Descuenta el stock del inventario
    // 4. Persiste la factura en el sistema
    public void finalizarVenta(Cliente cliente, Carrito carrito, IPago medioDePago, Ecuotas cuotas) throws tarjetaInexistenteEx, stockInsuficienteEx, IOException, ProductoNoEncontradoEx, JSONException {

        // Crear factura a partir del carrito
        Factura factura = new Factura(cliente, carrito);
        double total = factura.getTotal();

        // Procesar pago mediante polimorfismo (IPago puede ser Tarjeta o Credito)
        medioDePago.procesarPago(total, cuotas);

        // Descontar stock de cada producto vendido
        for (Map.Entry<Producto, Integer> entry : factura.getItemsFacturados().entrySet()) {
            Producto pFacturado = entry.getKey();
            Integer cantidadVendida = entry.getValue();
            this.inventario.restarStockYPersistir(pFacturado.getCodigo(), cantidadVendida);
        }

        // Actualizar estado de la factura
        factura.setPagada(true);
        factura.setTarjetaUtilizada((Tarjeta) medioDePago);
        if (medioDePago instanceof Transacciones.Credito) {
            factura.setCuotasElegidas(cuotas);
        }

        // Persistir factura en JSON
        FacturaJSONManager.guardarFactura(factura);
        System.out.println("¡VENTA EXITOSA! Factura guardada");
        carrito.vaciarCarrito();
    }
}