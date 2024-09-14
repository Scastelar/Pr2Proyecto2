package insta;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class Instagram extends JPanel implements ActionListener {

    IgUser user;
    static InstaLogin Log;
    
    JButton perfilIG = new JButton();
    JButton comentar = new JButton();
    JButton editar = new JButton();
    JButton search = new JButton();
    JButton cerrar = new JButton();
    JToolBar barraIG = new JToolBar(JToolBar.VERTICAL);
    JPanel contentPanel = new JPanel(new CardLayout());  
    JPanel mainPanel = new JPanel(new CardLayout());     
    JPanel toolbarPanel = new JPanel(new BorderLayout()); 

    public Instagram(InstaLogin Log) {
        this.Log = Log;
        
        setPreferredSize(new Dimension(650, 480));
        setLayout(new BorderLayout());

        barraIG.setBackground(Color.white);
        barraIG.setFloatable(false);

        setupButtonIG(perfilIG, "src\\imgs\\home.png");
        setupButtonIG(comentar, "src\\imgs\\comment.png");
        setupButtonIG(editar, "src\\imgs\\edit.png");
        setupButtonIG(search, "src\\imgs\\search.png");
        setupButtonIG(cerrar, "src\\imgs\\logout.png");

        contentPanel.setBackground(Color.white);
        contentPanel.add(new Perfil(Log), "perfil");
        contentPanel.add(new EditarPerfil(Log), "editar");
        contentPanel.add(new BuscarHashtag(Log), "buscar");

        toolbarPanel.add(barraIG, BorderLayout.WEST);
        toolbarPanel.add(contentPanel, BorderLayout.CENTER);

        mainPanel.add(toolbarPanel, "toolbarPanel");
        mainPanel.add(new LogPanel(), "logPanel");

        add(mainPanel);

        setVisible(true);
    }

    private void setupButtonIG(JButton button, String iconPath) {
        button.setSize(40, 40);
        button.setBackground(Color.white);
        button.setBorderPainted(false);
        button.addActionListener(this);
        setImageLabel(button, iconPath);
        barraIG.add(button);
        barraIG.add(Box.createVerticalStrut(10));
    }
    
    private void setImageLabel(JButton name, String root) {
        ImageIcon image = new ImageIcon(root);
        Icon icon = new ImageIcon(image.getImage().getScaledInstance(name.getWidth(), name.getHeight(), Image.SCALE_DEFAULT));
        name.setIcon(icon);
        this.repaint();
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        CardLayout cardActual = (CardLayout) contentPanel.getLayout();
        CardLayout mainCardLayout = (CardLayout) mainPanel.getLayout();
        
        if (e.getSource() == perfilIG) {
            cardActual.show(contentPanel, "perfil");
        } else if (e.getSource() == comentar) {

        } else if (e.getSource() == editar) {
            cardActual.show(contentPanel, "editar");
        } else if (e.getSource() == search) {
            cardActual.show(contentPanel, "buscar");
        } else if (e.getSource() == cerrar) {
            int confirm = JOptionPane.showConfirmDialog(null, "¿Estás seguro de que deseas cerrar sesión?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                mainCardLayout.show(mainPanel, "logPanel");
            }
        }
    }
    
    class LogPanel extends JPanel {
        public LogPanel() {
            setLayout(new BorderLayout());   
            InstaLogin log = new InstaLogin();
            add(log, BorderLayout.CENTER);
            revalidate();
            repaint();
        }
    }
}
