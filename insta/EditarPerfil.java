package insta;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

public class EditarPerfil extends JPanel {

    private InstaLogin Log;
    private CardLayout cardLayout = new CardLayout();
    private JPanel contentPanel = new JPanel(cardLayout);
    JButton buscarButton = new JButton();
    JButton activarButton = new JButton("Activar/Desactivar Cuenta");
    JLabel tituloLabel = new JLabel("Busqueda de users  ", SwingConstants.CENTER);
    JPanel panel = new JPanel();
    JTextField textField = new JTextField(30);
    JPanel panelPosts = new JPanel();

    public EditarPerfil(InstaLogin Log) {
        this.Log = Log;
        setSize(500, 400);
        setLayout(new BorderLayout());
        contentPanel.setBackground(Color.white);
        tituloLabel.setFont(new Font("Arial", Font.BOLD, 16));

        panel.setBackground(Color.white);
        panel.setLayout(new FlowLayout());
        panel.add(tituloLabel, BorderLayout.NORTH);

        textField.setPreferredSize(new Dimension(300, 30));
        panel.add(textField);

        buscarButton.setSize(40, 40);
        buscarButton.setBackground(Color.white);
        buscarButton.setBorderPainted(false);
        buscarButton.addActionListener(Log);
        setImageLabel(buscarButton, "src\\imgs\\search.png");
        panel.add(buscarButton);

        add(panel, BorderLayout.NORTH);

        activarButton.setPreferredSize(new Dimension(40, 40));
        add(activarButton, BorderLayout.SOUTH);

        buscarButton.addActionListener(e -> buscarPersonas());
        activarButton.addActionListener(e -> activarDesactivarCuenta());
        add(contentPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    private void setImageLabel(JButton name, String root) {
        ImageIcon image = new ImageIcon(root);
        Icon icon = new ImageIcon(image.getImage().getScaledInstance(name.getWidth(), name.getHeight(), Image.SCALE_DEFAULT));
        name.setIcon(icon);
        this.repaint();
    }

    private void buscarPersonas() {
        String searchUsername = textField.getText();
        if (searchUsername == null || searchUsername.isEmpty()) {
            return;
        }

        List<IgUser> matchingUsers = new ArrayList<>();
        List<IgUser> allUsers = IgCuentas.leerUsuarios();

        for (IgUser user : allUsers) {
            if (user.getUsername().contains(searchUsername)) {
                matchingUsers.add(user);
            }
        }

        if (matchingUsers.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No existen usuarios con ese nombre.");
            return;
        }

        mostrarUsuarios(matchingUsers);
    }

    private void mostrarUsuarios(List<IgUser> users) {
        JPanel userPanel = new JPanel(new BorderLayout());

        String loggedUsername = Log.cuentas.getUsuario().getUsername();

        String[] usernames = users.stream().map(user -> {
            if (user.getUsername().equals(loggedUsername)) {
                return user.getUsername() + " - eres tú";
            } else if (Log.cuentas.getUsuario().isFollowed(user)) {
                return user.getUsername() + " - Lo sigues";
            } else {
                return user.getUsername() + " - No lo sigues";
            }
        }).toArray(String[]::new);

        JList<String> userList = new JList<>(usernames);
        userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        userList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedIndex = userList.getSelectedIndex();
                if (selectedIndex != -1) {
                    mostrarPerfilUsuario(users.get(selectedIndex));
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(userList);
        userPanel.add(scrollPane, BorderLayout.CENTER);

        contentPanel.add(userPanel, "UserList");
        cardLayout.show(contentPanel, "UserList");
    }

    private void mostrarPerfilUsuario(IgUser user) {
        JPanel perfilPanel = new JPanel(new BorderLayout());
        perfilPanel.setBackground(Color.white);

        JPanel topPanel = new JPanel();
        topPanel.setBackground(Color.white);
        topPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        JLabel profilePicture = new JLabel();
        ImageIcon imagenPerfil = new ImageIcon(user.getFotoPerfil());
        profilePicture.setIcon(new ImageIcon(imagenPerfil.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH)));

        JLabel usernameLabel = new JLabel("Username: " + user.getUsername());
        JLabel followersLabel = new JLabel("Followers: " + user.getCantidadFollowers());
        JLabel followingLabel = new JLabel("Following: " + user.getCantidadFollowing());

        JButton btnSeguir = new JButton(Log.cuentas.getUsuario().isFollowed(user) ? "Dejar de seguir" : "Seguir");
        btnSeguir.addActionListener(e -> {
            if (Log.cuentas.getUsuario().isFollowed(user)) {
                System.out.println("Dejar de seguir a " + user.getUsername());
                Log.cuentas.dejarDeSeguir(Log.cuentas.getUsuario().getUsername(), user.getUsername());
                btnSeguir.setText("Seguir");
            } else {
                System.out.println("Seguir a " + user.getUsername());
                Log.cuentas.seguir(Log.cuentas.getUsuario().getUsername(), user.getUsername());
                btnSeguir.setText("Dejar de seguir");
            }

            followersLabel.setText("Followers: " + user.getCantidadFollowers());
            followingLabel.setText("Following: " + user.getCantidadFollowing());
            Perfil.datosTxt.removeAll();
            Perfil.datosTxt.setText("Followers: " + Log.cuentas.getUsuario().getCantidadFollowers() + " |  Following: " + Log.cuentas.getUsuario().getCantidadFollowing());
            // Redibujar los labels para reflejar el cambio
            Comentarios.cargarComentarios();
            Comentarios.scrollPane.revalidate();
            Comentarios.scrollPane.repaint();

            perfilPanel.revalidate();
            perfilPanel.repaint();
        });

        topPanel.add(profilePicture);
        topPanel.add(usernameLabel);
        topPanel.add(followersLabel);
        topPanel.add(followingLabel);
        topPanel.add(btnSeguir);

        panelPosts.setLayout(new GridBagLayout());
        panelPosts.setBackground(Color.white);
        JScrollPane scrollPane = new JScrollPane(panelPosts);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane, BorderLayout.CENTER);

        perfilPanel.add(topPanel, BorderLayout.NORTH);
        perfilPanel.add(scrollPane, BorderLayout.CENTER);
        cargarImagenesEnPanel(user);

        if (Log.cuentas.getUsuario().getUsername().equals(user.getUsername())) {
            btnSeguir.setVisible(false);
        }

        contentPanel.add(perfilPanel, "PerfilUsuario");
        cardLayout.show(contentPanel, "PerfilUsuario");
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void activarDesactivarCuenta() {
        if (Log.cuentas.getUsuario().isActivo()) {
            int confirm = JOptionPane.showConfirmDialog(null, "¿Deseas desactivar tu cuenta?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                Log.cuentas.getUsuario().setEstado(false);
                actualizarUsuario(Log.cuentas.getUsuario());

                // Vacía el contenido del archivo insta.ins
                vaciarArchivo("insta.ins");

                JOptionPane.showMessageDialog(null, "Cuenta Desactivada");
                cardLayout.show(contentPanel, "Login");
            }
        } else {
            int confirm = JOptionPane.showConfirmDialog(null, "¿Deseas activar tu cuenta?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                Log.cuentas.getUsuario().setEstado(true);
                actualizarUsuario(Log.cuentas.getUsuario());
                JOptionPane.showMessageDialog(null, "Cuenta Activada");
            }
        }
    }

    private void vaciarArchivo(String nombreArchivo) {
        File archivo = new File(nombreArchivo);
        try (FileWriter escritor = new FileWriter(archivo)) {
            // Vacía el archivo escribiendo una cadena vacía
            escritor.write("");
            System.out.println("Contenido del archivo " + nombreArchivo + " borrado exitosamente.");
        } catch (IOException e) {
            System.out.println("No se pudo vaciar el archivo " + nombreArchivo + ": " + e.getMessage());
        }
    }

    private void actualizarUsuario(IgUser usuario) {
        List<IgUser> usuarios = IgCuentas.leerUsuarios();
        for (IgUser u : usuarios) {
            if (u.getUsername().equals(usuario.getUsername())) {
                u.setEstado(usuario.isActivo());
            }
        }
        IgCuentas.escribirUsuarios(usuarios);
    }

   private void cargarImagenesEnPanel(IgUser user) {
    panelPosts.removeAll();
    File directorioImagenes = new File(user.getUsername(), "imagenes/");

    if (directorioImagenes.exists() && directorioImagenes.isDirectory()) {
        File[] imagenes = directorioImagenes.listFiles((dir, name)
                -> name.endsWith(".jpg") || name.endsWith(".png") || name.endsWith(".jpeg")
        );

        if (imagenes != null) {
            java.util.Arrays.sort(imagenes, (f1, f2) -> Long.compare(f2.lastModified(), f1.lastModified()));

            GridBagConstraints constraints = new GridBagConstraints();
            constraints.gridx = 0;
            constraints.gridy = 0;
            constraints.insets = new Insets(10, 10, 10, 10);

            for (File imagen : imagenes) {
                ImageIcon icon = new ImageIcon(imagen.getAbsolutePath());
                Image scaledImage = icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
                JButton button = new JButton(new ImageIcon(scaledImage));
                button.setPreferredSize(new Dimension(200, 200));

                // Añadir un ActionListener para el botón
                button.addActionListener(e -> {
                    String comentario = JOptionPane.showInputDialog(this, "Ingrese su comentario:");
                    if (comentario != null) {
                        if (comentario.isEmpty()) {
                            JOptionPane.showMessageDialog(this, "El comentario no puede estar vacío", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        if (comentario.length() > 140) {
                            JOptionPane.showMessageDialog(this, "El comentario no puede exceder los 140 caracteres", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        String fecha = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date());
                        String nuevoComentario = Log.cuentas.getUsuario().getUsername() + " escribió:\n" + "\"" + comentario + "\" el [" + fecha + "]\n\n";
                        
                        // Guardar el comentario en el archivo insta.ins del usuario logueado
                        File instaFile = new File("usuarios/" + Log.cuentas.getUsuario().getUsername() + "/insta.ins");
                        try (BufferedWriter writer = new BufferedWriter(new FileWriter(instaFile, true))) {
                            writer.write(nuevoComentario);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }

                        // Aquí puedes añadir la lógica para actualizar la vista de comentarios si es necesario.
                        Comentarios.cargarComentarios();
                    }
                });

                panelPosts.add(button, constraints);

                if ((constraints.gridx + 1) % 3 == 0) {
                    constraints.gridx = 0;
                    constraints.gridy++;
                } else {
                    constraints.gridx++;
                }
            }
        }
    }

    panelPosts.revalidate();
    panelPosts.repaint();
}

}
