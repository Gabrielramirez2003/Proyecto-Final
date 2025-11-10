import Personas.Empleado;

public class EnvolventePrincipal {
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
}
