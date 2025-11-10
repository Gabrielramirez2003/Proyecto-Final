import Archivos_Json.JSONUtiles;
import ENUMS.EestadosTarjetas;
import Personas.Cliente;
import Personas.Empleado;
import org.json.JSONArray;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;

import static Archivos_Json.validacionesArchivoTarjetas.tarjetaExistente;

public class EnvolventePractica {
    private HashSet<Cliente> clientes = new HashSet<>();

    public EnvolventePractica() {
    }

    public void agregarCliente(String nombre, String email, String telefono, String direccion, String cuit) {
        Empleado e1= new Empleado();
        e1.registrarCliente(nombre,email,telefono,direccion,cuit);

    }

    public void agregarTarjetaCliente(String cuit, String tipoTarjeta,String numeroTarjeta, LocalDate fechaVencimiento, String cvv, EestadosTarjetas estado){
        JSONArray a = new JSONArray(JSONUtiles.downloadJSON("cuentasCorrientes"));

        if(tarjetaExistente(numeroTarjeta)) {
            this.clientes.clear();
            for (int i = 0; i < a.length(); i++) {
                Cliente c = new Cliente(a.getJSONObject(i));
                this.clientes.add(c);
            }

            if (tipoTarjeta.equals("Debito")) {
                for (Cliente c : this.clientes) {
                    if (c.getCuit().equals(cuit)) {
                        c.agregarTarjetaDebito(numeroTarjeta, fechaVencimiento, cvv, estado);
                        break;
                    }
                }
            } else {
                for (Cliente c : this.clientes) {
                    if (c.getCuit().equals(cuit)) {
                        c.agregarTarjetaCredito(numeroTarjeta, fechaVencimiento, cvv, estado);
                        break;

                    }
                }
            }

            // Volver a convertir la lista de clientes a un JSONArray actualizado
            JSONArray nuevoArray = new JSONArray();
            for (Cliente c : clientes) {
                nuevoArray.put(c.personaToJSONObject());
            }

            // Guardar el nuevo JSON en el archivo
            JSONUtiles.uploadJSON(nuevoArray, "cuentasCorrientes");

        }


    }


}
