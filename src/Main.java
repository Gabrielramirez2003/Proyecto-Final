import ENUMS.EestadosTarjetas;
import Personas.Cliente;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        File usuarios = new File("usuarios.json");

        EnvolventePrincipal ep = new EnvolventePrincipal();
        boolean control = false;
        Scanner sc = new Scanner(System.in);
        int opcion;
        while(!control){
            System.out.println("1. Registrarse");
            System.out.println("2. Loguearse");
            opcion=sc.nextInt();
            switch (opcion){

                case 1:
                    boolean registro = false;
                    while(!registro){
                        System.out.println("Ingrese el nombre del usuario");
                        String nombre = sc.nextLine();
                        System.out.println("Ingrese el email del usuario");
                        String email = sc.nextLine();
                        System.out.println("Ingrese el telefono del usuario");
                        String telefono = sc.nextLine();
                        System.out.println("Ingrese la contrasenia del usuario");
                        String contrasenia = sc.nextLine();
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
                                boolean registrado = false;
                                while(!registrado){
                                    System.out.println("Ingrese la razon social");
                                    String nombre = sc.next();
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
                            case 3:
                            case 4:
                        }
                    }
                    break;

            }
        }

    }
}