package insta;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class IgCuentas {

    private static final String dirUsuarios = "users.ins";
    private IgUser usuario;

    IgCuentas() {
        this.usuario = null;
    }

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
    public static List<IgUser> leerUsuarios() {
        List<IgUser> usuarios = new ArrayList<>();
        File file = new File(dirUsuarios);
        if (!file.exists()) {
            System.out.println("No existe el archivo, se creará uno nuevo.");
            return usuarios; 
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

    public static void escribirUsuarios(List<IgUser> usuarios) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(dirUsuarios))) {
            oos.writeObject(usuarios);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isUsernameUnico(String username) {
        List<IgUser> usuarios = leerUsuarios();
        for (IgUser usuario : usuarios) {
            if (usuario.getUsername().equals(username)) {
                return false;
            }
        }
        return true;
    }

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


    public boolean iniciarSesion(String username, String password) {
        List<IgUser> usuarios = leerUsuarios();
        for (IgUser usuario : usuarios) {
            if (usuario.getUsername().equals(username) && usuario.getPassword().equals(password)) {
                    System.out.println("Inicio de sesión exitoso. Bienvenido, " + usuario.getNombre());
                    this.usuario = usuario;
                    return true;
            }
        }
        System.out.println("Username o password incorrectos.");
        return false;
    }

    public IgUser getUsuario() {
        return usuario;
    }
    public void setUsuario(IgUser user) {
        this.usuario = user;
    }


    private List<String> leerListaDesdeArchivo(String archivoRuta) {
        List<String> lista = new ArrayList<>();
        File archivo = new File(archivoRuta);

        if (archivo.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
                String linea;
                while ((linea = reader.readLine()) != null) {
                    lista.add(linea.trim());
                }
            } catch (IOException e) {
                e.printStackTrace(); 
            }
        }
        return lista;
    }


    private void escribirListaEnArchivo(String archivoRuta, List<String> lista) {
        File archivo = new File(archivoRuta);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo))) {
            for (String item : lista) {
                writer.write(item);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace(); 
        }
    }

    public void seguir(String usuarioLogueado, String usuarioASeguir) {
        String archivoFollowing = usuarioLogueado + "/following.ins";
        String archivoFollowers = usuarioASeguir + "/followers.ins";

        List<String> siguiendo = leerListaDesdeArchivo(archivoFollowing);
        List<String> seguidores = leerListaDesdeArchivo(archivoFollowers);

        if (!siguiendo.contains(usuarioASeguir)) {
            siguiendo.add(usuarioASeguir);
            seguidores.add(usuarioLogueado);

            escribirListaEnArchivo(archivoFollowing, siguiendo);
            escribirListaEnArchivo(archivoFollowers, seguidores);

            System.out.println("Ahora sigues a " + usuarioASeguir);
        } else {
            System.out.println("Ya sigues a " + usuarioASeguir);
        }
    }

    public void dejarDeSeguir(String usuarioLogueado, String usuarioADejarDeSeguir) {
        String archivoFollowing = usuarioLogueado + "/following.ins";
        String archivoFollowers = usuarioADejarDeSeguir + "/followers.ins";

        List<String> siguiendo = leerListaDesdeArchivo(archivoFollowing);
        List<String> seguidores = leerListaDesdeArchivo(archivoFollowers);

        if (siguiendo.contains(usuarioADejarDeSeguir)) {
            siguiendo.remove(usuarioADejarDeSeguir);
            seguidores.remove(usuarioLogueado);

            escribirListaEnArchivo(archivoFollowing, siguiendo);
            escribirListaEnArchivo(archivoFollowers, seguidores);

            System.out.println("Has dejado de seguir a " + usuarioADejarDeSeguir);
        } else {
            System.out.println("No sigues a " + usuarioADejarDeSeguir);
        }
    }

    public int obtenerCantidadFollowers(String username) {
        String archivoFollowers = username + "/followers.ins";
        return leerListaDesdeArchivo(archivoFollowers).size();
    }

    public int obtenerCantidadFollowing(String username) {
        String archivoFollowing = username + "/following.ins";
        return leerListaDesdeArchivo(archivoFollowing).size();
    }
}
