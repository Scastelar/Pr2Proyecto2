package insta;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class Comentarios extends JPanel {
    static JTextArea comentariosArea;
    static JScrollPane scrollPane;
    private JTextField nuevoComentarioField;
    private JButton agregarComentarioButton;
    private static File instaFile;
    private static File followingFile;
    private String username;
    InstaLogin Log;
    
    public Comentarios(InstaLogin Log) {
        this.username = Log.cuentas.getUsuario().getUsername();
        instaFile = new File(Log.cuentas.getUsuario().getUsername() , "insta.ins");
        followingFile = new File(Log.cuentas.getUsuario().getUsername(), "following.ins");
        
        setLayout(new BorderLayout());
        
        comentariosArea = new JTextArea(20, 40);
        comentariosArea.setEditable(false);
        scrollPane = new JScrollPane(comentariosArea);
        add(scrollPane, BorderLayout.CENTER);
        
        nuevoComentarioField = new JTextField(30);
        agregarComentarioButton = new JButton("Agregar comentario");
        
        JPanel panelComentario = new JPanel();
        panelComentario.add(nuevoComentarioField);
        panelComentario.add(agregarComentarioButton);
        add(panelComentario, BorderLayout.NORTH);
        
        agregarComentarioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                agregarComentario();
            }
        });
        
        cargarComentarios();
    }
    
    private void agregarComentario() {
        String comentario = nuevoComentarioField.getText();
        if (comentario.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El comentario no puede estar vacío", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (comentario.length() > 140) {
            JOptionPane.showMessageDialog(this, "El comentario no puede exceder los 140 caracteres", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String fecha = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date());
        String nuevoComentario = username + " escribió:\n" + "\"" + comentario + "\" el [" + fecha + "]\n\n";
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(instaFile, true))) {
            writer.write(nuevoComentario);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        nuevoComentarioField.setText("");
        cargarComentarios();
    }
    
    public static void cargarComentarios() {
        comentariosArea.setText("");
        List<String> comentarios = new ArrayList<>();
        
        cargarComentariosDesdeArchivo(instaFile, comentarios);
        
        cargarComentariosDeFollowing(comentarios);
        
        Collections.reverse(comentarios);
        for (String comentario : comentarios) {
            comentariosArea.append(comentario);
        }
    }
    
    private static void cargarComentariosDesdeArchivo(File file, List<String> comentarios) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String linea;
            StringBuilder comentarioActual = new StringBuilder();
            while ((linea = reader.readLine()) != null) {
                comentarioActual.append(linea).append("\n");
                if (linea.trim().isEmpty()) {
                    comentarios.add(comentarioActual.toString());
                    comentarioActual.setLength(0); 
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    private static void cargarComentariosDeFollowing(List<String> comentarios) {
        try (BufferedReader reader = new BufferedReader(new FileReader(followingFile))) {
            String followingUsername;
            while ((followingUsername = reader.readLine()) != null) {
                File followingInstaFile = new File(followingUsername ,"insta.ins");
                
                if (followingInstaFile.exists()) {
                    cargarComentariosDesdeArchivo(followingInstaFile, comentarios);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
