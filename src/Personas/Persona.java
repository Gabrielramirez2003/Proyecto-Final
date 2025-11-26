package Personas;

import org.json.JSONObject;

import java.util.UUID;

public abstract class Persona {
    private String id;
    protected String nombre;
    protected String email;
    protected String telefono;


//CONSTRUCTORS

    public Persona() {

        this.id = UUID.randomUUID().toString();

    }


    public Persona(String nombre, String email, String telefono) {

        this();

        this.nombre = nombre;

        this.email = email;

        this.telefono = telefono;

    }


    //GETTERS & SETTERS
    protected String getId() {

        return id;

    }


    public String getNombre() {

        return nombre;

    }


    public void setNombre(String nombre) {

        this.nombre = nombre;

    }


    public String getEmail() {

        return email;

    }


    public void setEmail(String email) {

        this.email = email;

    }


    public String getTelefono() {

        return telefono;

    }


    public void setTelefono(String telefono) {

        this.telefono = telefono;

    }


//METODS


    public abstract JSONObject personaToJSONObject();


    @Override

    public String toString() {

        return "ID: " + this.id + '\n' +

                "Nombre: " + this.nombre + '\n' +

                " Email: " + this.email + '\n' +

                " Telefono: " + this.telefono;

    }

}