package Archivos_Json;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;

public class JSONUtiles {
    public static void uploadJSON(JSONArray jsonArray, String archive){
        try{
            BufferedWriter salida = new BufferedWriter(new FileWriter(archive+".json"));
            salida.write(jsonArray.toString());
            salida.flush();
            salida.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public static void uploadJSON(JSONObject jsonObject, String archive){
        try{
            BufferedWriter salida = new BufferedWriter(new FileWriter(archive+".json"));
            salida.write(jsonObject.toString());
            salida.flush();
            salida.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public static String downloadJSON(String archive){
        StringBuilder contenido = new StringBuilder();
        String lectura= "";
        File archivo = new File(archive+".json");
        try
        {
            if (!archivo.exists()) {
                archivo.createNewFile();      // crea productos.json
                uploadJSON(new JSONArray(),archive); // escribe un JSON vacío
                return "";
            }

            BufferedReader entrada = new BufferedReader(new FileReader(archive+".json"));
            while((lectura = entrada.readLine())!=null){
                contenido.append(lectura);
            }
            entrada.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }


        return contenido.toString();
    }


    // Crear archivo si no existe
    public static void inicializarArchivo(String archivo) throws IOException {
        File file = new File(archivo + ".json");

        if (!file.exists()) {
            FileWriter fw = new FileWriter(file);
            fw.write("[]"); // Archivo vacío pero JSON válido
            fw.close();
            System.out.println("Archivo usuarios.json creado.");
        }
    }






}
