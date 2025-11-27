import Archivos_Json.FacturaJSONManager;
import Creacion_PDF_EnvioMail.CreadorPDF;
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
import com.itextpdf.text.DocumentException;
import jakarta.mail.MessagingException;
import org.json.JSONException;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.Scanner;


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
    public void finalizarVenta(Cliente cliente, Carrito carrito, IPago medioDePago, Ecuotas cuotas,Scanner sc) throws tarjetaInexistenteEx, stockInsuficienteEx, IOException, ProductoNoEncontradoEx, JSONException, MessagingException, DocumentException {

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

        opcionesFacturacion(sc,factura,cliente.getEmail());

        // Persistir factura en JSON
        FacturaJSONManager.guardarFactura(factura);
        System.out.println("¡VENTA EXITOSA! Factura guardada");
        carrito.vaciarCarrito();

    }

    public void crearPDFfactura(Factura factura) throws DocumentException, FileNotFoundException {
        CreadorPDF.generarFacturaPDF(factura);
    }

    public void enviarFacturaMail(Factura factura, String destinatario) throws MessagingException, DocumentException {
        CreadorPDF.enviarFacturaPorEmail(factura,destinatario);
    }

    public void opcionesFacturacion(Scanner sc, Factura factura, String destinatario) throws DocumentException, FileNotFoundException, MessagingException {
        int opcion;
        System.out.println("1. Descargar factura");
        System.out.println("2. Enviar factura por Email");
        System.out.println("3. Salir");

        opcion= sc.nextInt();
        switch (opcion){
            case 1:
                crearPDFfactura(factura);
                break;
            case 2:
                enviarFacturaMail(factura, destinatario);
                break;
            case 3:
                break;
        }
    }
}