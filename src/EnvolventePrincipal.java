import Archivos_Json.JSONUtiles;
import ENUMS.EtipoProducto;
import Excepciones.CodigoExistenteEx;
import Excepciones.NombreExistenteEx;
import Excepciones.opcionInvalidaEx;
import Personas.Cliente;
import Personas.Empleado;
import Productos.Producto;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

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

    public static void agregarNuevoProducto(Producto producto) throws CodigoExistenteEx, NombreExistenteEx {

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
}
