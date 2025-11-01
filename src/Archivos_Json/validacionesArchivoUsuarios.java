package Archivos_Json;

import Excepciones.ContraseniaIncorrectaException;
import Excepciones.contraseniaNoValidaEx;
import Excepciones.emailIncorrectoEx;
import Excepciones.emailInvalidoEx;
import org.json.JSONArray;
import org.json.JSONObject;

public class validacionesArchivoUsuarios {

    //metodo para verificar que el email ingresado sea consistente
    public static void validarEmail(String email) throws emailInvalidoEx {
        if(!email.contains("@") || email == null){
            throw new emailInvalidoEx("El email es invalido");
        }
    }

    //metodo para verificar que la contrasenia ingresada este dentro de los parametros
    public static void validarContrasenia(String contrasenia) throws contraseniaNoValidaEx {
        if(!(contrasenia.length()==8) || contrasenia==null){
            throw new contraseniaNoValidaEx("La contrasenia debe tener 8 caracteres");
        }
    }

    //metodo para corroborar que el mail registrado no exista
    public static void corroborarEmail(String email, JSONArray a) throws emailInvalidoEx {
        for(int i=0;i<a.length();i++){
            if(a.getJSONObject(i).getString("email").equals(email)){
                throw new emailInvalidoEx("Ese email ya esta registredo");
            }
        }
    }

    //metodo para ver si coincide contrasenia con el email
    public static boolean validarIngreso(String email, String contrasenia,JSONArray a) throws ContraseniaIncorrectaException, emailIncorrectoEx {
        for(int i=0; i<a.length();i++){
            JSONObject usuario = a.getJSONObject(i);
            String emailGuardado = usuario.getString("email");
            String contraseniaGuardada = usuario.getString("contrasenia");

            if(emailGuardado.equals(email)){
                if (contraseniaGuardada.equals(contrasenia)){
                    System.out.println("¡Loggin Exitoso!");
                    return true;
                }else{
                    throw new ContraseniaIncorrectaException("La contrasenia es incorrecta");
                }
            }
        }
        throw new emailIncorrectoEx("El email ingresado es incorrecto");
    }
}
