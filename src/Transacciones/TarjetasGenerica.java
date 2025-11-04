package Transacciones;

import org.json.JSONArray;

import java.util.HashSet;

public class TarjetasGenerica <T extends Tarjeta>{
    private HashSet<T> tarjetas = new HashSet<>();


    //constructor
    public TarjetasGenerica() {
    }

   //getters & setters

    public HashSet<T> getTarjetas() {
        return tarjetas;
    }

    public void setTarjetas(HashSet<T> tarjetas) {
        this.tarjetas = tarjetas;
    }

    //metodos
    public void agregarTarjeta(T t){
        this.tarjetas.add(t);
    }

    public void eliminarTarjeta(T t){
        this.tarjetas.remove(t);
    }

    @Override
    public String toString() {
        return "TarjetasGenerica{" +
                "tarjetas=" + tarjetas +
                '}';
    }

    public JSONArray tarjetasToJSONArray(){
        JSONArray a = new JSONArray();
        for(T t: tarjetas){
            a.put(t.toJson());
        }
        return a;
    }
}
