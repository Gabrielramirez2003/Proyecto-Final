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


import java.io.IOException;
import java.util.Map;
import java.util.Scanner;


public class EnvolventeFacturacion {

    //constructor

    public EnvolventeFacturacion() {
    }

    //metodos

    public static void finalizarVenta(Cliente cliente, Carrito carrito, IPago medioDePago, Ecuotas cuotas, Scanner sc) throws Exception {

        EnvolventeProductos inventario = new EnvolventeProductos();
        Factura factura = new Factura(cliente, carrito);
        double total = factura.getTotal();

        medioDePago.procesarPago(total, cuotas);

        for (Map.Entry<Producto, Integer> entry : factura.getItemsFacturados().entrySet()) {
            Producto pFacturado = entry.getKey();
            Integer cantidadVendida = entry.getValue();

            Producto pInventario = inventario.buscarProductoPorCodigo(pFacturado.getCodigo());

            if (pInventario == null) {
                throw new RuntimeException("Error: Producto " + pFacturado.getCodigo() + " no encontrado en inventario");
            }

            pInventario.restarStock(cantidadVendida);
        }

        factura.setPagada(true);
        factura.setTarjetaUtilizada((Tarjeta) medioDePago);
        if (medioDePago instanceof Credito) {
            factura.setCuotasElegidas(cuotas);
        }

        FacturaJSONManager.guardarFactura(factura);

        if(cliente == null) {
        cliente = new Cliente("Consumidor Final","consumidor@final",null,null,"11111111111");
        }
            opciones_descarga(sc, factura, cliente.getEmail());


        System.out.println("¡VENTA EXITOSA! Factura guardada.");
        carrito.vaciarCarrito();
    }

    private static void descargarFactura(Factura Factura) throws Exception {
        CreadorPDF.generarFacturaPDF(Factura);
    }

    private static void opciones_descarga(Scanner sc, Factura Factura, String mail) throws Exception {
        System.out.println("Desea descargar su factura ?");
        System.out.println("1. Descargar Factura");
        System.out.println("2. Enviar factura al mail");
        System.out.println("3. Salir");
        int opcion = sc.nextInt();
        switch (opcion) {
            case 1:
                descargarFactura(Factura);
                break;
            case 2:
                CreadorPDF.enviarFacturaPorEmail(Factura,mail );
                break;
            default:
                break;
        }


    }
}
