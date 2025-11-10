import ENUMS.EestadosTarjetas;
import Personas.Cliente;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) throws IOException {
        File usuarios = new File("usuarios.json");

        Cliente c1 = new Cliente("Gabriel", "aaaaa@aaaaa","2235316974","valencia 5476","20445894827");
        Cliente c3 = new Cliente("jorge", "aaaaa@aaaaa","223597846","valencia 5476","20885599446");
        //Cliente c2 = new Cliente("Rocio", "aaaaa@bbbbb","2235543121","valencia 33333");


        //Empleado e = new Empleado();
        //Empleado a = new Empleado();

        //e.register("Gabriel", "gabriel117_@outlook.com", "2235316974", "122113Ga");


        //boolean ingreso = e.loggin("gabriel113_@outlook.com","122113Ga");

        EnvolventePractica c2 = new EnvolventePractica();
        c2.agregarCliente(c1.getNombre(),c1.getEmail(),c1.getTelefono(),c1.getDireccion(),c1.getCuit());

        c2.agregarTarjetaCliente("20445894827","Credito","4509-8712-3456-7890",LocalDate.of(2027,10,12),"233",EestadosTarjetas.ACTIVA);

        c2.agregarCliente(c3.getNombre(),c3.getEmail(),c3.getTelefono(),c3.getDireccion(),c3.getCuit());
        c2.agregarTarjetaCliente("2088559944667","Debito","5109-8712-3456-7890", LocalDate.of(2027,3,12),"312",EestadosTarjetas.ACTIVA);


    }
}