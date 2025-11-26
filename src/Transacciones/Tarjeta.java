package Transacciones;

import ENUMS.Ecuotas;
import ENUMS.EestadosTarjetas;
import ENUMS.EmarcaTarjeta;
import ENUMS.EtipoTarjeta;
import Excepciones.tarjetaInexistenteEx;
import Interfaces.IPago;
import Personas.Cliente;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.HashSet;

public class Tarjeta implements IPago {
    protected Cliente cliente;
    private String numeroTarjeta;
    private LocalDate fechaVencimiento;
    private EmarcaTarjeta marca;
    private EestadosTarjetas estado;
    private EtipoTarjeta tipo;

    //constructor
    public Tarjeta() {

    }

    public Tarjeta(JSONObject obj) {
        this.numeroTarjeta = obj.getString("numeroTarjeta");
        this.fechaVencimiento = LocalDate.parse(obj.getString("fechaVencimiento"));

    }

    public Tarjeta(String numeroTarjeta, Cliente cliente, LocalDate fechaVencimiento, EestadosTarjetas estado) {
        this.numeroTarjeta = numeroTarjeta;
        this.cliente = cliente;
        this.fechaVencimiento = fechaVencimiento;
        this.estado = estado;
        this.marca = obtenerMarca();
    }

    //getters && setters
    public String getNumeroTarjeta() {
        return numeroTarjeta;
    }

    public void setNumeroTarjeta(String numeroTarjeta) {
        this.numeroTarjeta = numeroTarjeta;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public EmarcaTarjeta getMarca() {
        return marca;
    }

    public void setMarca(EmarcaTarjeta marca) {
        this.marca = marca;
    }

    public EestadosTarjetas getEstado() {
        return estado;
    }

    public void setEstado(EestadosTarjetas estado) {
        this.estado = estado;
    }

    public void setTipo() {
        this.tipo = getTipo();
    }


    // Metodo principal para chequear vigencia

    public boolean fechaVigente() {
        return LocalDate.now().isBefore(this.fechaVencimiento) || LocalDate.now().isEqual(this.fechaVencimiento);
    }

    public EtipoTarjeta getTipo(){
        return EtipoTarjeta.DEBITO;
    }

    public void activar(){
        this.estado=EestadosTarjetas.ACTIVA;
    }

    public void bloquear(){
        this.estado=EestadosTarjetas.BLOQUEADA;
    }

    public boolean esValida(){
        if(fechaVigente() && this.estado == EestadosTarjetas.ACTIVA){
            return true;
        }else{
            return false;
        }
    }

    public String getUltimosCuatro() {
        return numeroTarjeta.substring(numeroTarjeta.length() - 4);
    }

    @Override
    public String toString() {
        return " - **** **** **** " + getUltimosCuatro() +
                "nombreTitular='" + cliente.getNombre() + '\'' +
                ", marca=" + marca +
                '}';
    }

    public JSONObject toJson(){
        JSONObject o = new JSONObject();
        o.put("numeroTarjeta", this.getNumeroTarjeta());
        o.put("fechaVencimiento",this.getFechaVencimiento());
        o.put("estado",this.getEstado());
        return o;
    }

    public EmarcaTarjeta obtenerMarca(){
        if(numeroTarjeta.startsWith("4")){
            return EmarcaTarjeta.VISA;
        } else if (numeroTarjeta.startsWith("52") || numeroTarjeta.startsWith("51") || numeroTarjeta.startsWith("53") || numeroTarjeta.startsWith("54") || numeroTarjeta.startsWith("55")) {
            return EmarcaTarjeta.MASTERCARD;
        } else if (numeroTarjeta.startsWith("34") || numeroTarjeta.startsWith("37")) {
            return EmarcaTarjeta.AMERICAN_EXPRESS;
        } else if (numeroTarjeta.startsWith("6011") || numeroTarjeta.startsWith("65")) {
            return EmarcaTarjeta.DISCOVER;
        } else if (numeroTarjeta.startsWith("5896")) {
            return EmarcaTarjeta.CABAL;
        } else if (numeroTarjeta.startsWith("2799")) {
            return EmarcaTarjeta.NARANJA;
        }else{
            return EmarcaTarjeta.DESCONOCIDA;
        }
    }

    public static HashSet<Tarjeta> JSONArrayToHashset(JSONArray a){
        HashSet<Tarjeta> hashSet = new HashSet<Tarjeta>();
        for(int i=0;i<a.length();i++){
            hashSet.add(new Tarjeta(a.getJSONObject(i)));
        }
        return hashSet;
    }

    // Implementación de IPago
    @Override
    public void procesarPago(double monto, Ecuotas cuotas) throws tarjetaInexistenteEx {
        if (!esValida()) {
            throw new tarjetaInexistenteEx("Pago rechazado: Tarjeta no valida o vencida");
        }

        System.out.println("Pago (Débito/1 cuota) aprobado por $" + monto);
    }
}
