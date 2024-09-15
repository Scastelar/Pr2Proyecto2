package insta;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class Perfil extends JPanel implements ActionListener {

    private InstaLogin Log;
    private JLabel labelFotoPerfil;
    private JTextField infoUsuario;
    private JButton botonPost;
    static JTextField datosTxt;
    private JPanel panelPosts;
    private JPanel userInfoPanel = new JPanel();
    static JPanel profilePanel = new JPanel();

    public Perfil(InstaLogin Log) {
        this.Log = Log;
        inicializarComponentes();
        cargarImagenesEnPanel();
    }

    private void inicializarComponentes() {
        this.setLayout(new BorderLayout());

        // Imagen de perfil
        labelFotoPerfil = new JLabel();
        ImageIcon imagenPerfil = new ImageIcon(Log.cuentas.getUsuario().getFotoPerfil());
        labelFotoPerfil.setIcon(new ImageIcon(imagenPerfil.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH)));

        profilePanel.setLayout(new BorderLayout());
        profilePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        profilePanel.setBackground(Color.WHITE);
        profilePanel.add(labelFotoPerfil, BorderLayout.WEST);

        userInfoPanel.setLayout(new GridLayout(2, 1, 5, 5));
        userInfoPanel.setBackground(Color.WHITE);

        infoUsuario = new JTextField(Log.cuentas.getUsuario().getUsername());
        infoUsuario.setHorizontalAlignment(JTextField.CENTER);
        infoUsuario.setEditable(false);
        infoUsuario.setBorder(BorderFactory.createEmptyBorder());
        infoUsuario.setFont(new Font("Arial", Font.BOLD, 25));
        infoUsuario.setBackground(Color.white);
        userInfoPanel.add(infoUsuario);

        datosTxt = new JTextField("Followers: " + Log.cuentas.getUsuario().getCantidadFollowers() + " |  Following: " + Log.cuentas.getUsuario().getCantidadFollowing());
        datosTxt.setHorizontalAlignment(JTextField.CENTER);
        datosTxt.setEditable(false);
        datosTxt.setBorder(BorderFactory.createEmptyBorder());
        datosTxt.setFont(new Font("Arial", Font.PLAIN, 16));
        datosTxt.setBackground(Color.white);
        userInfoPanel.add(datosTxt);

        userInfoPanel.setBorder(BorderFactory.createLineBorder(Color.white, 2));
        profilePanel.setBorder(BorderFactory.createLineBorder(Color.white, 2));
        profilePanel.add(userInfoPanel, BorderLayout.CENTER);
        add(profilePanel, BorderLayout.NORTH);

        // Panel para las imágenes
        panelPosts = new JPanel();
        panelPosts.setLayout(new GridBagLayout());
        panelPosts.setBackground(Color.WHITE);
        panelPosts.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelPosts.setBorder(BorderFactory.createLineBorder(Color.white, 2));
        JScrollPane scrollPane = new JScrollPane(panelPosts);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane, BorderLayout.CENTER);

        // Botón para postear imágenes
        botonPost = new JButton("Cargar imagen");
        botonPost.setBackground(Color.black);
        botonPost.setForeground(Color.white);
        botonPost.setFont(new Font("Arial", Font.BOLD, 16));
        botonPost.addActionListener(this);
        profilePanel.add(botonPost, BorderLayout.EAST);
    }

    private void cargarImagenesEnPanel() {
        panelPosts.removeAll();
        File directorioImagenes = new File(Log.cuentas.getUsuario().getUsername(), "imagenes/");

        if (directorioImagenes.exists() && directorioImagenes.isDirectory()) {
            File[] imagenes = directorioImagenes.listFiles((dir, name)
                    -> name.endsWith(".jpg") || name.endsWith(".png") || name.endsWith(".jpeg")
            );

            if (imagenes != null) {
                // Ordenar las imágenes para que las más recientes aparezcan primero
                java.util.Arrays.sort(imagenes, (f1, f2) -> Long.compare(f2.lastModified(), f1.lastModified()));

                GridBagConstraints constraints = new GridBagConstraints();
                constraints.gridx = 0;
                constraints.gridy = 0;
                constraints.insets = new Insets(10, 10, 10, 10);

                for (int i = 0; i < imagenes.length; i++) {
                    ImageIcon icon = new ImageIcon(imagenes[i].getAbsolutePath());
                    // Cambiar el tamaño de las imágenes a 200x200
                    Image scaledImage = icon.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);
                    JLabel label = new JLabel(new ImageIcon(scaledImage));

                    panelPosts.add(label, constraints);

                    // Ajustar a 3 columnas
                    if ((i + 1) % 3 == 0) {
                        constraints.gridx = 0;
                        constraints.gridy++;
                    } else {
                        constraints.gridx++;
                    }
                }
            }
        }

        datosTxt.setText("Followers: " + Log.cuentas.getUsuario().getCantidadFollowers() + " |  Following: " + Log.cuentas.getUsuario().getCantidadFollowing());

        profilePanel.revalidate();
        profilePanel.repaint();

        panelPosts.revalidate();
        panelPosts.repaint();
    }

    // Método para subir una imagen
    private void cargarImagen() {
        // Usamos JFileChooser para seleccionar una imagen
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int resultado = fileChooser.showOpenDialog(this);

        if (resultado == JFileChooser.APPROVE_OPTION) {
            File archivoSeleccionado = fileChooser.getSelectedFile();

            // Directorio donde se va a guardar la imagen
            File directorioImagenes = new File(Log.cuentas.getUsuario().getUsername(), "imagenes");

            // Si el directorio no existe, lo creamos
            if (!directorioImagenes.exists()) {
                directorioImagenes.mkdirs();  // Crea el directorio y subdirectorios necesarios
            }

            // Archivo de destino con la ruta completa
            File destino = new File(directorioImagenes, archivoSeleccionado.getName());

            try {
                // Copiamos la imagen a la carpeta del usuario
                Files.copy(archivoSeleccionado.toPath(), destino.toPath(), StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Imagen subida exitosamente a: " + destino.getAbsolutePath());
                cargarImagenesEnPanel();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (botonPost == e.getSource()) {
            cargarImagen();
        }
    }
}
