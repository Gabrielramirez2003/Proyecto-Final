package Personas;

import Archivos_Json.JSONUtiles;
import Archivos_Json.validacionArchivoCuentasCorrientes;
import ENUMS.EestadosTarjetas;
import Excepciones.cuentaCorrienteExistente;
import Transacciones.Credito;
import Transacciones.Tarjeta;
import Transacciones.TarjetasGenerica;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.time.LocalDate;

public class Cliente extends Persona {
    private String idCliente = "C" + super.getId();
    private String direccion;
    private String cuit;
    private TarjetasGenerica<Tarjeta> tarjetasDebito = new TarjetasGenerica<>();
    private TarjetasGenerica<Credito> tarjetasCredito = new TarjetasGenerica<>();


    //CONSTRUCTORS

    public Cliente() {
        super();
    }

    public Cliente(String nombre, String email, String telefono, String direccion, String cuit) {
        super(nombre, email, telefono);
        this.direccion = direccion;
        this.cuit = cuit;
    }

    public Cliente(JSONObject o){
        Tarjeta a = new Tarjeta();
        Credito c = new Credito();
        this.setNombre(o.getString("nombre"));
        this.setEmail(o.getString("email"));
        this.setTelefono(o.getString("telefono"));
        this.setIdCliente(o.getString("idCliente"));
        this.setDireccion(o.getString("direccion"));
        this.setCuit(o.getString("cuit"));
        this.tarjetasDebito = new TarjetasGenerica<>(a.JSONArrayToHashset(o.getJSONArray("tarjetasDebito")));
        this.tarjetasCredito = new TarjetasGenerica<>(c.JSONArrayToHashsetCredito(o.getJSONArray("tarjetasCredito")));

    }


    //GETTERS & SETTERS


    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public TarjetasGenerica<Tarjeta> getTarjetasDebito() {
        return tarjetasDebito;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCuit() {
        return cuit;
    }

    public void setCuit(String cuit) {
        this.cuit = cuit;
    }
    //METODS


    @Override
    public JSONObject personaToJSONObject() {
        JSONObject o = new JSONObject();
        o.put("nombre", this.getNombre());
        o.put("email", this.getEmail());
        o.put("telefono", this.getTelefono());
        o.put("idCliente", this.getIdCliente());
        o.put("direccion", this.getDireccion());
        o.put("cuit", this.getCuit());
        o.put("tarjetasDebito", tarjetasDebito.tarjetasToJSONArray());
        o.put("tarjetasCredito", tarjetasCredito.tarjetasToJSONArray());
        return o;
    }

    @Override
    public String toString() {
        return "Cliente N°: " + idCliente + '\n' +
                super.toString() +
                " direccion: " + direccion + '\n';
    }




    public void agregarTarjetaDebito(String numeroTarjeta, LocalDate fechaVencimiento, String cvv, EestadosTarjetas estado){
        Tarjeta d = new Tarjeta(numeroTarjeta,this,fechaVencimiento,cvv,estado);
        d.setTipo();

        tarjetasDebito.agregarTarjeta(d);


    }

    public void agregarTarjetaCredito(String numeroTarjeta, LocalDate fechaVencimiento, String cvv, EestadosTarjetas estado){
        Credito c = new Credito(numeroTarjeta,this,fechaVencimiento,cvv,estado);
        c.setTipo();

        tarjetasCredito.agregarTarjeta(c);

    }
}


