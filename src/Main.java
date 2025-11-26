import ENUMS.Ecuotas;
import ENUMS.EestadosTarjetas;
import Excepciones.*;
import Interfaces.IPago;
import Personas.Cliente;
import Productos.Producto;
import Transacciones.Carrito;
import Transacciones.Credito;
import Transacciones.Tarjeta;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        File usuarios = new File("usuarios.json");

        EnvolventePrincipal ep = new EnvolventePrincipal();

        boolean control = false;
        Scanner sc = new Scanner(System.in);
        int opcion;

        while (!control) {
            try {
                System.out.println("\n--- BIENVENIDO AL SISTEMA ---");
                System.out.println("1. Registrarse");
                System.out.println("2. Loguearse");
                System.out.println("3. Salir");
                System.out.print("Ingrese una opcion: ");

                if (sc.hasNextInt()) {
                    opcion = sc.nextInt();
                    sc.nextLine();
                } else {
                    System.out.println("Opcion invalida. Intente de nuevo");
                    sc.nextLine();
                    continue;
                }

                switch (opcion) {
                    case 1:
                        boolean registro = false;
                        while (!registro) {
                            System.out.println("\n--- REGISTRO ---");
                            System.out.print("Nombre: ");
                            String nombre = sc.nextLine();
                            System.out.print("Email: ");
                            String email = sc.nextLine();
                            System.out.print("Telefono: ");
                            String telefono = sc.nextLine();
                            System.out.print("Contrasenia: ");
                            String contrasenia = sc.nextLine();
                            try {
                                registro = ep.register(nombre, email, telefono, contrasenia);
                                if (registro) System.out.println("Registro exitoso");
                            } catch (emailInvalidoEx | contraseniaNoValidaEx | IOException ex) {
                                System.out.println("Error de registro: " + ex.getMessage());
                                System.out.println("Intente nuevamente");
                            }
                        }
                        break;

                    case 2:
                        boolean loguearse = false;
                        while (!loguearse) {
                            System.out.println("\n--- LOGIN ---");
                            System.out.print("Email: ");
                            String email = sc.nextLine();
                            System.out.print("Contrasenia: ");
                            String contrasenia = sc.nextLine();
                            try {
                                loguearse = ep.login(email, contrasenia);
                                if (loguearse) System.out.println("Sesion iniciada correctamente");
                            } catch (ContraseniaIncorrectaException | emailIncorrectoEx | IOException ex) {
                                System.out.println("Error de login: " + ex.getMessage());
                                System.out.println("Intente nuevamente");
                            }
                        }

                        boolean sesion = false;
                        while (!sesion) {
                            System.out.println("\n--- MENU PRINCIPAL ---");
                            System.out.println("1. Registrar cliente");
                            System.out.println("2. Ajustes de inventario");
                            System.out.println("3. Cobrar");
                            System.out.println("4. Menu de Personas y Clientes");
                            System.out.println("5. Cerrar Sesion");
                            System.out.print("Seleccione una opcion: ");

                            int opcionesSesion;
                            if (sc.hasNextInt()) {
                                opcionesSesion = sc.nextInt();
                                sc.nextLine();
                            } else {
                                System.out.println("Opcion invalida. Intente de nuevo");
                                sc.nextLine();
                                continue;
                            }

                            try {
                                switch (opcionesSesion) {
                                    case 1:
                                        ep.crearClienteXconsola(sc);
                                        System.out.println("Cliente registrado con exito");
                                        break;

                                    case 2:
                                        boolean stockControl = false;
                                        while (!stockControl) {
                                            System.out.println("\n--- INVENTARIO ---");
                                            System.out.println("1. Modificar stock de producto");
                                            System.out.println("2. Agregar producto nuevo");
                                            System.out.println("3. Ver productos por categoria");
                                            System.out.println("4. Ver todos los productos");
                                            System.out.println("5. Buscar producto");
                                            System.out.println("6. Eliminar producto");
                                            System.out.println("7. Salir");
                                            System.out.print("Ingrese una opcion: ");

                                            try {
                                                int opcionStock = sc.nextInt();
                                                sc.nextLine();

                                                switch (opcionStock) {
                                                    case 1:
                                                        ep.cambiarStock(sc);
                                                        System.out.println("Stock modificado");
                                                        break;
                                                    case 2:
                                                        Producto pNuevo = ep.crearProductoConsola(sc);
                                                        ep.agregarNuevoProducto(pNuevo);
                                                        System.out.println("Producto agregado con exito");
                                                        break;
                                                    case 3:
                                                        System.out.println("1. LIMPIEZA, 2. FIAMBRERIA, 3. BEBIDA_SIN_ALCOHOL, 4. BEBIDA_CON_ALCOHOL, 5. BAZAR, 6. KIOSCO, 7. COMIDA");
                                                        System.out.print("Ingrese el numero del tipo: ");
                                                        int t = sc.nextInt();
                                                        sc.nextLine();
                                                        ep.verProductosXtipo(ep.seleccionarTipoProducto(t));
                                                        break;
                                                    case 4:
                                                        ep.verTodosProductos();
                                                        break;
                                                    case 5:
                                                        boolean buscaControl = false;
                                                        while (!buscaControl) {
                                                            System.out.println("1. Buscar por ID | 2. Buscar por nombre");
                                                            System.out.print("Opcion: ");

                                                            int opcionBusca = sc.nextInt();
                                                            sc.nextLine();

                                                            if (opcionBusca == 1) {
                                                                ep.buscarXid(sc);
                                                                buscaControl = true;
                                                            } else if (opcionBusca == 2) {
                                                                ep.buscarXnombre(sc);
                                                                buscaControl = true;
                                                            } else {
                                                                System.out.println("Error: Seleccione una opcion valida (1 o 2)");
                                                            }
                                                        }
                                                        break;
                                                    case 6:
                                                        System.out.println("1. Eliminar por ID | 2. Eliminar por nombre");
                                                        System.out.print("Opcion: ");
                                                        int el = sc.nextInt();
                                                        sc.nextLine();
                                                        System.out.print("Ingrese clave de seguridad: ");
                                                        String cl = sc.nextLine();
                                                        if (el == 1) {
                                                            System.out.print("ID del producto: ");
                                                            ep.eliminarXid(sc.nextLine(), cl);
                                                        } else {
                                                            System.out.print("Nombre del producto: ");
                                                            ep.eliminarXNombre(sc.nextLine(), cl);
                                                        }
                                                        break;
                                                    case 7:
                                                        stockControl = true;
                                                        break;
                                                    default:
                                                        System.out.println("Opcion de inventario invalida");
                                                }
                                            } catch (InputMismatchException ex) {
                                                System.out.println("Error: Debe ingresar un numero");
                                                sc.nextLine();
                                            } catch (Exception ex) {
                                                System.out.println("ERROR en Inventario: " + ex.getMessage());
                                            }
                                        }
                                        break;

                                    case 3:
                                        System.out.println("\n--- INICIANDO VENTA ---");
                                        Carrito carrito = new Carrito();
                                        boolean agregandoProductos = true;

                                        while (agregandoProductos) {
                                            System.out.print("Ingrese el codigo del producto (o 'n' para terminar): ");
                                            String codigo = sc.nextLine();
                                            if (codigo.equalsIgnoreCase("n")) {
                                                agregandoProductos = false;
                                                break;
                                            }

                                            Producto pInventario = ep.buscarProductoPorCodigo(codigo);

                                            if (pInventario == null) {
                                                System.out.println("Error: Producto no encontrado.");
                                            } else {
                                                System.out.println("Producto: " + pInventario.getNombre() + " | Precio: $" + pInventario.getPrecio() + " | Stock: " + pInventario.getCantidad());
                                                System.out.print("Ingrese la cantidad a llevar: ");
                                                int cantidad = sc.nextInt();
                                                sc.nextLine();

                                                if (pInventario.getCantidad() < cantidad) {
                                                    throw new stockInsuficienteEx("Stock insuficiente. Stock actual: " + pInventario.getCantidad());
                                                } else {
                                                    carrito.agregarProducto(pInventario, cantidad);
                                                    System.out.println("Producto agregado. Total actual: $" + carrito.calcularTotal());
                                                }
                                            }
                                        }

                                        if (carrito.getItems().isEmpty()) {
                                            System.out.println("Venta cancelada. Carrito vacio.");
                                            break;
                                        }

                                        System.out.println("\nTotal Final: $" + carrito.calcularTotal());

                                        System.out.print("Ingrese el CUIT del cliente (o ENTER para 'Consumidor Final'): ");
                                        String cuit = sc.nextLine();
                                        Cliente cliente = null;
                                        if (!cuit.isEmpty()) {
                                            cliente = ep.buscarClienteCuit(cuit);
                                            if (cliente == null) {
                                                System.out.println("Cliente no encontrado. Se facturara como Consumidor Final.");
                                            } else {
                                                System.out.println("Cliente seleccionado: " + cliente.getNombre());
                                            }
                                        }

                                        System.out.println("Seleccione Medio de Pago: 1. Tarjeta (Debito/1 Cuota) | 2. Credito (Con cuotas)");
                                        System.out.print("Opcion: ");
                                        int opcionPago = sc.nextInt();
                                        sc.nextLine();

                                        IPago medioDePago = null;
                                        Ecuotas cuotas = Ecuotas.UNA;

                                        if (opcionPago == 1) {
                                            medioDePago = new Tarjeta("1111222233334444", cliente, LocalDate.now().plusYears(2), EestadosTarjetas.ACTIVA);
                                            System.out.println("Procesando Tarjeta (Debito/1 Cuota)...");
                                        } else if (opcionPago == 2) {
                                            medioDePago = new Credito("5555666677778888", cliente, LocalDate.now().plusYears(3), EestadosTarjetas.ACTIVA);
                                            System.out.println("Seleccione cuotas: 1. TRES, 2. SEIS, 3. NUEVE, 4. DOCE");
                                            System.out.print("Opcion: ");
                                            int opcionCuota = sc.nextInt();
                                            sc.nextLine();
                                            switch (opcionCuota) {
                                                case 1:
                                                    cuotas = Ecuotas.TRES;
                                                    break;
                                                case 2:
                                                    cuotas = Ecuotas.SEIS;
                                                    break;
                                                case 3:
                                                    cuotas = Ecuotas.NUEVE;
                                                    break;
                                                case 4:
                                                    cuotas = Ecuotas.DOCE;
                                                    break;
                                                default:
                                                    System.out.println("Advertencia: Opcion de cuota invalida. Se usara 1 cuota.");
                                                    cuotas = Ecuotas.UNA;
                                                    break;
                                            }
                                        } else {
                                            System.out.println("Opcion de pago no valida. Venta cancelada.");
                                            break;
                                        }

                                        ep.finalizarVenta(cliente, carrito, medioDePago, cuotas);
                                        System.out.println("Venta finalizada con exito.");
                                        break;

                                    case 4:
                                        boolean personaControl = false;
                                        while (!personaControl) {
                                            System.out.println("\n--- GESTION DE PERSONAS ---");
                                            System.out.println("1. Ver listado de Clientes");
                                            System.out.println("2. Eliminar Cliente");
                                            System.out.println("3. Eliminar Empleado");
                                            System.out.println("4. Ascender empleado");
                                            System.out.println("5. Descender empleado");
                                            System.out.println("6. Gestionar Tarjetas de Cliente");
                                            System.out.println("7. Volver al Menu Principal");
                                            System.out.print("Ingrese una opcion: ");

                                            try {
                                                int opcionPersona = sc.nextInt();
                                                sc.nextLine();

                                                switch (opcionPersona) {
                                                    case 1:
                                                        ep.verClientes();
                                                        break;
                                                    case 2:
                                                        System.out.print("Ingrese el CUIT del cliente a eliminar: ");
                                                        String cuitE = sc.nextLine();
                                                        System.out.print("Ingrese clave admin: ");
                                                        String claveE = sc.nextLine();
                                                        ep.eliminarCliente(cuitE, claveE);
                                                        System.out.println("Cliente eliminado.");
                                                        break;
                                                    case 3:
                                                        System.out.print("Ingrese el ID del empleado a eliminar: ");
                                                        String idEmp = sc.nextLine();
                                                        System.out.print("Ingrese clave admin: ");
                                                        String claveEmp = sc.nextLine();
                                                        ep.eliminarEmpleado(idEmp, claveEmp);
                                                        System.out.println("Empleado eliminado.");
                                                        break;
                                                    case 4:
                                                        System.out.print("Ingrese ID empleado: ");
                                                        String idAsc = sc.nextLine();
                                                        System.out.print("Ingrese clave admin: ");
                                                        String clAsc = sc.nextLine();
                                                        ep.empleadoAEncargado(idAsc, clAsc);
                                                        System.out.println("Empleado ascendido.");
                                                        break;
                                                    case 5:
                                                        System.out.print("Ingrese ID empleado: ");
                                                        String idDesc = sc.nextLine();
                                                        System.out.print("Ingrese clave admin: ");
                                                        String clDesc = sc.nextLine();
                                                        ep.encargadoAEmpleado(idDesc, clDesc);
                                                        System.out.println("Empleado descendido.");
                                                        break;
                                                    case 6:
                                                        System.out.println("Funcionalidad no implementada en consola.");
                                                        break;
                                                    case 7:
                                                        personaControl = true;
                                                        break;
                                                    default:
                                                        System.out.println("Opcion invalida.");
                                                }
                                            } catch (InputMismatchException ex) {
                                                System.out.println("Error: Debe ingresar un numero.");
                                                sc.nextLine();
                                            } catch (Exception ex) {
                                                System.out.println("ERROR en Gestion de Personas: " + ex.getMessage());
                                            }
                                        }
                                        break;

                                    case 5:
                                        sesion = true;
                                        System.out.println("Sesion cerrada.");
                                        break;

                                    default:
                                        System.out.println("Opcion de sesion no valida.");
                                }
                            } catch (InputMismatchException ex) {
                                System.out.println("Error de formato: Debe ingresar un numero.");
                                sc.nextLine();
                            } catch (Exception ex) {
                                System.out.println("ERROR en Operacion: " + ex.getMessage());
                            }
                        }
                        break;

                    case 3:
                        control = true;
                        System.out.println("Saliendo del sistema...");
                        break;

                    default:
                        System.out.println("Opcion del menu principal no valida.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Error: Debe ingresar un numero para seleccionar la opcion.");
                sc.nextLine();
            } catch (Exception e) {
                System.out.println("ERROR CRITICO DEL SISTEMA: " + e.getMessage());
                sc.nextLine();
            }
        }
        sc.close();
    }
}