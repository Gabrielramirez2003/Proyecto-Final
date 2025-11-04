package Personas;

import Archivos_Json.JSONUtiles;
import Archivos_Json.validacionArchivoCuentasCorrientes;
import ENUMS.EestadosTarjetas;
import Excepciones.cuentaCorrienteExistente;
import Transacciones.Credito;
import Transacciones.Debito;
import Transacciones.TarjetasGenerica;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.time.LocalDate;
import java.util.HashSet;

public class Cliente extends Persona {
    private String idCliente = "C" + super.getId();
    private String direccion;
    private String cuit;
    private TarjetasGenerica<Debito> tarjetasDebito = new TarjetasGenerica<>();
    private TarjetasGenerica<Credito> tarjetasCredito;


    //CONSTRUCTORS

    public Cliente() {
        super();
    }

    public Cliente(String nombre, String email, String telefono, String direccion, String cuit) {
        super(nombre, email, telefono);
        this.direccion = direccion;
        this.cuit = cuit;
    }


    //GETTERS & SETTERS


    public TarjetasGenerica<Debito> getTarjetasDebito() {
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
        return o;
    }

    @Override
    public String toString() {
        return "Cliente N°: " + idCliente + '\n' +
                super.toString() +
                " direccion: " + direccion + '\n';
    }


    public boolean registrarCliente(String nombre, String email, String telefono, String direccion, String cuit) {
        try {
            File file = new File("cuentasCorrientes.json");
            JSONArray a = new JSONArray();
            JSONArray b;
            Cliente c = new Cliente(nombre, email, telefono, direccion, cuit);
            if (!file.exists()) {
                JSONUtiles.inicializarArchivo("cuentasCorrientes");
                a.put(c.personaToJSONObject());
                JSONUtiles.uploadJSON(a, "cuentasCorrientes");
                System.out.println("Cliente registrado con exito");
                return true;
            } else {
                b = new JSONArray(JSONUtiles.downloadJSON("cuentasCorrientes"));
                b.put(c.personaToJSONObject());
                if (validacionArchivoCuentasCorrientes.cuentaExistente(cuit)) {
                    throw new cuentaCorrienteExistente("No se pudo registrar el usuario porque ya existe alguien con ese cuit");
                } else {
                    JSONUtiles.uploadJSON(b, "cuentasCorrientes");
                    System.out.println("Cliente registrado con exito");
                    return true;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void agregarTarjetaDebito(String numeroTarjeta, LocalDate fechaVencimiento, String cvv, EestadosTarjetas estado, double saldo){
        Debito d = new Debito(numeroTarjeta,this,fechaVencimiento,cvv,estado,saldo);
        d.setTipo();

        tarjetasDebito.agregarTarjeta(d);
        JSONUtiles.uploadJSON(tarjetasDebito.tarjetasToJSONArray(),"tarjetasDebito");

    }

    public void agregarTarjetaCredito(String numeroTarjeta, Cliente cliente, LocalDate fechaVencimiento, String cvv, EestadosTarjetas estado, double limite){
        Credito c = new Credito(numeroTarjeta,this,fechaVencimiento,cvv,estado,limite);
        c.setTipo();

        tarjetasDebito.agregarTarjeta(d);
        ;       JSONUtiles.uploadJSON(tarjetasDebito.tarjetasToJSONArray(),"tarjetasDebito");

    }
}


