package sticchi.matias.practico7.entidades;

/**
 * Created by MatiasGui on 10/3/2016.
 */
public class Contacto {

    //    private int id;
    private String nombre;
    private String apellido;
    private String telefono;
    private String grupo; //Familia, Amigos o nulo.

    public Contacto(String nombre, String apellido, String telefono, String grupo) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.grupo = grupo;
    }

    public Contacto(String nombre, String apellido, String telefono) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.grupo = "";
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Retorna 0 si el objeto es del grupo por defecto
    // Retorna 1 Si pertenece al grupo Familia"
    // Retorna 2 si pertenece al grupo "Amigos"
    ///////////////////////////////////////////////////////////////////////////
    public int getGrupo() {
        int ret = 0;
        if (this.grupo.equals("Familia"))
            ret = 1;
        if (this.grupo.equals("Amigos"))
            ret = 2;
        return ret;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public String toString(){
        return nombre+" "+apellido+" - "+telefono+" - "+grupo;
    }
}