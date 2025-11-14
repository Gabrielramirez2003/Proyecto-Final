import Archivos_Json.JSONUtiles;
import Personas.Cliente;
import Personas.Empleado;
import org.json.JSONArray;
import org.json.JSONObject;

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
}
