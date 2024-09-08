package insta;

import java.io.File;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class IgUser implements Serializable {

    private String nombre;
    private char genero;
    private String username;
    private String password;
    private Date fecha;
    private int edad;
    private String fotoPerfil;

    public IgUser(String nombre, char genero, String username, String password, int edad, String fotoPerfil) {
        this.nombre = nombre;
        this.genero = genero;
        this.username = username;
        this.password = password;
        this.fecha = Calendar.getInstance().getTime();
        this.edad = edad;
        this.estado = true; 
        this.fotoPerfil = fotoPerfil;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public char getGenero() {
        return genero;
    }

    public void setGenero(char genero) {
        this.genero = genero;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public boolean isActivo() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public File getUserDir() {
        return userDir;
    }

    public void setUserDir(File userDir) {
        this.userDir = userDir;
    }
    private boolean estado;
    private File userDir;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
    
    public String toString() {
        return "Nombre: " + nombre + ", Username: " + username + ", Genero: " + genero +
               ", Fecha Entrada: " + fecha + ", Edad: " + edad + ", Activo: " + estado +
               ", Foto: " + fotoPerfil;
    }

}
