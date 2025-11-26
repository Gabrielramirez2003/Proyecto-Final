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

public class Credito extends Tarjeta implements IPago {
    private Ecuotas cuotas;

    //constructor

    public Credito() {
    }

    public Credito(String numeroTarjeta, Cliente cliente, LocalDate fechaVencimiento, EestadosTarjetas estado) {
        super(numeroTarjeta, cliente, fechaVencimiento, estado);

    }

    public Credito(JSONObject obj) {
        super(obj);
    }

    //metodos

    @Override
    public EtipoTarjeta getTipo() {
        return EtipoTarjeta.CREDITO;
    }

    public double pagar(Ecuotas c, double monto) {
        double valorXcuota = 0;
        if (c == Ecuotas.UNA) {
            valorXcuota = monto;
        } else if (c == Ecuotas.TRES) {
            valorXcuota = (monto * 1.1) / 3;
        } else if (c == Ecuotas.SEIS) {
            valorXcuota = (monto * 1.2) / 6;
        } else if (c == Ecuotas.NUEVE) {
            valorXcuota = (monto * 1.3) / 9;
        } else if (c == Ecuotas.DOCE) {
            valorXcuota = (monto * 1.45) / 12;
        }
        return valorXcuota;
    }

    public HashSet<Credito> JSONArrayToHashsetCredito(JSONArray a) {
        HashSet<Credito> hashSet = new HashSet<Credito>();
        for (int i = 0; i < a.length(); i++) {
            hashSet.add(new Credito(a.getJSONObject(i)));
        }
        return hashSet;
    }

    // Implementación de IPago
    @Override
    public void procesarPago(double monto, Ecuotas cuotas) throws tarjetaInexistenteEx {
        if (!esValida()) {
            throw new tarjetaInexistenteEx("Pago rechazado: Tarjeta no valida o vencida");
        }

        double valorCuota = pagar(cuotas, monto);

        // Truncado de decimales sin String.format
        double montoCuotaFinal = (int) (valorCuota * 100);
        montoCuotaFinal = montoCuotaFinal / 100.0;

        System.out.println("Pago (Crédito) aprobado en " + cuotas + " cuotas de $" + montoCuotaFinal);
    }
}