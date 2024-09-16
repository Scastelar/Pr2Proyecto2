package proyecto2;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class ReproductorMusical extends JPanel {
    private UserManager userManager;
    private User currentUser;
    private Player player;
    private FileInputStream fileInputStream;
    private long pauseLocation;
    private long totalLength;
    private JButton playButton, stopButton, pauseButton, prevButton, nextButton, addButton, removeButton, upButton, downButton;
    private JSlider progressSlider;
    private boolean isPaused;
    private int currentIndex = -1;
    private DefaultListModel<String> listModel;
    private JList<String> songList;
    private ArrayList<String> filePaths;
    private Timer timer; // Para actualizar el progreso de la canción
    private final String filePath = "canciones.txt"; // Archivo donde se guardan las canciones
    private JLabel nowPlayingLabel;

    public ReproductorMusical(UserManager userManager) {
        this.userManager = userManager;
        this.currentUser = userManager.getCurrentUser();
        // Configuración básica de la ventana
        setSize(800, 500);
        setLayout(new GridBagLayout());
        setBackground(new Color(45, 45, 45));  // Fondo oscuro elegante

        // Panel para los botones de reproducción
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        controlPanel.setBackground(new Color(45, 45, 45));

        // Inicializar los botones de control con símbolos Unicode
        playButton = crearBotonConIcono("\u25B6");   // ▶
        pauseButton = crearBotonConIcono("\u23F8");  // ⏸
        stopButton = crearBotonConIcono("\u23F9");   // ⏹
        prevButton = crearBotonConIcono("\u23EE");   // ⏮
        nextButton = crearBotonConIcono("\u23ED");   // ⏭

        // Añadir botones al panel
        controlPanel.add(prevButton);
        controlPanel.add(playButton);
        controlPanel.add(pauseButton);
        controlPanel.add(stopButton);
        controlPanel.add(nextButton);

        // Slider de progreso
        progressSlider = new JSlider(0, 100, 0);
        progressSlider.setPreferredSize(new Dimension(600, 20));
        progressSlider.setBackground(new Color(45, 45, 45));
        progressSlider.setForeground(new Color(200, 200, 200));
        progressSlider.setUI(new CustomSliderUI(progressSlider));

        // Etiqueta "Now Playing"
        nowPlayingLabel = new JLabel("Now Playing: ");
        nowPlayingLabel.setForeground(Color.WHITE);
        nowPlayingLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        // Lista de canciones
        listModel = new DefaultListModel<>();
        songList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(songList);
        scrollPane.setPreferredSize(new Dimension(600, 150));

        // Estilo de la lista de canciones
        songList.setBackground(new Color(55, 55, 55));
        songList.setForeground(Color.WHITE);
        songList.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        songList.setSelectionBackground(new Color(70, 130, 180)); // Azul elegante para la selección
        songList.setBorder(BorderFactory.createEmptyBorder());

        // Panel para botones de lista
        JPanel buttonPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        buttonPanel.setBackground(new Color(45, 45, 45));
        addButton = crearBotonEstilizado("ADD");
        removeButton = crearBotonEstilizado("REMOVE");
        upButton = crearBotonEstilizado("UP");
        downButton = crearBotonEstilizado("DOWN");
        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(upButton);
        buttonPanel.add(downButton);

        // Layout y posiciones de los elementos
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Posicionar el slider de progreso
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        add(progressSlider, gbc);

        // Añadir el panel de control de reproducción
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 4;
        gbc.fill = GridBagConstraints.NONE;
        add(controlPanel, gbc);

        // Añadir la lista de canciones
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 4;
        gbc.fill = GridBagConstraints.BOTH;
        add(scrollPane, gbc);

        // Añadir el panel de botones de la lista
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 4;
        add(buttonPanel, gbc);

        // Etiqueta de "Now Playing"
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 4;
        add(nowPlayingLabel, gbc);

        // Inicializar lista de canciones
        filePaths = new ArrayList<>();
        cargarCancionesGuardadas();  // Cargar canciones desde un archivo

        // Añadir funcionalidad a los botones
        playButton.addActionListener(e -> reproducirCancion());
        pauseButton.addActionListener(e -> pausarCancion());
        stopButton.addActionListener(e -> detenerReproduccion());
        prevButton.addActionListener(e -> cancionAnterior());
        nextButton.addActionListener(e -> siguienteCancion());

        addButton.addActionListener(e -> agregarCancion());
        removeButton.addActionListener(e -> eliminarCancion());
        upButton.addActionListener(e -> moverCancionArriba());
        downButton.addActionListener(e -> moverCancionAbajo());

        // Añadir listener para seleccionar y reproducir canción de la lista
        songList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selected = songList.getSelectedIndex();
                    if (selected != -1 && selected != currentIndex) {
                        currentIndex = selected;
                        detenerReproduccion();
                        reproducirCancion();
                    }
                }
            }
        });

        setVisible(true);
    }

    // Método para crear botones con símbolos Unicode
    private JButton crearBotonConIcono(String texto) {
        JButton boton = new JButton(texto);
        boton.setFocusPainted(false);
        boton.setForeground(Color.WHITE);
        boton.setBackground(new Color(70, 70, 70));
        boton.setFont(new Font("Segoe UI Symbol", Font.BOLD, 24)); // Fuente que soporta símbolos Unicode
        boton.setPreferredSize(new Dimension(60, 60)); // Tamaño adecuado
        boton.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setOpaque(true);
        boton.setBorderPainted(false);

        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(new Color(90, 90, 90));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(new Color(70, 70, 70));
            }
        });

        return boton;
    }

    // Método para crear botones estilizados
    private JButton crearBotonEstilizado(String texto) {
        JButton boton = new JButton(texto);
        boton.setFocusPainted(false);
        boton.setForeground(Color.WHITE);
        boton.setBackground(new Color(70, 70, 70));
        boton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        boton.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setOpaque(true);
        boton.setBorderPainted(false);

        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(new Color(90, 90, 90));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(new Color(70, 70, 70));
            }
        });

        return boton;
    }

    // Método para reproducir la canción seleccionada o reanudar si está en pausa
    private void reproducirCancion() {
        try {
            if (currentIndex != -1 && currentIndex < filePaths.size()) {
                File selectedFile = new File(filePaths.get(currentIndex));
                if (!selectedFile.exists()) {
                    JOptionPane.showMessageDialog(this, "El archivo no existe:\n" + selectedFile.getAbsolutePath(), "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (isPaused && player != null) {
                    // Reanudar desde la ubicación de pausa
                    fileInputStream = new FileInputStream(selectedFile);

                    // Asegurarse de que pauseLocation no sea mayor que totalLength y nunca sea negativo
                    long skipBytes = totalLength - pauseLocation;
                    if (skipBytes < 0) {
                        skipBytes = 0;
                    }

                    fileInputStream.skip(skipBytes); // Saltar a la posición de pausa
                    player = new Player(fileInputStream);
                    isPaused = false;
                    nowPlayingLabel.setText("Now Playing: " + selectedFile.getName());

                    // Crear un hilo para continuar la reproducción
                    new Thread(() -> {
                        try {
                            if (player != null) {
                                player.play();
                                while (player != null && !player.isComplete()) {
                                    Thread.sleep(1000); // Esperar un segundo antes de verificar el estado de reproducción
                                }
                                if (player != null && player.isComplete()) {
                                    siguienteCancion();  // Reproduce la siguiente canción cuando se complete
                                }
                            }
                        } catch (JavaLayerException | InterruptedException e) {
                            e.printStackTrace();
                        }
                    }).start();

                    iniciarTemporizador(); // Reanudar el temporizador
                } else {
                    // Si no está en pausa, iniciamos una nueva reproducción
                    detenerReproduccion();  // Detener cualquier reproducción en curso
                    fileInputStream = new FileInputStream(selectedFile); // Reiniciamos el flujo de entrada
                    totalLength = fileInputStream.available();
                    player = new Player(fileInputStream);
                    isPaused = false;
                    nowPlayingLabel.setText("Now Playing: " + selectedFile.getName());

                    new Thread(() -> {
                        try {
                            if (player != null) {
                                player.play();
                                while (player != null && !player.isComplete()) {
                                    Thread.sleep(1000);
                                }
                                if (player != null && player.isComplete()) {
                                    siguienteCancion();
                                }
                            }
                        } catch (JavaLayerException | InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    }).start();

                    iniciarTemporizador();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Método para pausar la canción
    private void pausarCancion() {
        if (player != null) {
            try {
                pauseLocation = fileInputStream.available();
                player.close();
                isPaused = true;
                detenerTemporizador();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Método para detener (reiniciar) la reproducción
    private void detenerReproduccion() {
        if (player != null) {
            try {
                // Si el flujo aún está abierto, lo cerramos
                if (fileInputStream != null) {
                    fileInputStream.close(); // Cerramos el flujo de entrada
                }
                player.close(); // Cerramos el reproductor
                player = null; // Reiniciar el reproductor para evitar problemas
                pauseLocation = 0; // Reiniciamos la posición de pausa
                progressSlider.setValue(0); // Reseteamos la barra de progreso a 0
                nowPlayingLabel.setText("Now Playing: "); // Restablecemos la etiqueta
                detenerTemporizador(); // Detenemos el temporizador de progreso
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Método para reproducir la canción anterior
    private void cancionAnterior() {
        if (currentIndex > 0) {
            currentIndex--;
            detenerReproduccion();
            reproducirCancion();
            songList.setSelectedIndex(currentIndex);
        }
    }

    // Método para reproducir la siguiente canción
    private void siguienteCancion() {
        if (currentIndex < filePaths.size() - 1) {
            currentIndex++;
            detenerReproduccion();
            reproducirCancion();
            songList.setSelectedIndex(currentIndex);
        } else {
            detenerReproduccion(); // Detener si no hay más canciones
        }
    }

    // Método para agregar una canción
    private void agregarCancion() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Archivos MP3", "mp3"));
        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();
            String rutaArchivo = archivo.getAbsolutePath();

            // Verificar si la canción ya está en la lista
            if (filePaths.contains(rutaArchivo)) {
                JOptionPane.showMessageDialog(this, "La canción ya está en la lista.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            } else {
                filePaths.add(rutaArchivo);
                listModel.addElement(archivo.getName());
                guardarCanciones();  // Guardar las canciones en el archivo de texto
            }
        }
    }

    // Método para eliminar una canción
    private void eliminarCancion() {
        int index = songList.getSelectedIndex();
        if (index != -1) {
            filePaths.remove(index);
            listModel.remove(index);
            guardarCanciones();
            if (currentIndex == index) {
                detenerReproduccion();
            }
            if (currentIndex > index) {
                currentIndex--;
            }
        }
    }

    // Método para mover la canción hacia arriba
    private void moverCancionArriba() {
        int index = songList.getSelectedIndex();
        if (index > 0) {
            String song = listModel.remove(index);
            listModel.add(index - 1, song);

            String path = filePaths.remove(index);
            filePaths.add(index - 1, path);

            songList.setSelectedIndex(index - 1);
            guardarCanciones();
        }
    }

    // Método para mover la canción hacia abajo
    private void moverCancionAbajo() {
        int index = songList.getSelectedIndex();
        if (index < listModel.size() - 1) {
            String song = listModel.remove(index);
            listModel.add(index + 1, song);

            String path = filePaths.remove(index);
            filePaths.add(index + 1, path);

            songList.setSelectedIndex(index + 1);
            guardarCanciones();
        }
    }

    // Método para guardar canciones en un archivo de texto con codificación UTF-8
    private void guardarCanciones() {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), StandardCharsets.UTF_8))) {
            for (String ruta : filePaths) {
                writer.write(ruta);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método para cargar canciones desde un archivo con codificación UTF-8
    private void cargarCancionesGuardadas() {
        try {
            File archivo = new File(filePath);
            if (archivo.exists()) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(archivo), StandardCharsets.UTF_8));
                String linea;
                while ((linea = reader.readLine()) != null) {
                    filePaths.add(linea);
                    listModel.addElement(new File(linea).getName());
                }
                reader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método para iniciar el temporizador que actualiza el progreso
    private void iniciarTemporizador() {
        timer = new Timer(1000, e -> {
            if (fileInputStream != null && !isPaused) {
                try {
                    long currentProgress = totalLength - fileInputStream.available();
                    int progressPercentage = (int) ((double) currentProgress / totalLength * 100);
                    progressSlider.setValue(progressPercentage);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        timer.start();
    }

    // Método para detener el temporizador
    private void detenerTemporizador() {
        if (timer != null) {
            timer.stop();
        }
    }

    // Clase personalizada para estilizar el slider
    class CustomSliderUI extends javax.swing.plaf.basic.BasicSliderUI {

        public CustomSliderUI(JSlider slider) {
            super(slider);
        }

        @Override
        public void paintTrack(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(new Color(70, 70, 70));
            g2d.fillRoundRect(trackRect.x, trackRect.y + trackRect.height / 2 - 2, trackRect.width, 4, 2, 2);
        }

        @Override
        public void paintThumb(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(new Color(70, 130, 180));
            g2d.fillOval(thumbRect.x - 6, thumbRect.y + thumbRect.height / 2 - 6, 12, 12);
        }

        @Override
        public void paintFocus(Graphics g) {
            // No pintar el foco
        }
    }

}
