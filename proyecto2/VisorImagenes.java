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

public class VisorImagenes extends JPanel {

    private UserManager userManager;
    private User currentUser;
    private JLabel titulo = new JLabel("Visor de Imagenes", SwingConstants.CENTER);
    private JLabel imageLabel;
    private JButton previousButton;
    private JButton nextButton;
    private List<File> imageFiles;
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

        previousButton = new JButton("Anterior");
        previousButton.addActionListener(new PreviousImageActionListener());
        buttonPanel.add(previousButton);

        nextButton = new JButton("Siguiente");
        nextButton.addActionListener(new NextImageActionListener());
        buttonPanel.add(nextButton);

        // Cargar imágenes
        loadImagesFromDirectory("Z\\Users\\" + currentUser.getUsername() + "\\Mis Imagenes");
        displayImage(currentIndex);
        setVisible(true);
    }

    private void loadImagesFromDirectory(String directoryPath) {
        File dir = new File(directoryPath);
        if (dir.isDirectory()) {
            File[] files = dir.listFiles((d, name) -> name.endsWith(".jpg") || name.endsWith(".png") || name.endsWith(".jpeg"));
            if (files != null) {
                imageFiles = new ArrayList<>();
                for (File file : files) {
                    imageFiles.add(file);
                }
                currentIndex = 0;
            }
        }
    }

    private void displayImage(int index) {
        if (index >= 0 && index < imageFiles.size()) {
            try {
                Image image = ImageIO.read(imageFiles.get(index));
                ImageIcon imageIcon = new ImageIcon(image.getScaledInstance(800, 600, Image.SCALE_SMOOTH));
                imageLabel.setIcon(imageIcon);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NullPointerException e){
                JOptionPane.showMessageDialog(null, "No hay imagenes disponibles");
                System.out.println("No hay Imagenes disponibles");
            }
        }
    }

    private class PreviousImageActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (imageFiles != null && currentIndex > 0) {
                currentIndex--;
                displayImage(currentIndex);
            }
        }
    }

    private class NextImageActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (imageFiles != null && currentIndex < imageFiles.size() - 1) {
                currentIndex++;
                displayImage(currentIndex);
            }
        }
    }
    
    

}
