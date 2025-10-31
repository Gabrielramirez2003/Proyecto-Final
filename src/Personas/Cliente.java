package Personas;

import org.json.JSONObject;

public class Cliente extends Persona{
    private String idCliente = "C" +super.getId();
    private String direccion;

    //CONSTRUCTORS

    public Cliente() {
        super();
    }

    public Cliente(String nombre, String email, String telefono, String direccion) {
        super(nombre, email, telefono);
        this.direccion = direccion;
    }



    //GETTERS & SETTERS


    public String getIdCliente() {
        return idCliente;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    //METODS


    @Override
    public JSONObject personaToJSONObject() {
        JSONObject o = new JSONObject();
        o.put("nombre",this.getNombre());
        o.put("email",this.getEmail());
        o.put("telefono",this.getTelefono());
        o.put("idCliente", this.getIdCliente());
        o.put("direccion", this.getDireccion());
        return o;
    }

    @Override
    public String toString() {
        return "Cliente N°: " + idCliente + '\n' +
                super.toString() +
                " direccion: " + direccion + '\n';
    }
}
