package insta;

import Insta.InstaLogin;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

public class InstaSignup extends JFrame implements ActionListener {
    JLabel logoImg = new JLabel();
    JLabel nombreLabel = new JLabel("Nombre: ");
    JTextField nombreField = new JTextField();
    JLabel usernameLabel = new JLabel("Username: ");
    JTextField usernameField = new JTextField();
    JLabel passwordLabel = new JLabel("Password: ");
    JPasswordField passwordField = new JPasswordField();
    JLabel edadLabel = new JLabel("Edad: ");
    JTextField edadField = new JTextField();
    JLabel generoLabel = new JLabel("Genero: ");
    JComboBox<Character> generoComboBox = new JComboBox<>(new Character[]{'M', 'F'});
    private JButton registrarButton;
    private JButton seleccionarImagenButton;
    private JButton regresarButton;
    JLabel imagenLabel = new JLabel();
    String rutaImagenPerfil;

    public InstaSignup() {
        setTitle("Registro de Usuario");
        setSize(730, 480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(4, 1)); // Cambiado a GridLayout con una columna

        // Logo Panel
        JPanel panelLogo = new JPanel();
        panelLogo.setBackground(Color.white);
        logoImg.setSize(220, 85); // Establece el tamaño preferido
        logoImg.setBorder(new EmptyBorder(40, 0, 0, 0));
        setImageLabel(logoImg, "src\\imgs\\Instagram_logo.svg.png");
        panelLogo.add(logoImg);
        mainPanel.add(panelLogo);
        
        // Panel de Datos
        JPanel panelDatos = new JPanel(new GridLayout(6, 2));
        panelDatos.setBorder(new EmptyBorder(0, 400, 0, 400));
        panelDatos.setBackground(Color.white);
        panelDatos.add(nombreLabel);
        panelDatos.add(nombreField);
        panelDatos.add(generoLabel);
        panelDatos.add(generoComboBox);
        panelDatos.add(usernameLabel);
        panelDatos.add(usernameField);
        panelDatos.add(passwordLabel);
        panelDatos.add(passwordField);
        panelDatos.add(edadLabel);
        panelDatos.add(edadField);
        mainPanel.add(panelDatos);
        
        // Panel de Imagen
        JPanel imagenPanel = new JPanel(new FlowLayout());
        imagenPanel.setBackground(Color.white);
        imagenLabel.setSize(150, 150); // Tamaño cuadrado
        imagenLabel.setHorizontalAlignment(JLabel.CENTER);
        imagenLabel.setBorder(BorderFactory.createLineBorder(Color.blue));
        seleccionarImagenButton = crearBoton("Seleccionar Imagen");
        imagenPanel.add(new JLabel());
        imagenPanel.add(seleccionarImagenButton);
        imagenPanel.add(imagenLabel);
        mainPanel.add(imagenPanel);
        
        // Panel de Botones
        JPanel botones = new JPanel(new FlowLayout());
        botones.setBackground(Color.white);
        regresarButton = crearBoton("Regresar a Login"); 
        registrarButton = crearBoton("Registrar"); 
        registrarButton.setBackground(Color.blue);
        registrarButton.setForeground(Color.white);
        botones.add(regresarButton);
        botones.add(registrarButton);
        mainPanel.add(botones);
        
        add(mainPanel, BorderLayout.CENTER);
        setVisible(true);
    }
    
    private void setImageLabel(JLabel labelName, String root) {
        ImageIcon image = new ImageIcon(root);
        Icon icon = new ImageIcon(image.getImage().getScaledInstance(labelName.getWidth(), labelName.getHeight(), Image.SCALE_DEFAULT));
        labelName.setIcon(icon);
        this.repaint();
    }
    
    private JButton crearBoton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(255, 255, 255));
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.addActionListener(this);
        return button;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == seleccionarImagenButton) {
            JFileChooser fileChooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Imágenes", "jpg", "png", "jpeg");
            fileChooser.setFileFilter(filter);

            int result = fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                rutaImagenPerfil = selectedFile.getAbsolutePath();

                // Cargar la imagen seleccionada y mostrarla en el JLabel
                ImageIcon imageIcon = new ImageIcon(rutaImagenPerfil);

                // Redimensionar la imagen para ajustarla al tamaño del JLabel
                Image image = imageIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                imagenLabel.setIcon(new ImageIcon(image));
            }
        } else if (e.getSource() == registrarButton) {
            String nombre = nombreField.getText();
            char genero = (char) generoComboBox.getSelectedItem();
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String edadTexto = edadField.getText();
            int edad;
            
            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(null, "El campo de nombre no puede estar vacío.");
                return;
            }

            if (username.isEmpty()) {
                JOptionPane.showMessageDialog(null, "El campo de username no puede estar vacío.");
                return;
            }

            if (password.isEmpty()) {
                JOptionPane.showMessageDialog(null, "El campo de contraseña no puede estar vacío.");
                return;
            }

            try {
                edad = Integer.parseInt(edadTexto);
                if (edad <= 0) {
                    JOptionPane.showMessageDialog(null, "La edad debe ser un número positivo.");
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Por favor ingrese una edad válida.");
                return;
            }

            if (rutaImagenPerfil == null || rutaImagenPerfil.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Por favor selecciona una imagen de perfil.");
                return;
            }

            IgCuentas.crearUsuario(nombre, genero, username, password, edad, rutaImagenPerfil);
            JOptionPane.showMessageDialog(null, "Usuario registrado con éxito!");

            InstaLogin loginFrame = new InstaLogin();
            loginFrame.setVisible(true);
            dispose(); 
        } else if (e.getSource() == regresarButton) {
            // Volver al login
            InstaLogin loginFrame = new InstaLogin();
            loginFrame.setVisible(true);
            dispose(); // Cierra la ventana actual
        }
    }

    public static void main(String[] args) {
        InstaSignup frame = new InstaSignup();
    }
}
