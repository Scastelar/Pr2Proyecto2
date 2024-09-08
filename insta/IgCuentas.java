package insta;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class IgCuentas {
    private static final String dirUsuarios = "users.ins";

    // Leer todos los usuarios del archivo binario
    public static List<IgUser> leerUsuarios() {
        List<IgUser> usuarios = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(dirUsuarios))) {
            usuarios = (List<IgUser>) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("No existe el archivo, se creará uno nuevo.");
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
    public static boolean esUsernameUnico(String username) {
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
        if (esUsernameUnico(username)) {
            IgUser nuevoUsuario = new IgUser(nombre, genero, username, password, edad, fotoPerfil);
            List<IgUser> usuarios = leerUsuarios();
            usuarios.add(nuevoUsuario);
            escribirUsuarios(usuarios);
            System.out.println("Usuario creado exitosamente.");
            crearCarpetaUsuario(username);
        } else {
            System.out.println("El username ya está en uso.");
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
    
    public static IgUser iniciarSesion(String username, String password) {
        List<IgUser> usuarios = leerUsuarios();
        for (IgUser usuario : usuarios) {
            if (usuario.getUsername().equals(username) && usuario.getPassword().equals(password)) {
                if (usuario.isActivo()) {
                    System.out.println("Inicio de sesión exitoso. Bienvenido, " + usuario.getNombre());
                    return usuario; // Retornar el usuario autenticado
                } else {
                    System.out.println("La cuenta está desactivada.");
                    return null;
                }
            }
        }
        System.out.println("Username o password incorrectos.");
        return null;
    }
}
