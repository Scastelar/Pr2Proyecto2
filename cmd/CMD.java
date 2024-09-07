
package cmd;

import proyecto2.Navegador;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;
import proyecto2.UserManager;

public class CMD extends JFrame {
    private Funciones cmd = new Funciones("");
    private JTextArea Windows = new JTextArea();
    private JScrollPane scrollPane = new JScrollPane(Windows);
    private Navegador navegador;
    UserManager userManager;

    public CMD(UserManager userManager) {
        String usuarioActual = userManager.getCurrentUser().getUsername();
        this.userManager = userManager;
        this.cmd = new Funciones ("Z/Users/" + usuarioActual);
      //this.cmd = new Funciones (userManager.getCurrentUser().getUserDir().getPath());
        
        Frame();
        Windows.setText("Bienvenido al CMD!\n" + cmd.getCurrentPath() + ">");
    }

    private void Frame() {
        Windows.setBackground(Color.black);
        Windows.setForeground(Color.white);
        Windows.setFont(new java.awt.Font("Consolas", 0, 12));
        getContentPane().setLayout(new BorderLayout());

        Windows.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    e.consume(); 
                    String text = Windows.getText();
                    int lastIndex = text.lastIndexOf("\n" + cmd.getCurrentPath() + ">");
                    if (lastIndex != -1) {
                        String command = text.substring(lastIndex + cmd.getCurrentPath().length() + 2).trim();
                        parseCommand(command);
                    }
                }
            }
        });

        setTitle("CMD");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        Windows.setVisible(true);
    }

    private void parseCommand(String command) {
        String[] comando = command.trim().split(" ");
        switch (comando[0].toLowerCase()) {
            case "cd":
                if (comando.length == 1) {
                    print("Ingrese un directorio para cambiar.");
                } else {
                    cmd.cd(comando[1]);
                }
                break;
            case "cd..":
                cmd.cd("..");
                break;
            case "dir":
                print(cmd.listarFiles(cmd.getCurrentPath()));
                break;
            case "mkdir":
                if (comando.length == 1) {
                    print("Ingrese un directorio para crear.");
                } else {
                    print(cmd.mkdir(cmd.getCurrentPath() + "/" + comando[1]));
                }
                break;
            case "rm":
                if (comando.length == 1) {
                    print("Ingrese la carpeta/archivo que desea eliminar.");
                } else {
                    File destino = new File(cmd.getCurrentPath() + "/" + comando[1]);
                    print(cmd.Eliminar(destino));
                }
                break;
            case "date":
                print(LocalDate.now().toString());
                break;
            case "time":
                print(LocalTime.now().toString());
                break;
        }
        Windows.setText(Windows.getText() + "\n" + cmd.getCurrentPath() + ">");
    }

    private void print(String msg) {
        String newText = Windows.getText();
        newText += "\n" + msg;
        Windows.setText(newText);
    }
    
    public JPanel getJFrame(){
        return (JPanel)
                this.getContentPane();
    }

   
}