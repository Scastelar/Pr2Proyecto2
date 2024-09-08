package insta;

import Insta.InstaLogin;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class InstagramPerfil extends JFrame {
    private JLabel perfilImg = new JLabel();
    private JTextField userTxt, datosTxt;
    private JPanel panelPosts;
    private JButton addPost;
    private int cantPost = 0, followers = 0, following = 0;
    //IgUser usuarioActual = IgCuentas.getInstance().getUsuario();
    InstaLogin Log;

    public InstagramPerfil(InstaLogin Log) {
        this.Log = Log; 
        setTitle("Instagram Profile");
        setSize(730, 480);  
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBackground(Color.white);
        setLayout(new BorderLayout());

        JPanel profilePanel = new JPanel();
        profilePanel.setLayout(new BorderLayout());
        profilePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        profilePanel.setBackground(Color.WHITE);

        perfilImg.setBounds(300, 100, 130, 130);
        setImageLabel(perfilImg, "src\\imgs\\user.png");
        perfilImg.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
        profilePanel.add(perfilImg, BorderLayout.WEST);

        JPanel userInfoPanel = new JPanel();
        userInfoPanel.setLayout(new GridLayout(2, 1, 5, 5));
        userInfoPanel.setBackground(Color.WHITE);

        userTxt = new JTextField("Username");
        userTxt.setHorizontalAlignment(JTextField.CENTER);
        userTxt.setEditable(false);
        userTxt.setBorder(BorderFactory.createEmptyBorder());
        userTxt.setFont(new Font("Arial", Font.BOLD, 25));
        userTxt.setBackground(Color.white);
        //userTxt.setText(usuarioActual.getNombre());
        userInfoPanel.add(userTxt);

        datosTxt = new JTextField("Followers: 0  |  Following: 0  |  Posts: 0");
        datosTxt.setHorizontalAlignment(JTextField.CENTER);
        datosTxt.setEditable(false);
        datosTxt.setBorder(BorderFactory.createEmptyBorder());
        datosTxt.setFont(new Font("Arial", Font.PLAIN, 16));
        datosTxt.setBackground(Color.white);
        //datosTxt.setText("Followers: "+usuarioActual.getFollowers()+"  |  Following: "+usuarioActual.getFollowing()+"  |  Posts: 0");
        userInfoPanel.add(datosTxt);

        profilePanel.add(userInfoPanel, BorderLayout.CENTER);

        add(profilePanel, BorderLayout.NORTH);

        panelPosts = new JPanel();
        panelPosts.setLayout(new GridBagLayout());
        panelPosts.setBackground(Color.WHITE);
        panelPosts.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JScrollPane scrollPane = new JScrollPane(panelPosts);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane, BorderLayout.CENTER);
        
        addPost = new JButton("Add Post");
        addPost.setBackground(Color.pink);
        addPost.setForeground(Color.white);
        addPost.setFont(new Font("Arial", Font.BOLD, 17));
        addPost.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addPost();
            }
        });

        profilePanel.add(addPost, BorderLayout.EAST);
    }

    private void addPost() {
        if (cantPost < 9) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Select an Image to Upload");
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int result = fileChooser.showOpenDialog(this);

            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                String imagePath = selectedFile.getAbsolutePath();

                JButton postButton = new JButton();
                setImageButton(postButton, imagePath);  

                postButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int confirm = JOptionPane.showConfirmDialog(
                                null, "Desea borrar este post?",
                                "Delete Post", JOptionPane.YES_NO_OPTION);

                        if (confirm == JOptionPane.YES_OPTION) {
                            panelPosts.remove(postButton);
                            panelPosts.revalidate();
                            panelPosts.repaint();
                            cantPost--;
                            datosTxt.setText("Followers: " + followers + "  |  Following: " + following + "  |  Posts: " + cantPost);
                        }
                    }
                });

                GridBagConstraints gbc = new GridBagConstraints();
                gbc.gridx = cantPost % 3;
                gbc.gridy = cantPost / 3; 
                gbc.insets = new Insets(10, 10, 10, 10); 
                gbc.anchor = GridBagConstraints.CENTER;
                panelPosts.add(postButton, gbc);

                panelPosts.revalidate();
                panelPosts.repaint();

                cantPost++;
                datosTxt.setText("Followers: " + followers + "  |  Following: " + following + "  |  Posts: " + cantPost);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Hay un maximo de 9 posts");
        }
    }
    
    private void setImageLabel(JLabel labelName, String root) {
        ImageIcon image = new ImageIcon(root);
        Icon icon = new ImageIcon(image.getImage().getScaledInstance(labelName.getWidth(), labelName.getHeight(), Image.SCALE_DEFAULT));
        labelName.setIcon(icon);
        this.repaint();
    }

    private void setImageButton(JButton button, String root) {
        ImageIcon image = new ImageIcon(root);
        Icon icon = new ImageIcon(image.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH));
        button.setIcon(icon);
        button.setPreferredSize(new Dimension(300, 300));
        button.setBorder(BorderFactory.createEmptyBorder()); 
        button.setContentAreaFilled(false);
    }
}

   
