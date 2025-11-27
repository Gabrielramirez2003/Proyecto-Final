import ENUMS.Ecuotas;
import ENUMS.EestadosTarjetas;
import ENUMS.Eroles;
import ENUMS.EtipoProducto;
import Excepciones.*;
import Interfaces.IPago;
import Personas.Cliente;
import Personas.Empleado;
import Productos.Producto;
import Transacciones.Carrito;
import Transacciones.Credito;
import Transacciones.Tarjeta;
import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        EnvolventePrincipal ep = new EnvolventePrincipal();
        Scanner sc = new Scanner(System.in);
        boolean salirDelSistema = false;

        while (!salirDelSistema) {
            try {
                System.out.println("\n--- BIENVENIDO AL SISTEMA ---");
                System.out.println("1. Registrar Cliente (Nueva Cuenta Corriente)");
                System.out.println("2. Registrar Empleado (Nuevo Usuario)");
                System.out.println("3. Loguearse (Ingresar al sistema)");
                System.out.println("4. Salir");
                System.out.print("Ingrese una opcion: ");

                int opcionInicio = sc.nextInt();
                sc.nextLine();

                switch (opcionInicio) {
                    case 1: {
                        System.out.println("\n--- REGISTRO DE NUEVA CUENTA CORRIENTE ---");
                        try {
                            ep.crearClienteXconsola(sc);
                            System.out.println("Cliente registrado con éxito.");
                        } catch (cuentaCorrienteExistente ex) {
                            System.out.println("Error: Ya existe un cliente con ese CUIT");
                        } catch (IllegalArgumentException ex) {
                            System.out.println("Datos inválidos: " + ex.getMessage());
                        } catch (IOException ex) {
                            System.out.println("Error al guardar en archivo");
                        }
                        break;
                    }

                    case 2: {
                        boolean registro = false;
                        while (!registro) {
                            System.out.println("\n--- REGISTRO DE USUARIO EMPLEADO ---");
                            System.out.print("Nombre: ");
                            String nombre = sc.nextLine();
                            System.out.print("Email: ");
                            String email = sc.nextLine();
                            System.out.print("Telefono: ");
                            String telefono = sc.nextLine();
                            System.out.print("Contrasenia: ");
                            String contrasenia = sc.nextLine();

                            try {
                                if (ep.register(nombre, email, telefono, contrasenia)) {
                                    System.out.println("¡Usuario registrado con éxito! Ahora puede loguearse.");
                                    registro = true;
                                }
                            } catch (emailInvalidoEx ex) {
                                System.out.println("Email inválido: " + ex.getMessage());
                            } catch (contraseniaNoValidaEx ex) {
                                System.out.println("Contraseña no válida: " + ex.getMessage());
                            } catch (IOException ex) {
                                System.out.println("Error al guardar usuario");
                            } catch (Exception ex) {
                                System.out.println("Error inesperado: " + ex.getMessage());
                            }
                            if (!registro) {
                                System.out.println("Intente nuevamente");
                            }
                        }
                        break;
                    }


                    case 3: {
                        Empleado usuarioLogueado = null;
                        System.out.println("\n--- LOGIN ---");
                        System.out.print("Email: ");
                        String email = sc.nextLine();
                        System.out.print("Contrasenia: ");
                        String pass = sc.nextLine();

                        try {
                            usuarioLogueado = ep.login(email, pass);
                        } catch (ContraseniaIncorrectaException e) {
                            System.out.println("Contraseña incorrecta");
                        } catch (emailIncorrectoEx e) {
                            System.out.println("Email no registrado");
                        } catch (IOException e) {
                            System.out.println("Error al leer datos de usuarios");
                        } catch (JSONException e) {
                            System.out.println("Error en formato de datos");
                        }

                        if (usuarioLogueado != null) {
                            System.out.println("\n Bienvenido " + usuarioLogueado.getNombre() + " [" + usuarioLogueado.getRol() + "] ");

                            boolean cerrarSesion = false;
                            while (!cerrarSesion) {
                                System.out.println("\n--- MENÚ PRINCIPAL ---");
                                System.out.println("1. Registrar Cliente (Cuenta Corriente)");
                                System.out.println("2. Gestión de Inventario");
                                System.out.println("3. Cobrar (Ventas)");
                                System.out.println("4. Gestión de Personas (Admin)");
                                System.out.println("5. Cerrar Sesion");
                                System.out.print("Opción: ");

                                try {
                                    int opSesion = sc.nextInt();
                                    sc.nextLine();

                                    switch (opSesion) {
                                        case 1: {
                                            ep.crearClienteXconsola(sc);
                                            break;
                                        }
                                        case 2: {
                                            boolean volverInv = false;
                                            while (!volverInv) {
                                                System.out.println("\n--- INVENTARIO ---");
                                                System.out.println("1. Ver productos (Todos)");
                                                System.out.println("2. Ver productos por categoría");
                                                System.out.println("3. Buscar producto");
                                                if (usuarioLogueado.getRol() == Eroles.ENCARGADO) {
                                                    System.out.println("4. Modificar Stock (ENCARGADO)");
                                                    System.out.println("5. Agregar Producto (ENCARGADO)");
                                                    System.out.println("6. Eliminar Producto (ENCARGADO)");
                                                }
                                                System.out.println("0. Volver");
                                                System.out.print("Opción: ");

                                                int opInv = sc.nextInt();
                                                sc.nextLine();

                                                switch (opInv) {
                                                    case 1:
                                                        ep.verTodosProductos();
                                                        System.out.println("Enter para seguir...");
                                                        sc.nextLine();
                                                        break;
                                                    case 2:
                                                        System.out.println("1.LIMPIEZA 2.FIAMBRERIA 3.BEBIDA_S/A 4.BEBIDA_C/A 5.BAZAR 6.KIOSCO 7.COMIDA");
                                                        int t = sc.nextInt();
                                                        sc.nextLine();
                                                        ArrayList<Producto> lista = ep.verProductosXtipo(ep.seleccionarTipoProducto(t));
                                                        for (Producto p : lista) System.out.println(p);
                                                        break;
                                                    case 3:
                                                        System.out.print("Nombre del producto: ");
                                                        ep.buscarXnombre(sc);
                                                        break;
                                                    case 4:
                                                        if (usuarioLogueado.getRol() == Eroles.ENCARGADO)
                                                            ep.cambiarStock(sc);
                                                        else
                                                            System.out.println("Acceso denegado. Requiere ser ENCARGADO.");
                                                        break;
                                                    case 5:
                                                        if (usuarioLogueado.getRol() == Eroles.ENCARGADO) {
                                                            Producto p = ep.crearProductoConsola(sc);
                                                            ep.agregarNuevoProducto(p);
                                                        } else System.out.println("Acceso denegado.");
                                                        break;
                                                    case 6:
                                                        if (usuarioLogueado.getRol() == Eroles.ENCARGADO) {
                                                            System.out.print("ID a eliminar: ");
                                                            ep.eliminarXid(sc.nextLine(), "admin123");
                                                            System.out.println("Eliminado.");
                                                        } else System.out.println("Acceso denegado.");
                                                        break;
                                                    case 0:
                                                        volverInv = true;
                                                        break;
                                                    default:
                                                        System.out.println("Opción incorrecta.");
                                                }
                                            }
                                            break;
                                        }
                                        case 3: {
                                            Carrito carrito = new Carrito();
                                            while (true) {
                                                System.out.print("Codigo producto ('n' para finalizar): ");
                                                String cod = sc.nextLine();
                                                if (cod.equalsIgnoreCase("n")) break;

                                                Producto p = ep.buscarProductoPorCodigo(cod);
                                                if (p != null) {
                                                    System.out.println(p);
                                                    System.out.print("Cantidad: ");
                                                    int cant = sc.nextInt();
                                                    sc.nextLine();
                                                    try {
                                                        if (p.getCantidad() >= cant) {
                                                            carrito.agregarProducto(p, cant);
                                                            System.out.printf("Agregado. Subtotal: $%.2f%n", carrito.calcularTotal());
                                                        } else {
                                                            System.out.println("Stock insuficiente. Disponible: " + p.getCantidad());
                                                        }
                                                    } catch (IllegalArgumentException ex) {
                                                        System.out.println("Error: " + ex.getMessage());
                                                    }
                                                } else System.out.println("No existe.");
                                            }

                                            if (!carrito.getItems().isEmpty()) {
                                                System.out.println("Total: " + carrito.calcularTotal());
                                                System.out.print("CUIT Cliente (Enter para Consumidor Final): ");
                                                String cuit = sc.nextLine();
                                                Cliente clienteVenta = null;
                                                if (!cuit.isEmpty()) clienteVenta = ep.buscarClienteCuit(cuit);

                                                System.out.println("1. Debito 2. Credito");
                                                int opPago = sc.nextInt();
                                                sc.nextLine();
                                                IPago medio = null;
                                                Ecuotas cuotas = Ecuotas.UNA;

                                                if (opPago == 1)
                                                    medio = new Tarjeta("1111-2222", clienteVenta, LocalDate.now(), EestadosTarjetas.ACTIVA);
                                                else {
                                                    medio = new Credito("5555-6666", clienteVenta, LocalDate.now(), EestadosTarjetas.ACTIVA);
                                                    System.out.println("Cuotas (1,3,6,12): ");
                                                    int c = sc.nextInt();
                                                    sc.nextLine();
                                                    if (c == 3) cuotas = Ecuotas.TRES;
                                                    else if (c == 6) cuotas = Ecuotas.SEIS;
                                                    else if (c == 12) cuotas = Ecuotas.DOCE;
                                                }
                                                ep.finalizarVenta(clienteVenta, carrito, medio, cuotas,sc);


                                            }
                                            break;
                                        }
                                        case 4: {
                                            boolean volverPer = false;
                                            while (!volverPer) {
                                                System.out.println("\n--- GESTIÓN PERSONAS ---");
                                                System.out.println("1. Ver Clientes");
                                                System.out.println("2. Ver Empleados (IDs)");
                                                System.out.println("3. Eliminar Cliente (Clave)");
                                                System.out.println("4. Eliminar Empleado (Clave)");
                                                System.out.println("5. Ascender Empleado (Clave)");
                                                System.out.println("6. Descender Empleado (Clave)");
                                                System.out.println("7. Agregar Nuevo Empleado (Clave)");
                                                System.out.println("8. Volver");
                                                System.out.print("Opción: ");

                                                int opP = sc.nextInt();
                                                sc.nextLine();

                                                if (opP >= 3 && opP <= 7) {
                                                    System.out.print("Ingrese clave de seguridad (admin123): ");
                                                    String clave = sc.nextLine();
                                                    if (!clave.equals("admin123")) {
                                                        System.out.println("Clave incorrecta.");
                                                        continue;
                                                    }
                                                }

                                                switch (opP) {
                                                    case 1:
                                                        ep.verClientes();
                                                        break;
                                                    case 2:
                                                        ep.verEmpleados();
                                                        break;
                                                    case 3:
                                                        System.out.print("CUIT Cliente: ");
                                                        ep.eliminarCliente(sc.nextLine(), "admin123");
                                                        System.out.println("Cliente eliminado.");
                                                        break;
                                                    case 4:
                                                        System.out.print("ID Emp: ");
                                                        ep.eliminarEmpleado(sc.nextLine(), "admin123");
                                                        System.out.println("Empleado eliminado.");
                                                        break;
                                                    case 5:
                                                        System.out.print("ID Emp: ");
                                                        ep.empleadoAEncargado(sc.nextLine(), "admin123");
                                                        System.out.println("Ascendido.");
                                                        break;
                                                    case 6:
                                                        System.out.print("ID Emp: ");
                                                        ep.encargadoAEmpleado(sc.nextLine(), "admin123");
                                                        System.out.println("Descendido.");
                                                        break;
                                                    case 7:
                                                        System.out.println("Nuevo Empleado:");
                                                        System.out.print("Nombre: ");
                                                        String n = sc.nextLine();
                                                        System.out.print("Email: ");
                                                        String e = sc.nextLine();
                                                        System.out.print("Tel: ");
                                                        String t = sc.nextLine();
                                                        System.out.print("Pass: ");
                                                        String p = sc.nextLine();
                                                        ep.register(n, e, t, p);
                                                        System.out.println("Creado.");
                                                        break;
                                                    case 8:
                                                        volverPer = true;
                                                        break;
                                                    default:
                                                        System.out.println("Opción inválida.");
                                                }
                                            }
                                            break;
                                        }
                                        case 5:
                                            cerrarSesion = true;
                                            break;
                                        default:
                                            System.out.println("Opción inválida.");
                                    }
                                } catch (InputMismatchException ex) {
                                    System.out.println("Error: Debe ingresar un número válido");
                                    sc.nextLine();
                                } catch (ProductoNoEncontradoEx | PersonaNoEncontradaEx ex) {
                                    System.out.println("Error: " + ex.getMessage());
                                } catch (stockInsuficienteEx ex) {
                                    System.out.println("Stock insuficiente: " + ex.getMessage());
                                } catch (IOException ex) {
                                    System.out.println("Error al acceder a archivos");
                                } catch (Exception ex) {
                                    System.out.println("ERROR OPERATIVO: " + ex.getMessage());
                                }
                            }
                        }
                        break;
                    }

                    case 4: {
                        salirDelSistema = true;
                        System.out.println("Cerrando sistema...");
                        break;
                    }
                    default:
                        System.out.println("Opción inválida.");
                }

            } catch (InputMismatchException e) {
                System.out.println("Error: Debe ingresar un número.");
                sc.nextLine();
            } catch (Exception e) {
                System.out.println("ERROR CRÍTICO: " + e.getMessage());
            }
        }
        sc.close();
    }
}