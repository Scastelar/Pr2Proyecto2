package insta;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class IgCuentas {
    private static final String dirUsuarios = "users.ins";
    private IgUser usuario;

    IgCuentas(){
        this.usuario = null;
    }

    // Leer todos los usuarios del archivo binario
    public static List<IgUser> leerUsuarios() {
        List<IgUser> usuarios = new ArrayList<>();
        File file = new File(dirUsuarios);
        if (!file.exists()) {
            System.out.println("No existe el archivo, se creará uno nuevo.");
            return usuarios; // Retorna una lista vacía si el archivo no existe
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            usuarios = (List<IgUser>) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("Archivo no encontrado, se creará uno nuevo.");
        } catch (InvalidClassException e) {
            System.out.println("Error de versión de clase. El archivo puede estar corrupto o incompatible.");
            e.printStackTrace();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return usuarios;
    }

    // Guardar la lista de usuarios en el archivo binario
    public static void escribirUsuarios(List<IgUser> usuarios) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(dirUsuarios))) {
            oos.writeObject(usuarios);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Verificar si el username es único
    public static boolean isUsernameUnico(String username) {
        List<IgUser> usuarios = leerUsuarios();
        for (IgUser usuario : usuarios) {
            if (usuario.getUsername().equals(username)) {
                return false;
            }
        }
        return true;
    }

    // Crear un nuevo usuario
    public static void crearUsuario(String nombre, char genero, String username, String password, int edad, String fotoPerfil) {
        if (isUsernameUnico(username)) {
            IgUser nuevoUsuario = new IgUser(nombre, genero, username, password, edad, fotoPerfil);
            List<IgUser> usuarios = leerUsuarios();
            usuarios.add(nuevoUsuario);
            escribirUsuarios(usuarios);
            System.out.println("Usuario creado exitosamente.");
            crearCarpetaUsuario(username);
        } else {
            System.out.println("El username ya existe.");
        }
    }

    // Crear carpeta del usuario
    public static void crearCarpetaUsuario(String username) {
        File directorio = new File(username);
        if (!directorio.exists()) {
            if (directorio.mkdir()) {
                System.out.println("Directorio " + username + " creado.");
                try {
                    new File(directorio, "following.ins").createNewFile();
                    new File(directorio, "followers.ins").createNewFile();
                    new File(directorio, "insta.ins").createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("No se pudo crear el directorio.");
            }
        }
    }

    // Iniciar sesión
    public boolean iniciarSesion(String username, String password) {
        List<IgUser> usuarios = leerUsuarios();
        for (IgUser usuario : usuarios) {
            if (usuario.getUsername().equals(username) && usuario.getPassword().equals(password)) {
                if (usuario.isActivo()) {
                    System.out.println("Inicio de sesión exitoso. Bienvenido, " + usuario.getNombre());
                    this.usuario = usuario; // Opcional: almacenar el usuario en una variable de instancia
                    return true;
                } else {
                    System.out.println("La cuenta está desactivada.");
                    return false;
                }
            }
        }
        System.out.println("Username o password incorrectos.");
        return false;
    }

    public IgUser getUsuario() {
        return usuario;
    }
}
