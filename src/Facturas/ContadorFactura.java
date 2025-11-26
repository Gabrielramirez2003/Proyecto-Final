package Facturas;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class ContadorFactura {
    private static final String ARCHIVO = "facturas/contador.txt";

    public static int cargarContador() {
        try {
            File directory = new File("facturas");
            if (!directory.exists()) {
                directory.mkdirs(); // crea carpeta siempre
            }

            File file = new File("facturas/contador.txt");
            if (!file.exists()) {
                // si no existe, lo creo con valor 1
                FileWriter fw = new FileWriter(file);
                fw.write("1");
                fw.close();
                return 1;
            }

            BufferedReader br = new BufferedReader(new FileReader(file));
            int valor = Integer.parseInt(br.readLine());
            br.close();
            return valor;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return 1;
        }
    }

    public static void guardarContador(int valor) {
        try {
            File directory = new File("facturas");
            if (!directory.exists()) directory.mkdirs();

            FileWriter fw = new FileWriter(ARCHIVO);
            fw.write(String.valueOf(valor));
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
