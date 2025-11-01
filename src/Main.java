import Excepciones.emailInvalidoEx;
import Personas.Cliente;
import Personas.Empleado;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        File usuarios = new File("usuarios.json");

        Cliente c1 = new Cliente("Gabriel", "aaaaa@aaaaa","2235316974","valencia 5476","20445894827");
        //Cliente c2 = new Cliente("Rocio", "aaaaa@bbbbb","2235543121","valencia 33333");


        Empleado e = new Empleado();
        Empleado a = new Empleado();

        e.register("Gabriel", "gabriel117_@outlook.com", "2235316974", "122113Ga");


        boolean ingreso = e.loggin("gabriel113_@outlook.com","122113Ga");


        boolean registro = c1.registrarCliente(c1.getNombre(),c1.getEmail(),c1.getTelefono(),c1.getDireccion(),c1.getCuit());

        System.out.println(registro);
    }
}