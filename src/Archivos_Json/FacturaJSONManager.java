package Archivos_Json;

import Facturas.Factura;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.IOException;

public class FacturaJSONManager {
    private static final String ARCHIVO_FACTURAS = "facturas";

    public static void guardarFactura(Factura factura) throws IOException, JSONException {
        File file = new File(ARCHIVO_FACTURAS + ".json");
        JSONArray facturasArray;

        if (!file.exists()) {
            JSONUtiles.inicializarArchivo(ARCHIVO_FACTURAS);
            facturasArray = new JSONArray();
        } else {
            String contenido = JSONUtiles.downloadJSON(ARCHIVO_FACTURAS);
            if (contenido.isEmpty()) {
                facturasArray = new JSONArray();
            } else {
                facturasArray = new JSONArray(contenido);
            }
        }
        facturasArray.put(factura.toJSON());
        JSONUtiles.uploadJSON(facturasArray, ARCHIVO_FACTURAS);
    }
}