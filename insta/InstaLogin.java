package insta;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class InstaLogin extends JPanel implements ActionListener {
    JLabel logoLabel = new JLabel();
    JLabel image = new JLabel();
    JTextField usuarioTxt = new JTextField();
    JPasswordField passwordTxt = new JPasswordField();
    JButton login = new JButton("login");
    JButton signup = new JButton("Crear nueva cuenta");
    JPanel contentPanel = new JPanel(new CardLayout());
    private JPanel mainPanel = new JPanel(new BorderLayout());
    IgCuentas cuentas;
    private String cuenta, password;

    public InstaLogin() {
    cuentas = new IgCuentas();
    
    contentPanel.setLayout(new CardLayout());
    contentPanel.setBackground(Color.white);

    mainPanel.setLayout(new BorderLayout());
    mainPanel.setBackground(Color.white);
    

       
    JPanel centerPanel = new JPanel();
    centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.PAGE_AXIS));
    centerPanel.setBackground(Color.white);
    
    setImageLabel(logoLabel, "src\\imgs\\Instagram_logo.svg.png");
    logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    centerPanel.add(logoLabel);
    centerPanel.add(Box.createVerticalStrut(20));
    
    usuarioTxt.setMaximumSize(new Dimension(210, 30));
    usuarioTxt.setAlignmentX(Component.CENTER_ALIGNMENT);
    centerPanel.add(usuarioTxt);
    centerPanel.add(Box.createVerticalStrut(20));
    
    passwordTxt.setMaximumSize(new Dimension(210, 30));
    passwordTxt.setAlignmentX(Component.CENTER_ALIGNMENT);
    centerPanel.add(passwordTxt);
    centerPanel.add(Box.createVerticalStrut(20));
    
    login.addActionListener(this);
    login.setBackground(Color.blue);
    login.setForeground(Color.white);
    login.setAlignmentX(Component.CENTER_ALIGNMENT);
    centerPanel.add(login);
    centerPanel.add(Box.createVerticalStrut(30));
    signup.addActionListener(this);
    signup.setAlignmentX(Component.CENTER_ALIGNMENT);
    centerPanel.add(signup);
    centerPanel.add(Box.createVerticalStrut(20));
    
    mainPanel.add(centerPanel, BorderLayout.CENTER);
    
    mainPanel.setBorder(null);
    
    contentPanel.add(mainPanel, "default");
    
    setLayout(new BorderLayout());
    add(contentPanel, BorderLayout.CENTER);
}


    private void setImageLabel(JLabel labelName, String root) {
        ImageIcon image = new ImageIcon(root);
        Icon icon = new ImageIcon(image.getImage().getScaledInstance(260, 120, Image.SCALE_DEFAULT));
        labelName.setIcon(icon);
        this.repaint();
    }
    
    public JPanel getMainPanel() {
        return mainPanel;  
    }

    public void actionPerformed(ActionEvent e) {
        CardLayout cardActual = (CardLayout) (contentPanel.getLayout());

        if (login == e.getSource()) {
            cuenta = usuarioTxt.getText().toLowerCase();
            password = new String(passwordTxt.getPassword());

            if (usuarioTxt.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Ingrese el usuario.", "Error", JOptionPane.ERROR_MESSAGE);
                usuarioTxt.requestFocus();
                return;
            }
            if (String.valueOf(passwordTxt.getPassword()).isEmpty()) {
               JOptionPane.showMessageDialog(null, "Ingrese la contraseña.", "Error", JOptionPane.ERROR_MESSAGE);
                passwordTxt.requestFocus();
                return; 
            }
            if (cuentas.iniciarSesion(cuenta, password)){
            contentPanel.add(new Insta(this), "instagram");
            cardActual.show(contentPanel, "instagram");
            } else {
                JOptionPane.showMessageDialog(null,"Error al ingresar");
            }
        } else if (signup == e.getSource()) {
            contentPanel.add(new SignupPanel(this), "signup");
            cardActual.show(contentPanel, "signup");
        }
    }
    
    
    class Insta extends JPanel {
        InstaLogin Log;
        Insta(InstaLogin Log){
            this.Log=Log;
            setLayout(new BorderLayout());   
            Instagram ig = new Instagram(Log);
            add(ig, BorderLayout.CENTER);
            revalidate();
            repaint();  
        }
    }
}
