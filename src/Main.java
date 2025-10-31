import Personas.Cliente;

public class Main {
    public static void main(String[] args) {
        Cliente c1 = new Cliente("Gabriel", "aaaaa@aaaaa","2235316974","valencia 5476");
        Cliente c2 = new Cliente("Rocio", "aaaaa@bbbbb","2235543121","valencia 33333");

        System.out.println(c1.toString());
        System.out.println(c2.toString());
    }
}