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

        try{
            Producto p1 = new Producto("11","coca",1500,1,EtipoProducto.BEBIDA_SIN_ALCOHOL);
            Producto p2 = new Producto("1","cocacola",1500,1,EtipoProducto.BEBIDA_SIN_ALCOHOL);
            Producto p3 = new Producto("2","Copon Aconcagua Rosa",2000,1,EtipoProducto.BEBIDA_CON_ALCOHOL);
            Producto p4 = new Producto("12","Copon Aconcagua Rosa",6000,13,EtipoProducto.BAZAR);
            e.agregarProducto(p1);

            e.agregarProducto(p2);
            e.agregarProducto(p3);

            e.agregarProducto(p4);

            e.imprimirProductos();
        }catch(Exception ex) {
            ex.printStackTrace();
        }





        EnvolventePrincipal ep = new EnvolventePrincipal();
        boolean control = false;
        Scanner sc = new Scanner(System.in);
        int opcion;
        while(!control){
            System.out.println("1. Registrarse");
            System.out.println("2. Loguearse");
            System.out.println("3. Salir");
            opcion=sc.nextInt();
            switch (opcion){

                case 1:
                    boolean registro = false;
                    while(!registro){
                        System.out.println("Ingrese el nombre del usuario");
                        String nombre = sc.next();
                        System.out.println("Ingrese el email del usuario");
                        String email = sc.next();
                        System.out.println("Ingrese el telefono del usuario");
                        String telefono = sc.next();
                        System.out.println("Ingrese la contrasenia del usuario");
                        String contrasenia = sc.next();
                        registro = ep.register(nombre,email,telefono,contrasenia);
                    }
                    break;

                case 2:
                    boolean loguearse = false;
                    boolean sesion = false;
                    int opcionesSesion;
                    while(!loguearse){
                        System.out.println("Ingrese el email");
                        String email = sc.next();
                        System.out.println("Ingrese la contrasenia");
                        String contrasenia = sc.next();
                        loguearse = ep.login(email,contrasenia);
                    }



                    while(!sesion){
                        System.out.println("1. Registrar cliente");
                        System.out.println("2. Ajustes de inventario");
                        System.out.println("3. Cobrar");
                        System.out.println("4. Cerrar Sesion");
                        switch (opcionesSesion=sc.nextInt()){
                            case 1:
                                sc.nextLine();
                                boolean registrado = false;
                                while(!registrado){
                                    System.out.println("Ingrese la razon social");
                                    String nombre = sc.nextLine();
                                    System.out.println("Ingrese el email");
                                    String email = sc.next();
                                    System.out.println("Ingrese el telefono");
                                    String telefono = sc.next();
                                    System.out.println("Ingrese la direccion");
                                    String direccion = sc.next();
                                    System.out.println("Ingrese el cuit");
                                    String cuit = sc.next();
                                    registrado = ep.registrarCliente(nombre,email,telefono,direccion,cuit);
                                }
                                break;
                            case 2:
                                sc.nextLine();
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

                                    Producto pInventario = e.buscarProductoPorCodigo(codigo);

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