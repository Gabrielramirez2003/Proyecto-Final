package Transacciones;

import ENUMS.Ecuotas;
import ENUMS.EestadosTarjetas;
import ENUMS.EmarcaTarjeta;
import ENUMS.EtipoTarjeta;
import Personas.Cliente;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.HashSet;

public class Credito extends Tarjeta{
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

    //getters && setters


    //metodos

    @Override
    public EtipoTarjeta getTipo() { return EtipoTarjeta.CREDITO; }

    public double pagar(Ecuotas c, double monto) {
        double valorXcuota=0;
        if(c == Ecuotas.UNA){
            valorXcuota = monto;
        }else if(c == Ecuotas.TRES){
            valorXcuota = (monto*1.1)/3;
        }else if(c == Ecuotas.SEIS){
            valorXcuota = (monto*1.2)/6 ;
        }else if(c == Ecuotas.NUEVE){
            valorXcuota = (monto*1.3)/9 ;
        }else if(c == Ecuotas.DOCE){
            valorXcuota = (monto*1.45)/12 ;
        }
        return valorXcuota;
    }

    public HashSet<Credito> JSONArrayToHashsetCredito(JSONArray a){
        HashSet<Credito> hashSet = new HashSet<Credito>();
        for(int i=0;i<a.length();i++){
            hashSet.add(new Credito(a.getJSONObject(i)));
        }
        return hashSet;
    }


}
