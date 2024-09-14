/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package insta;

import java.awt.BorderLayout;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Comentarios extends JPanel {
    private static IgUser usuarioActivo;
    static InstaLogin Log;
    private static String usuarioActual;

    public Comentarios(InstaLogin Log) {
        this.Log=Log;
        usuarioActual = Log.cuentas.getUsuario().getUsername();
        setSize(400, 300);
        setLayout(new BorderLayout());

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);

        List<String> comentarios = cargarComentarios();
        comentarios.forEach(c -> textArea.append(c + "\n"));

        add(new JScrollPane(textArea), BorderLayout.CENTER);
    }

    public static List<String> cargarComentarios() {
        List<String> comentarios = new ArrayList<>();
        comentarios.addAll(leerComentarios(usuarioActual));

        List<String> following = Perfil.leerUsuariosDeArchivo(usuarioActual + "/following.ins");
        for (String followedUser : following) {
            comentarios.addAll(leerComentarios(followedUser));
        }

        comentarios.sort(Comparator.reverseOrder());

        return comentarios;
    }

    public static List<String> leerComentarios(String username) {
        List<String> comentarios = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(username + "/insta.ins"))) {
            comentarios = (List<String>) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("No se encontraron comentarios para " + username);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return comentarios;
    }
}

