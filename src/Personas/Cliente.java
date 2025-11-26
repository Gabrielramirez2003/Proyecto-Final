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
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashSet;

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
        this.idCliente = "C" + super.getId().substring(0, 8); // Tomamos los primeros 8 caracteres del UUID
        this.direccion = direccion;
        this.cuit = cuit;
    }


    public Cliente(JSONObject o) {

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

    public boolean registrarCliente(String nombre, String email, String direccion, String cuit) throws IOException, cuentaCorrienteExistente {


        File file = new File("cuentasCorrientes.json");
        JSONArray a = new JSONArray();
        JSONArray b;
        Cliente c = new Cliente(nombre, email, telefono, direccion, cuit);


        if (validacionArchivoCuentasCorrientes.cuentaExistente(cuit)) {

            throw new cuentaCorrienteExistente("No se pudo registrar el usuario porque ya existe alguien con ese cuit");

        }


        if (!file.exists()) {

            JSONUtiles.inicializarArchivo("cuentasCorrientes");

            a.put(c.personaToJSONObject());

            JSONUtiles.uploadJSON(a, "cuentasCorrientes");

            System.out.println("Cliente registrado con exito");

            return true;

        } else {

            b = new JSONArray(JSONUtiles.downloadJSON("cuentasCorrientes"));

            b.put(c.personaToJSONObject());

            JSONUtiles.uploadJSON(b, "cuentasCorrientes");

            System.out.println("Cliente registrado con exito");

            return true;

        }

    }


//GETTERS & SETTERS


    public void setIdCliente(String idCliente) {

        this.idCliente = idCliente;

    }


    public HashSet<Tarjeta> getTarjetasDebito() {
        return new HashSet<>(tarjetasDebito.getTarjetas());
    }

    public HashSet<Credito> getTarjetasCredito() {

        return new HashSet<>(tarjetasCredito.getTarjetas());

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
        return String.format(
                "| ID: %-5s | CUIT: %-15s | %-30s | Email: %-25s | Teléfono: %-15s | Dirección: %s",
                idCliente,
                cuit,
                nombre,
                email,
                telefono,
                direccion);
    }


    public void agregarTarjetaDebito(String numeroTarjeta, LocalDate fechaVencimiento, EestadosTarjetas estado) {

        Tarjeta d = new Tarjeta(numeroTarjeta, this, fechaVencimiento, estado);

        d.setTipo();


        tarjetasDebito.agregarTarjeta(d);


    }


    public void agregarTarjetaCredito(String numeroTarjeta, LocalDate fechaVencimiento, EestadosTarjetas estado) {

        Credito c = new Credito(numeroTarjeta, this, fechaVencimiento, estado);

        c.setTipo();


        tarjetasCredito.agregarTarjeta(c);


    }

}