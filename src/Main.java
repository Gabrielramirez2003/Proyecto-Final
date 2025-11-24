import ENUMS.Ecuotas;
import ENUMS.EestadosTarjetas;
import ENUMS.EtipoProducto;
import Excepciones.stockInsuficienteEx;
import Excepciones.tarjetaInexistenteEx;
import Interfaces.IPago;
import Personas.Cliente;
import Productos.Producto;
import Transacciones.Carrito;
import Transacciones.Credito;
import Transacciones.Tarjeta;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        File usuarios = new File("usuarios.json");



        EnvolventeProductos e = new EnvolventeProductos();


        EnvolventePrincipal ep = new EnvolventePrincipal();
        boolean control = false;
        Scanner sc = new Scanner(System.in);
        int opcion;
        while(!control){
            System.out.println("1. Registrarse");
            System.out.println("2. Loguearse");
            System.out.println("3. Salir");
            opcion=sc.nextInt();
            sc.nextLine();
            switch (opcion){

                case 1:

                    boolean registro = false;
                    while(!registro){
                        registro = ep.register(sc);
                    }
                    break;

                case 2:
                    boolean loguearse = false;
                    boolean sesion = false;
                    int opcionesSesion;
                    while(!loguearse){

                        loguearse = ep.login(sc);

                    }

                    while(!sesion){
                        System.out.println("1. Registrar cliente");
                        System.out.println("2. Ajustes de inventario");
                        System.out.println("3. Cobrar");
                        System.out.println("4. Eliminar Usuario");
                        System.out.println("5. Cerrar Sesion");
                        switch (opcionesSesion=sc.nextInt()){
                            case 1:
                                sc.nextLine();
                                boolean registrado = false;
                                while(!registrado){
                                    registrado = ep.crearClienteXconsola(sc);
                                }
                                break;
                            case 2:
                                boolean stockControl = false;
                                int opcionStock;
                                while(!stockControl){
                                    System.out.println("1. Modificar stock de producto");
                                    System.out.println("2. Agregar producto nuevo");
                                    System.out.println("3. Ver productos por categoria");
                                    System.out.println("4. Ver todos los productos");
                                    System.out.println("5. Buscar producto");
                                    System.out.println("6. Eliminar producto");
                                    System.out.println("7. Salir");

                                    switch (opcionStock = sc.nextInt()){
                                        case 1:
                                                ep.cambiarStock(sc);
                                            break;

                                        case 2:
                                            sc.nextLine();
                                            try{

                                                ep.agregarNuevoProducto(ep.crearProductoConsola(sc));

                                            }catch(Exception ex){
                                                System.out.println(ex.getMessage());
                                            }
                                        break;
                                        case 3:
                                            try {
                                                System.out.println("Indique el tipo de producto que desea ver");
                                                EtipoProducto tipo = ep.seleccionarTipoProducto(sc);

                                                ep.verProductosXtipo(tipo);
                                            } catch (Exception ex) {
                                                System.out.println(ex.getMessage());
                                            }
                                            break;
                                        case 4:
                                                ep.verTodosProductos();
                                                break;
                                        case 5:
                                            System.out.println("Como desea buscar el producto");
                                            System.out.println("Buscar por ID");
                                            System.out.println("Buscar por nombre");
                                            int opcionBusca = sc.nextInt();
                                            switch (opcionBusca) {
                                                case 1:

                                                    ep.buscarXid(sc);
                                                    break;

                                                case 2:

                                                    ep.buscarXnombre(sc);
                                                    break;
                                            }

                                        case 6:
                                            System.out.println("1. Eliminar por ID");
                                            System.out.println("2. Eliminar por nombre");
                                            int opcionElimina = sc.nextInt();
                                            String contraSeguridad;
                                            switch (opcionElimina) {
                                                case 1:

                                                    ep.eliminarXid(sc);
                                                    break;

                                                case 2:

                                                    ep.eliminarXNombre(sc);
                                                    break;

                                            }

                                        case 7:
                                            stockControl = true;
                                            break;
                                    }
                                }
                                break;
                            case 3:
                                sc.nextLine();
                                System.out.println("Iniciando Nueva Venta");

                                Carrito carrito = new Carrito();
                                boolean agregandoProductos = true;
                                while (agregandoProductos) {
                                    System.out.println("Ingrese el codigo del producto (o 'n' para terminar):");
                                    String codigo = sc.nextLine();
                                    if (codigo.equalsIgnoreCase("n")) {
                                        agregandoProductos = false;
                                        break;
                                    }

                                    Producto pInventario = new Producto(); //= e.buscarProductoPorCodigo(codigo);

                                    if (pInventario == null) {
                                        System.out.println("Error: Producto no encontrado");
                                    } else {
                                        System.out.println("Producto: " + pInventario.getNombre() + " Precio: $" + pInventario.getPrecio() + " Stock: " + pInventario.getCantidad());
                                        System.out.println("Ingrese la cantidad a llevar:");
                                        int cantidad = sc.nextInt();
                                        sc.nextLine();

                                        if(pInventario.getCantidad() < cantidad){
                                            System.out.println("Error: Stock insuficiente. Stock actual: " + pInventario.getCantidad());
                                        } else {
                                            carrito.agregarProducto(pInventario, cantidad);
                                            //e.modificarStock(pInventario.getCodigo(), (pInventario.getCantidad()-cantidad));
                                            System.out.println("Total actual del carrito: $" + carrito.calcularTotal());
                                        }
                                    }
                                }

                                if (carrito.getItems().isEmpty()) {
                                    System.out.println("Venta cancelada. No hay productos");
                                    break;
                                }

                                System.out.println("Venta a Finalizar. Total: $" + carrito.calcularTotal());

                                System.out.println("Ingrese el CUIT del cliente (o presione ENTER para 'Consumidor Final'):");
                                String cuit = sc.nextLine();
                                Cliente cliente = null;
                                if (!cuit.isEmpty()) {
                                    cliente = ep.buscarClienteCuit(cuit);
                                    if (cliente == null) {
                                        System.out.println("Cliente no encontrado. Se facturará como Consumidor Final");
                                    } else {
                                        System.out.println("Cliente seleccionado: " + cliente.getNombre());
                                    }
                                }

                                System.out.println("Seleccione Medio de Pago:");
                                System.out.println("1. Tarjeta (Débito / 1 Cuota)");
                                System.out.println("2. Crédito (Con cuotas)");
                                int opcionPago = sc.nextInt();
                                sc.nextLine();

                                IPago medioDePago = null;
                                Ecuotas cuotas = Ecuotas.UNA;

                                if (opcionPago == 1) {
                                    medioDePago = new Tarjeta("1111222233334444", cliente, LocalDate.now().plusYears(2), EestadosTarjetas.ACTIVA);
                                    System.out.println("Procesando Tarjeta (Débito/1 Cuota)");
                                } else {
                                    medioDePago = new Credito("5555666677778888", cliente, LocalDate.now().plusYears(3), EestadosTarjetas.ACTIVA);
                                    System.out.println("Seleccione cuotas: 1. TRES, 2. SEIS, 3. DOCE");
                                    int opcionCuota = sc.nextInt();
                                    sc.nextLine();
                                    if(opcionCuota == 1) cuotas = Ecuotas.TRES;
                                    else if (opcionCuota == 2) cuotas = Ecuotas.SEIS;
                                    else if (opcionCuota == 3) cuotas = Ecuotas.DOCE;
                                }

                                try {
                                    EnvolventeFacturacion.finalizarVenta(cliente, carrito, medioDePago, cuotas, e);

                                } catch (tarjetaInexistenteEx ex) {
                                    System.out.println("ERROR DE PAGO: " + ex.getMessage());
                                } catch (stockInsuficienteEx ex) {
                                    System.out.println("ERROR DE STOCK: " + ex.getMessage());
                                } catch (IOException ex) {
                                    System.out.println("ERROR DE ARCHIVO: No se pudo guardar la factura. " + ex.getMessage());
                                } catch (Exception ex) {
                                    System.out.println("ERROR INESPERADO: " + ex.getMessage());
                                }

                                break;

                            case 4:
                                sc.nextLine();
                                System.out.println("Ingrese el id del empleado que desea eliminar");
                                String idEmpleado = sc.nextLine();
                                System.out.println("Ingrese el codigo de seguridad");
                                String codigoSeguridad = sc.nextLine();
                                ep.eliminarEmpleado(idEmpleado,codigoSeguridad);
                                break;
                            case 5:
                                sesion = true;
                                break;
                        }
                    }
                    break;

                case 3:
                    control = true;
                    break;
            }
        }

    }
}