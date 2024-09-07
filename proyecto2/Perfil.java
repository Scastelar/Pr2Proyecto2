package proyecto2;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class Perfil extends JPanel implements ActionListener {
    JTextField usuario = new JTextField();
    JButton crear = new JButton("Crear Usuario");
    JButton cerrarSesion = new JButton("Cerrar Sesión");
    private UserManager userManager;

    
    public Perfil(UserManager userManager) {
        User usuarioActual = userManager.getCurrentUser();
        this.userManager = userManager;
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(730, 480));

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(255, 192, 203)); 
        panel.setPreferredSize(new Dimension(730, 480));

        ImageIcon perfilImg = new ImageIcon("src\\imgs\\user.png"); 
        Image profileImage = perfilImg.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        perfilImg = new ImageIcon(profileImage);
        JLabel profileLabel = new JLabel(perfilImg);
        profileLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel titulo = new JLabel("Mi Perfil");
        titulo.setAlignmentX(CENTER_ALIGNMENT);
        titulo.setFont(new java.awt.Font("Trebuchet MS", 1, 25));
        titulo.setForeground(Color.white);
        titulo.setMaximumSize(new Dimension(120, 50));
        
        usuario.setFont(new Font("Trebuchet MS", Font.BOLD, 25));
        usuario.setForeground(new Color(255, 105, 180)); // Color rosa más suave
        usuario.setBackground(Color.white);
        usuario.setMaximumSize(new Dimension(250, 40)); // Aumenta el tamaño para mejor visualización
        usuario.setHorizontalAlignment(JTextField.CENTER); // Centra el texto dentro del campo
        usuario.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.pink)); // Borde inferior rosa
        usuario.setText(usuarioActual.getUsername());
        usuario.setEditable(false);
        
        if (usuarioActual.getUsername()!="admin"){
          crear.setEnabled(false);
        } else {
          crear.setEnabled(true);
            
        }
            
        crear.setBackground(new Color(255, 105, 180));
        crear.setForeground(Color.WHITE);
        crear.setFont(new java.awt.Font("Trebuchet MS", 1, 17));
        crear.setAlignmentX(Component.CENTER_ALIGNMENT);
        crear.addActionListener(this);

        cerrarSesion.setBackground(new Color(255, 105, 180));
        cerrarSesion.setForeground(Color.WHITE);
        cerrarSesion.setFont(new java.awt.Font("Trebuchet MS", 1, 17));
        cerrarSesion.setAlignmentX(Component.CENTER_ALIGNMENT);
        cerrarSesion.addActionListener(this);
        
        panel.add(Box.createVerticalStrut(40));
        panel.add(profileLabel);
        panel.add(Box.createVerticalStrut(10)); 
        panel.add(titulo);
        panel.add(Box.createVerticalStrut(10)); 
        panel.add(usuario);
        panel.add(Box.createVerticalStrut(25));
        panel.add(crear);
        panel.add(Box.createVerticalStrut(25)); 
        panel.add(cerrarSesion);

        add(Box.createVerticalStrut(20)); 
        add(panel, BorderLayout.CENTER);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (crear==e.getSource()){
        String nombre = JOptionPane.showInputDialog("Ingrese username nuevo:");
        String password = JOptionPane.showInputDialog("Ingrese contraseña");
        
        if (nombre.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Ingrese los datos requeridos.");
                    return;
                }
                if (userManager.userExists(nombre)) {
                    JOptionPane.showMessageDialog(null, "Usuario ya existente.");
                } else {
                  userManager.createUser(nombre, password);
                    JOptionPane.showMessageDialog(null, "Usuario registrado exitosamente.");  
                }
        }
        if (cerrarSesion==e.getSource()){
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            new Login(userManager).setVisible(true);
            frame.setVisible(false);
        }
    }
   
}