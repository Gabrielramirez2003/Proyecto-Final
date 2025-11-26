import Archivos_Json.JSONUtiles;
import ENUMS.Ecuotas;
import ENUMS.EtipoProducto;
import Excepciones.*;
import Interfaces.IPago;
import Personas.Cliente;
import Personas.Empleado;
import Productos.Producto;
import Transacciones.Carrito;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class EnvolventePrincipal {
    EnvolventeProductos ep = new EnvolventeProductos();
    EnvolventePersona epp = new EnvolventePersona();
    private EnvolventeFacturacion ef;

    public EnvolventePrincipal() {
        this.ef = new EnvolventeFacturacion(this.ep);
    }

    public boolean register(String nombre, String email, String telefono, String contrasenia) throws IOException, emailInvalidoEx, contraseniaNoValidaEx {
        Empleado e = new Empleado(nombre, email, telefono, contrasenia);
        return e.register(nombre, email, telefono, contrasenia);
    }

    public boolean login(String email, String contrasenia) throws IOException, ContraseniaIncorrectaException, emailIncorrectoEx {
        Empleado e = new Empleado();
        return e.loggin(email, contrasenia);
    }

    public boolean registrarCliente(String nombre, String email, String telefono, String direccion, String cuit) throws IOException, cuentaCorrienteExistente {
        Empleado e = new Empleado();
        return e.registrarCliente(nombre, email, telefono, direccion, cuit);
    }

    public Cliente buscarClienteCuit(String cuit) throws IOException, JSONException {
        JSONArray a = new JSONArray(JSONUtiles.downloadJSON("cuentasCorrientes"));
        Cliente c = null;
        for (int i = 0; i < a.length(); i++) {
            JSONObject obj = a.getJSONObject(i); // CORREGIDO
            if (obj.getString("cuit").equalsIgnoreCase(cuit)) {
                c = new Cliente(obj);
            }
        }
        return c;
    }

    public Cliente buscarClienteNombre(String nombre) throws IOException, JSONException {
        JSONArray a = new JSONArray(JSONUtiles.downloadJSON("cuentasCorrientes"));
        Cliente c = null;
        for (int i = 0; i < a.length(); i++) {
            JSONObject obj = a.getJSONObject(i);
            if (obj.getString("nombre").equalsIgnoreCase(nombre)) {
                c = new Cliente(obj);
            }
        }
        return c;
    }

    public void agregarNuevoProducto(Producto p) throws CodigoExistenteEx, NombreExistenteEx, IOException, JSONException {
        ep.agregarProducto(p);
    }

    public ArrayList<Producto> verProductosXtipo(EtipoProducto tipo) throws IOException, JSONException, PrecioInvalidoEx, CampoNuloEx {
        return ep.guardarProductoXtipo(tipo);
    }

    public void verTodosProductos() throws IOException, JSONException, PrecioInvalidoEx, CampoNuloEx {
        System.out.println("--- LISTADO COMPLETO DE PRODUCTOS ---");
        for (EtipoProducto tipo : EtipoProducto.values()) {
            System.out.println("\n*** Categoría: " + tipo.name().replace("_", " ") + " ***");

            ArrayList<Producto> lista = verProductosXtipo(tipo);

            if (lista.isEmpty()) {
                System.out.println("No hay productos en esta categoría.");
            } else {
                for (Producto p : lista) {
                    System.out.println(p.toString());
                }
            }
        }
        System.out.println("------------------------------------");
    }

    public EtipoProducto seleccionarTipoProducto(int opcionTipo) throws opcionInvalidaEx {
        EtipoProducto tipo;
        if (opcionTipo == 1) tipo = EtipoProducto.LIMPIEZA;
        else if (opcionTipo == 2) tipo = EtipoProducto.FIAMBRERIA;
        else if (opcionTipo == 3) tipo = EtipoProducto.BEBIDA_SIN_ALCOHOL;
        else if (opcionTipo == 4) tipo = EtipoProducto.BEBIDA_CON_ALCOHOL;
        else if (opcionTipo == 5) tipo = EtipoProducto.BAZAR;
        else if (opcionTipo == 6) tipo = EtipoProducto.KIOSCO;
        else if (opcionTipo == 7) tipo = EtipoProducto.COMIDA;
        else throw new opcionInvalidaEx("Opcion no valida");
        return tipo;
    }

    public void buscarXid(Scanner sc) throws ProductoNoEncontradoEx, IOException, JSONException, PrecioInvalidoEx, CampoNuloEx {
        System.out.println("Ingrese el ID del producto que desea buscar");
        String id = sc.next();
        ep.buscarXid(id);

        Producto productoEncontrado = buscarProductoPorCodigo(id);

        if (productoEncontrado != null) {
            System.out.println("\n Producto Encontrado:");
            System.out.println(productoEncontrado.toString());
        } else {
            throw new ProductoNoEncontradoEx("El producto con ID " + id + " no fue encontrado.");
        }
    }

    public Producto buscarProductoPorCodigo(String codigo) throws ProductoNoEncontradoEx, IOException, JSONException, PrecioInvalidoEx, CampoNuloEx {
        return ep.buscarProductoPorCodigo(codigo);
    }

    public void buscarXnombre(Scanner sc) throws ProductoNoEncontradoEx, IOException, JSONException {
        System.out.println("Ingrese el nombre del producto que desea buscar");
        String nombre = sc.nextLine();

        Producto productoEncontrado = this.ep.buscarProductoPorNombre(nombre);

        if (productoEncontrado != null) {
            System.out.println("\n Producto Encontrado:");
            System.out.println(productoEncontrado.toString());
        } else {
            throw new ProductoNoEncontradoEx("El producto con nombre '" + nombre + "' no fue encontrado.");
        }
    }


    private boolean confirmarEliminacionSeguridad(String claveIngresada) throws codigoDeSeguridadIncorrectoEx {
        final String CLAVE_MAESTRA = "admin123";
        if (CLAVE_MAESTRA.equalsIgnoreCase(claveIngresada)) {
            return true;
        } else {
            throw new codigoDeSeguridadIncorrectoEx("El codigo de seguridad es incorrecto");
        }
    }

    public void eliminarXid(String id, String clave) throws codigoDeSeguridadIncorrectoEx, ProductoNoEncontradoEx, IOException, JSONException, CampoNuloEx {
        confirmarEliminacionSeguridad(clave);
        ep.eliminarProductoPorId(id);
    }

    public void eliminarXNombre(String nombre, String clave) throws codigoDeSeguridadIncorrectoEx, ProductoNoEncontradoEx, IOException, JSONException {
        confirmarEliminacionSeguridad(clave);
        ep.eliminarProductoPorNombre(nombre);
    }

    public Producto crearProductoConsola(Scanner sc) throws CampoNuloEx, PrecioInvalidoEx, InputMismatchException {
        System.out.println("Ingrese el nombre del producto");
        String nombre = sc.nextLine();
        System.out.println("Ingrese el codigo del producto");
        String codigo = sc.next();
        System.out.println("Ingrese el stock inicial del producto");
        int stock = sc.nextInt();
        System.out.println("Ingrese el precio del producto");
        double precio = sc.nextDouble();
        sc.nextLine();
        System.out.println("Escoja el area del producto creado:");
        System.out.println("1. LIMPIEZA, 2. FIAMBRERIA, 3. BEBIDA_SIN_ALCOHOL, 4. BEBIDA_CON_ALCOHOL, 5. BAZAR, 6. KIOSCO, 7. COMIDA");
        System.out.println("Ingrese el número de la opción:");
        int opcionTipo = sc.nextInt();
        sc.nextLine();
        EtipoProducto tipo = seleccionarTipoProducto(opcionTipo);
        return new Producto(codigo, nombre, precio, stock, tipo);
    }

    public void cambiarStock(Scanner sc) throws ProductoNoEncontradoEx, IOException, JSONException, opcionInvalidaEx, InputMismatchException {
        System.out.println("Ingrese el codigo del producto que desea modificar: ");
        String codigo = sc.next();
        System.out.println("Ingrese el stock del producto que desea modificar: ");
        int stock = sc.nextInt();
        ep.modificarStock(codigo, stock);
    }

    public void modificarStock(String codigo, int stock) throws ProductoNoEncontradoEx, IOException, JSONException, opcionInvalidaEx {
        ep.modificarStock(codigo, stock);
    }

    public boolean crearClienteXconsola(Scanner sc) throws IOException, cuentaCorrienteExistente, InputMismatchException {
        System.out.println("Ingrese la razon social");
        String nombre = sc.nextLine();
        System.out.println("Ingrese el email");
        String email = sc.nextLine();
        System.out.println("Ingrese el telefono");
        String telefono = sc.nextLine();
        System.out.println("Ingrese la direccion");
        String direccion = sc.nextLine();
        System.out.println("Ingrese el cuit");
        String cuit = sc.nextLine();
        return registrarCliente(nombre, email, telefono, direccion, cuit);
    }

    // MÉTODOS PUENTE (WRAPPERS) - Clave para el encapsulamiento

    public void eliminarEmpleado(String id, String clave_ingresada) throws codigoDeSeguridadIncorrectoEx, PersonaNoEncontradaEx, IOException, JSONException {
        confirmarEliminacionSeguridad(clave_ingresada);
        epp.eliminarEmpleado(id);
    }

    public void eliminarCliente(String id, String clave_ingresada) throws codigoDeSeguridadIncorrectoEx, PersonaNoEncontradaEx, IOException, JSONException {
        confirmarEliminacionSeguridad(clave_ingresada);
        epp.eliminarCliente(id);
    }

    public void empleadoAEncargado(String id_empleado, String clave_ingresada) throws codigoDeSeguridadIncorrectoEx, PersonaNoEncontradaEx, RolMalAsignadoEx, IOException, JSONException, IllegalArgumentException {
        confirmarEliminacionSeguridad(clave_ingresada);
        epp.empleadoAEncargado(id_empleado);
    }

    public void encargadoAEmpleado(String id_empleado, String clave_ingresada) throws codigoDeSeguridadIncorrectoEx, PersonaNoEncontradaEx, RolMalAsignadoEx, IOException, JSONException, IllegalArgumentException {
        confirmarEliminacionSeguridad(clave_ingresada);
        epp.encargadoAEmpleado(id_empleado);
    }

    public void finalizarVenta(Cliente cliente, Carrito carrito, IPago medioDePago, Ecuotas cuotas) throws tarjetaInexistenteEx, stockInsuficienteEx, IOException, ProductoNoEncontradoEx, JSONException {
        ef.finalizarVenta(cliente, carrito, medioDePago, cuotas);
    }

    public void verClientes() throws IOException, JSONException {

        ArrayList<Cliente> listaClientes = epp.verClientes("cuentasCorrientes");

        System.out.println("\n==========================================================================================================================");
        System.out.println("                                               LISTADO DE CLIENTES (CUENTAS CORRIENTES)");
        System.out.println("==========================================================================================================================");

        if (listaClientes.isEmpty()) {
            System.out.println(" No hay clientes registrados.");
        } else {
            for (Cliente c : listaClientes) {
                System.out.println(c.toString());
            }
        }

        System.out.println("==========================================================================================================================");
    }


}