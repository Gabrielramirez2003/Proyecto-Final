package Transacciones;

import ENUMS.EestadosTarjetas;
import ENUMS.EmarcaTarjeta;
import ENUMS.EtipoTarjeta;
import Personas.Cliente;
import org.json.JSONObject;

import java.time.LocalDate;

public class Credito extends Tarjeta{
    private double limite;
    private double consumido;

    //constructor

    public Credito() {
    }

    public Credito(String numeroTarjeta, Cliente cliente, LocalDate fechaVencimiento, String cvv, EestadosTarjetas estado, double limite) {
        super(numeroTarjeta, cliente, fechaVencimiento, cvv, estado);
        this.limite = limite;

    }

    //getters && setters

    public double getLimite() {
        return limite;
    }

    public void setLimite(double limite) {
        this.limite = limite;
    }

    public double getConsumido() {
        return consumido;
    }

    public void setConsumido(double consumido) {
        this.consumido = consumido;
    }

    //metodos

    @Override
    public EtipoTarjeta getTipo() { return EtipoTarjeta.CREDITO; }

    @Override
    public void pagar(double monto) {
        if(this.limite<monto){
            if(this.consumido+monto < this.limite){
                this.consumido = this.consumido+monto;
                System.out.println("Transaccion completa");
            }else {
                System.out.println("Transaccion no valida. Este gasto supera el limite de su tarjeta");
            }
        }else{
            System.out.println("El monto es mayor al limite de su tarjeta");
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
        o.put("limite",this.limite);
        o.put("consumido",this.limite);
        return o;
    }
}
