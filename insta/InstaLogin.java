package Insta;
import insta.IgCuentas;
import insta.InstaSignup;
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

public class InstaLogin extends JFrame implements ActionListener{
    JLabel perfilImg = new JLabel();
    JLabel image = new JLabel();
    JLabel signupLabel = new JLabel("¿No tienes cuenta?");
    JTextField usuarioTxt = new JTextField("Ingrese usuario");  
    JPasswordField passwordTxt = new JPasswordField("Ingrese contraseña"); 
    JButton login = new JButton("login");
    JButton signup = new JButton("Sign up");
    private JPanel mainPanel;

    public InstaLogin() {
        setSize(730, 480);
        setTitle("Instagram");
        mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setLayout(null);
        mainPanel.setBackground(Color.white);

        image.setBounds(70, 70, 250, 300);
        setImageLabel(image, "src\\imgs\\gatti.jpg");
        mainPanel.add(image);
        
        perfilImg.setBounds(370, 60, 220, 85);
        setImageLabel(perfilImg, "src\\imgs\\Instagram_logo.svg.png");
        mainPanel.add(perfilImg);

        usuarioTxt.setBounds(370, 170, 220, 40);
        usuarioTxt.setFont(new java.awt.Font("Trebuchet MS", 1, 12));
        usuarioTxt.setForeground(Color.gray);
        mainPanel.add(usuarioTxt);

        passwordTxt.setBounds(370, 240, 220, 40);
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

        login.setBounds(400, 300, 160, 30);
        login.setFont(new java.awt.Font("Trebuchet MS", 4, 12));
        login.setForeground(Color.white);
        login.setBackground(Color.blue);
        login.addActionListener(this);
        mainPanel.add(login);
        
        signupLabel.setBounds(340, 340, 220, 40);
        signupLabel.setFont(new java.awt.Font("Trebuchet MS", 1, 12));
        signupLabel.setForeground(Color.gray);
        mainPanel.add(signupLabel);
        
        signup.setBounds(460, 350, 80, 20);
        signup.setFont(new java.awt.Font("Trebuchet MS", 4, 12));
        signup.setForeground(Color.blue);
        signup.setBackground(Color.white);
        signup.addActionListener(this);
        mainPanel.add(signup);

        
        
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
        
    
    public static void main(String[] args) {
        InstaLogin frame = new InstaLogin();
    }

    public void actionPerformed(ActionEvent e) {
        if (login==e.getSource()){
            String cuenta = usuarioTxt.getText().toLowerCase();
            String password = new String(passwordTxt.getPassword());

            if ((usuarioTxt.getText()).isEmpty()==true){
                JOptionPane.showMessageDialog(null, "Ingrese el usuario.", "Error", JOptionPane.ERROR_MESSAGE);
                usuarioTxt.requestFocus();
                return;
            }

           IgCuentas.iniciarSesion(cuenta, password);
       }
        
        if (signup==e.getSource()){
           new InstaSignup().setVisible(true);
           this.setVisible(false); 
        }


    }
}