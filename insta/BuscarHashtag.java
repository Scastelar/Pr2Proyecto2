
package insta;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class BuscarHashtag extends JPanel implements ActionListener {
    private InstaLogin Log;
    JLabel tituloLabel = new JLabel("Busqueda de #s", SwingConstants.CENTER);
    JPanel panel = new JPanel();
    JTextField textField = new JTextField(30);
    JButton buscarButton = new JButton();
    JTextArea textArea = new JTextArea();
    
    public BuscarHashtag(InstaLogin Log) {
        this.Log = Log;
        setSize(500, 400);
        setLayout(new BorderLayout());

        tituloLabel.setFont(new Font("Arial", Font.BOLD, 16));

        panel.setBackground(Color.white);
        panel.setLayout(new FlowLayout());
        panel.add(tituloLabel, BorderLayout.NORTH);

        textField.setPreferredSize(new Dimension(300, 30));
        panel.add(textField);

        buscarButton.setSize(40, 40);
        buscarButton.setBackground(Color.white);
        buscarButton.setBorderPainted(false);
        buscarButton.addActionListener(this);
        setImageLabel(buscarButton, "src\\imgs\\search.png");        
        panel.add(buscarButton);

        add(panel, BorderLayout.NORTH);

        // Crear el JTextArea
        textArea.setEditable(false);
        textArea.setRows(10);
        textArea.setColumns(40);
        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);
        setVisible(true);
    }

    private List<String> buscarHashtag(String hashtag) {
        List<String> resultados = new ArrayList<>();
        List<String> comentarios = Comentarios.cargarComentarios(); 
        for (String comentario : comentarios) {
            if (comentario.contains("#" + hashtag)) {
                resultados.add(comentario);
            }
        }
        return resultados;
    }
    
    private void setupButtonIG(JButton button, String iconPath) {
        button.setSize(40, 40);
        button.setBackground(Color.white);
        button.setBorderPainted(false);
        button.addActionListener(this);
        setImageLabel(button, iconPath);
    }
    
    private void setImageLabel(JButton name, String root) {
        ImageIcon image = new ImageIcon(root);
        Icon icon = new ImageIcon(image.getImage().getScaledInstance(name.getWidth(), name.getHeight(), Image.SCALE_DEFAULT));
        name.setIcon(icon);
        this.repaint();
    }

    public void actionPerformed(ActionEvent e) {
        if (buscarButton==e.getSource()){
            String hashtag = textField.getText().trim();
            if (!hashtag.isEmpty()) {
                List<String> resultados = buscarHashtag(hashtag);
                textArea.setText("");
                resultados.forEach(r -> textArea.append(r + "\n"));
            } else {
                JOptionPane.showMessageDialog(null, "El campo esta vacio");
                textField.requestFocus();
            }
        }
    }
}

