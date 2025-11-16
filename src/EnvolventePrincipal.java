import Archivos_Json.JSONUtiles;
import ENUMS.EtipoProducto;
import Excepciones.*;
import Personas.Cliente;
import Personas.Empleado;
import Productos.Producto;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Scanner;

public class EnvolventePrincipal {
    EnvolventeProductos ep = new EnvolventeProductos();
    public EnvolventePrincipal() {
    }

    public static boolean register(String nombre, String email, String telefono, String contrasenia){
        Empleado e = new Empleado();
        return e.register(nombre, email, telefono, contrasenia);

    }

    public static boolean login(String email, String contrasenia){
        Empleado e = new Empleado();
        return e.loggin(email, contrasenia);
    }


    public static boolean registrarCliente(String nombre, String email, String telefono, String direccion, String cuit){
        Empleado e = new Empleado();
        return e.registrarCliente(nombre, email, telefono, direccion, cuit);
    }



    public static Cliente buscarClienteCuit(String cuit){

        JSONArray a = new JSONArray(JSONUtiles.downloadJSON("cuentasCorrientes"));
        Cliente c =null;
        for(int i = 0; i < a.length(); i++){
            JSONObject obj = new JSONObject(a);
            if(obj.getString("cuit").equals(cuit)){
                c = new Cliente(obj);
            }
        }
        return c;
    }

    public void agregarNuevoProducto(Producto p) throws CodigoExistenteEx, NombreExistenteEx {
        ep.agregarProducto(p);
    }

    public void verProductosXtipo(EtipoProducto tipo){
        System.out.println("\n"+"Categoria " + tipo.toString()+": \n");
        ArrayList<Producto> lista = ep.guardarProductoXtipo(tipo);
        if(!lista.isEmpty()) {
            for (Producto p : lista) {
                System.out.println(p);
            }
        }else{
            System.out.println("No existen productos de esta categoria");
        }
    }

    public void verTodosProductos(){
        for(EtipoProducto tipo : EtipoProducto.values()){
            verProductosXtipo(tipo);
        }
    }

    public EtipoProducto seleccionarTipoProducto(int opcionTipo){
        EtipoProducto tipo;
        if (opcionTipo == 1){
            tipo = EtipoProducto.LIMPIEZA;
        }else if (opcionTipo == 2){
            tipo = EtipoProducto.FIAMBRERIA;
        }else if (opcionTipo == 3){
            tipo = EtipoProducto.BEBIDA_SIN_ALCOHOL;
        } else if (opcionTipo == 4) {
            tipo = EtipoProducto.BEBIDA_CON_ALCOHOL;
        }else if (opcionTipo == 5){
            tipo = EtipoProducto.BAZAR;
        }else if (opcionTipo == 6){
            tipo = EtipoProducto.KIOSCO;
        }else if (opcionTipo == 7){
            tipo = EtipoProducto.COMIDA;
        }else{
            throw new opcionInvalidaEx("Opcion no valida");
        }
        return tipo;
    }


    public void buscarXid(String id){
        try {
            ep.buscarXid(id);
        }catch (Exception e){
            System.out.println("Error al buscar el producto: Ese id no fue encontrado");
        }
    }

    public void buscarXnombre(String nombre){
        try {
            ep.buscarXnombre(nombre);
        }catch (Exception e){
            System.out.println("Error al buscar el producto: Ese nombre no fue encontrado");
        }
    }

    private boolean confirmarEliminacionSeguridad(String claveIngresada) {
        final String CLAVE_MAESTRA = "admin123";
        if (CLAVE_MAESTRA.equals(claveIngresada)){
            return true;
        }else{
            throw new codigoDeSeguridadIncorrectoEx("El codigo de seguridad es incorrecto");
        }
    }


    public void eliminarXid(String id, String clave){
        try {
            if (confirmarEliminacionSeguridad(clave)) {
                ep.eliminarProductoPorId(id);
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void eliminarXNombre(String nombre, String clave){
        try {
            if (confirmarEliminacionSeguridad(clave)) {
                ep.eliminarProductoPorNombre(nombre);
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public Producto crearProductoConsola(Scanner sc) throws CampoNuloEx, PrecioInvalidoEx {
        System.out.println("Ingrese el nombre del producto");
        String nombre = sc.nextLine();
        System.out.println("Ingrese el codigo del producto");
        String codigo = sc.next();
        System.out.println("Ingrese el stock inicial del producto");
        int stock = sc.nextInt();
        System.out.println("Ingrese el precio del producto");
        double precio = sc.nextDouble();
        EtipoProducto tipo;
        int opcionTipo;
        System.out.println("Escoja el area del producto creado:");
        System.out.println("1.   LIMPIEZA,\n" +
                " 2.   FIAMBRERIA,\n" +
                " 3.   BEBIDA_SIN_ALCOHOL,\n" +
                " 4.   BEBIDA_CON_ALCOHOL,\n" +
                " 5.   BAZAR,\n" +
                " 6.   KIOSCO,\n" +
                " 7.   COMIDA");
        opcionTipo = sc.nextInt();
        tipo = seleccionarTipoProducto(opcionTipo);
        return new Producto(codigo,nombre,precio,stock,tipo);
    }


    public void cambiarStock(Scanner sc) {
        System.out.println("Ingrese el codigo del producto que desea modificar: ");
        String codigo = sc.next();
        System.out.println("Ingrese el stock del producto que desea modificar: ");
        int stock = sc.nextInt();
        ep.modificarStock(codigo,stock);
    }

}

