package insta;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class IgUser implements Serializable {

    private static final long serialVersionUID = 1L;
    private String nombre;
    private char genero;
    private String username;
    private String password;
    private Date fecha;
    private int edad;
    private String fotoPerfil;
    private boolean estado;
    private transient File userDir;

    public IgUser(String nombre, char genero, String username, String password, int edad, String fotoPerfil) {
        this.nombre = nombre;
        this.genero = genero;
        this.username = username;
        this.password = password;
        this.fecha = Calendar.getInstance().getTime();
        this.edad = edad;
        this.estado = true;
        this.fotoPerfil = fotoPerfil;
        this.userDir = new File(username);
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

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    public int getCantidadFollowers() {
        File followersFile = new File(username, "followers.ins");
        return contarUsuariosEnArchivo(followersFile);
    }

    public int getCantidadFollowing() {
        File followingFile = new File(username, "following.ins");
        return contarUsuariosEnArchivo(followingFile);
    }

    @Override
    public String toString() {
        return "Nombre: " + nombre + ", Username: " + username + ", Genero: " + genero
                + ", Fecha Entrada: " + fecha + ", Edad: " + edad + ", Activo: " + estado
                + ", Foto: " + fotoPerfil;
    }

   private int contarUsuariosEnArchivo(File archivo) {
    int count = 0;
    if (archivo.exists()) {
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
                while (br.readLine() != null) {
                    count++;
                }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    return count;
}

    public boolean isFollowed(IgUser user) {
        File followingFile = new File(username, "following.ins");  
        List<String> siguiendo = leerUsuariosDesdeArchivo(followingFile);
        return siguiendo.contains(user.getUsername());
    }

    public static List<String> leerUsuariosDesdeArchivo(File archivo) {
        List<String> usuarios = new ArrayList<>();
        if (archivo.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
                String line;
                while ((line = br.readLine()) != null) {
                    usuarios.add(line);     
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return usuarios;
    }

    public void agregarSeguidor(IgUser user) {
        File followersFile = new File(username, "followers.ins");
        escribirUsuarioEnArchivo(followersFile, user.getUsername());
    }

    private void escribirUsuarioEnArchivo(File archivo, String username) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo, true))) {
            bw.write(username);
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
