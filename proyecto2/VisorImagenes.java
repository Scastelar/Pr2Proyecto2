package proyecto2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.ArrayList;
import java.util.List;
import javax.swing.border.EmptyBorder;

public class VisorImagenes extends JPanel implements ActionListener {

    private UserManager userManager;
    private User currentUser;
    private JLabel titulo = new JLabel("Visor de Imagenes", SwingConstants.CENTER);
    private JLabel imageLabel;
    private JButton anterior;
    private JButton siguiente;
    private List<File> imagenes;
    private int currentIndex;

    public VisorImagenes(UserManager userManager) {
        this.userManager = userManager;
        this.currentUser = userManager.getCurrentUser();
        setPreferredSize(new Dimension(730, 480));
        setLayout(new BorderLayout());
        setBackground(new Color(255, 249, 249));

        titulo.setFont(new java.awt.Font("Trebuchet MS", 1, 24));
        titulo.setForeground(new Color(242, 191, 191));
        titulo.setBorder(new EmptyBorder(10, 0, 10, 0));
        add(titulo, BorderLayout.NORTH);

        JPanel panel = new JPanel();
        panel.setBackground(new Color(255, 249, 249));
        imageLabel = new JLabel("", SwingConstants.CENTER);
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.setVerticalAlignment(JLabel.CENTER);
        panel.add(imageLabel, BorderLayout.CENTER);
        add(panel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(255, 249, 249));
        add(buttonPanel, BorderLayout.SOUTH);

        anterior = new JButton();
        anterior.setSize(70, 70);        
        anterior.setBorderPainted(false);
        anterior.setBackground(new Color(255, 249, 249));
        anterior.setFocusPainted(false);
        setImageLabel(anterior,"src\\imgs\\anterior.jpg");
        anterior.addActionListener(this);
        buttonPanel.add(anterior);

        siguiente = new JButton();
        siguiente.setSize(70,70);
        siguiente.setBorderPainted(false);
        siguiente.setBackground(new Color(255, 249, 249));
        siguiente.setFocusPainted(false);
        setImageLabel(siguiente,"src\\imgs\\siguiente.jpg");
        siguiente.addActionListener(this);
        buttonPanel.add(siguiente);

        // Cargar imágenes del usuario actual
        cargarImagenes("Z\\Users\\" + currentUser.getUsername() + "\\Mis Imagenes");
        displayImage(currentIndex);
        setVisible(true);
    }
    
    private void setImageLabel(JButton name, String root) {
        ImageIcon image = new ImageIcon(root);
        Icon icon = new ImageIcon(image.getImage().getScaledInstance(name.getWidth(), name.getHeight(), Image.SCALE_DEFAULT));
        name.setIcon(icon);
        this.repaint();
    }

    private void cargarImagenes(String directoryPath) {
        File dir = new File(directoryPath);
        if (dir.isDirectory()) {
            File[] files = dir.listFiles((d, name) -> name.endsWith(".jpg") || name.endsWith(".png") || name.endsWith(".jpeg"));
            if (files != null) {
                imagenes = new ArrayList<>();
                for (File file : files) {
                    imagenes.add(file);
                }
                currentIndex = 0;
            }
        }
    }

    private void displayImage(int index) {
        if (index >= 0 && index < imagenes.size()) {
            try {
                Image image = ImageIO.read(imagenes.get(index));
                ImageIcon imageIcon = new ImageIcon(image.getScaledInstance(800, 600, Image.SCALE_SMOOTH));
                imageLabel.setIcon(imageIcon);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                JOptionPane.showMessageDialog(null, "No hay imagenes disponibles");
                System.out.println("No hay Imagenes disponibles");
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (anterior == e.getSource()) {
            botonAnterior();
        }
        if (siguiente == e.getSource()) {
            botonSiguiente();
        }

    }

    public void botonAnterior() {
        if (imagenes != null && currentIndex > 0) {
            currentIndex--;
            displayImage(currentIndex);
        }
    }

    public void botonSiguiente() {
        if (imagenes != null && currentIndex < imagenes.size() - 1) {
            currentIndex++;
            displayImage(currentIndex);
        }
    }

}
