package Transacciones;

import ENUMS.EestadosTarjetas;
import ENUMS.EmarcaTarjeta;
import ENUMS.EtipoTarjeta;
import Personas.Cliente;
import org.json.JSONObject;

import java.time.LocalDate;

public class Debito extends Tarjeta{

    private double saldo;


    //constructor


    public Debito() {
    }

    public Debito(String numeroTarjeta, Cliente cliente, LocalDate fechaVencimiento, String cvv, EestadosTarjetas estado, double saldo) {
        super(numeroTarjeta, cliente, fechaVencimiento, cvv, estado);
        this.saldo = saldo;
    }

    //getters && setters


    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    //metodos

    @Override
    public EtipoTarjeta getTipo() { return EtipoTarjeta.DEBITO; }

    @Override
    public void pagar(double monto) {
        if(monto > this.saldo){
            this.saldo = this.saldo - monto;
        }else{
            System.out.println("Saldo insuficiente");
        }
    }

    @Override
    public JSONObject toJson() {
        JSONObject o = new JSONObject();
        o.put("numetoTarjeta", this.getNumeroTarjeta());
        o.put("cliente", this.cliente.personaToJSONObject());
        o.put("fechaVencimiento",this.getFechaVencimiento());
        o.put("cvv",this.getCvv());
        o.put("estado",this.getEstado());
        o.put("saldo", this.getSaldo());
        o.put("marca", this.getMarca());
        return o;
    }
}
