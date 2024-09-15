package proyecto2;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

public class Login extends JFrame implements ActionListener {

    private UserManager userManager;
    JLabel perfilImg = new JLabel();
    JTextField usuarioTxt = new JTextField("Ingrese usuario");
    JPasswordField passwordTxt = new JPasswordField("Ingrese contraseña");
    JButton login = new JButton("login");
    private JPanel mainPanel;

    public Login(UserManager userManager) {
        this.userManager = userManager;
        setSize(730, 480);
        setTitle("Windows");
        mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setLayout(null);
        mainPanel.setBackground(Color.white);

        perfilImg.setBounds(280, 100, 130, 130);
        setImageLabel(perfilImg, "src\\imgs\\user.png");
        mainPanel.add(perfilImg);

        usuarioTxt.setBounds(250, 250, 200, 30);
        usuarioTxt.setFont(new java.awt.Font("Trebuchet MS", 1, 12));
        usuarioTxt.setForeground(Color.gray);
        mainPanel.add(usuarioTxt);

        passwordTxt.setBounds(250, 300, 200, 30);
        passwordTxt.setFont(new java.awt.Font("Trebuchet MS", 1, 12));
        passwordTxt.setForeground(Color.gray);
        passwordTxt.setEchoChar((char) 0);
        mainPanel.add(passwordTxt);

        usuarioTxt.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (usuarioTxt.getText().equals("Ingrese usuario")) {
                    usuarioTxt.setText("");
                    usuarioTxt.setForeground(Color.black);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (usuarioTxt.getText().isEmpty()) {
                    usuarioTxt.setForeground(Color.gray);
                    usuarioTxt.setText("Ingrese usuario");
                }
            }
        });

        passwordTxt.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (String.valueOf(passwordTxt.getPassword()).equals("Ingrese contraseña")) {
                    passwordTxt.setText("");
                    passwordTxt.setForeground(Color.black);
                    passwordTxt.setEchoChar('*');
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (String.valueOf(passwordTxt.getPassword()).isEmpty()) {
                    passwordTxt.setForeground(Color.gray);
                    passwordTxt.setText("Ingrese contraseña");
                    passwordTxt.setEchoChar((char) 0);
                }
            }
        });

        login.setBounds(300, 350, 100, 30);
        login.setFont(new java.awt.Font("Trebuchet MS", 4, 12));
        login.setForeground(Color.white);
        login.setBackground(Color.pink);
        login.addActionListener(this);
        mainPanel.add(login);

        JPanel container = new JPanel(new BorderLayout());
        container.add(mainPanel, BorderLayout.CENTER);
        add(container);

        setContentPane(mainPanel);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(this);
        setVisible(true);
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    private void setImageLabel(JLabel labelName, String root) {
        ImageIcon image = new ImageIcon(root);
        Icon icon = new ImageIcon(image.getImage().getScaledInstance(labelName.getWidth(), labelName.getHeight(), Image.SCALE_DEFAULT));
        labelName.setIcon(icon);
        this.repaint();
    }

    public void actionPerformed(ActionEvent e) {
        if (login == e.getSource()) {
            String nombre = usuarioTxt.getText();
            String contrasena = String.valueOf(passwordTxt.getPassword());
            if (userManager.login(nombre, contrasena)) {
                System.out.println("Ingreso de " + nombre + " exitoso!");
                new Windows(userManager).setVisible(true);
                this.setVisible(false);
            }
        }
    }

    public static void main(String[] args) {
        UserManager userManager = new UserManager();
        new Login(userManager).setVisible(true);
    }
}
