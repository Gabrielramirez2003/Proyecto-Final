import ENUMS.EestadosTarjetas;
import ENUMS.EtipoProducto;
import Personas.Cliente;
import Productos.Producto;

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
                                boolean controlfact=false;
                                int opcionFacturacion;
                                while(!controlfact){
                                    System.out.println("1. Agregar productos");
                                    System.out.println("2. Seleccionar cliente");
                                    System.out.println("3. Seleccionar Metodo de Pago");
                                }

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