package Transacciones;

import ENUMS.EestadosTarjetas;
import ENUMS.EmarcaTarjeta;
import ENUMS.EtipoTarjeta;
import Personas.Cliente;
import org.json.JSONObject;

import java.time.LocalDate;

public abstract class Tarjeta {
    protected Cliente cliente;
    private String numeroTarjeta;
    private LocalDate fechaVencimiento;
    private String cvv;
    private EmarcaTarjeta marca;
    private EestadosTarjetas estado;
    private EtipoTarjeta tipo;

    //constructor
    public Tarjeta() {
    }



    public Tarjeta(String numeroTarjeta, Cliente cliente, LocalDate fechaVencimiento, String cvv, EestadosTarjetas estado) {
        this.numeroTarjeta = numeroTarjeta;
        this.cliente = cliente;
        this.fechaVencimiento = fechaVencimiento;
        this.cvv = cvv;
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

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
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

    //metodos

    public abstract EtipoTarjeta getTipo();

    public void activar(){
        this.estado=EestadosTarjetas.ACTIVA;
    }

    public void bloquear(){
        this.estado=EestadosTarjetas.BLOQUEADA;
    }

    public boolean vencida(){
        boolean a = false;
        if(LocalDate.now().isBefore(this.fechaVencimiento)){
            a = true;
        }
        return a;
    }

    public boolean esValida(){
        if(vencida() && this.estado == EestadosTarjetas.ACTIVA){
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

    public abstract JSONObject toJson();


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

    public abstract void pagar(double monto);

}
