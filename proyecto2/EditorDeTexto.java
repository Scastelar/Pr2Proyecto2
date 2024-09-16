package proyecto2;


import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.*;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class EditorDeTexto extends JPanel {
    private UserManager userManager;
    private User currentUser;
    private JTextPane textPane;
    private JComboBox<String> fontComboBox;
    private JComboBox<Integer> sizeComboBox;
    private JComboBox<Float> lineSpacingComboBox; // ComboBox para interlineado
    private DefaultStyledDocument doc;
    private UndoManager undoManager;
    private JPanel colorPanel;
    private ArrayList<Color> selectedColors;

    public EditorDeTexto(UserManager userManager) {
        this.userManager = userManager;
        this.currentUser = userManager.getCurrentUser();
        setSize(900, 800);
        undoManager = new UndoManager();
        selectedColors = new ArrayList<>();
        initComponents();
    }

    private void initComponents() {
        // Área de texto
        textPane = new JTextPane();
        doc = new DefaultStyledDocument();
        textPane.setDocument(doc);
        textPane.getDocument().addUndoableEditListener(undoManager);
        textPane.setFont(new Font("Arial", Font.PLAIN, 18));  // Fuente moderna y clara
        JScrollPane scrollPane = new JScrollPane(textPane);

        // Barra de herramientas
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setBackground(new Color(245, 245, 245)); // Fondo gris claro

        // Botones estilizados con fondo blanco
        JButton cutButton = createStyledButton("Cortar", e -> new DefaultEditorKit.CutAction().actionPerformed(e));
        JButton copyButton = createStyledButton("Copiar", e -> new DefaultEditorKit.CopyAction().actionPerformed(e));
        JButton pasteButton = createStyledButton("Pegar", e -> new DefaultEditorKit.PasteAction().actionPerformed(e));

        JButton undoButton = createStyledButton("Deshacer", e -> {
            if (undoManager.canUndo()) {
                undoManager.undo();
            }
        });

        JButton redoButton = createStyledButton("Rehacer", e -> {
            if (undoManager.canRedo()) {
                undoManager.redo();
            }
        });

        // Botones de formato (negrita, cursiva, subrayado) con fondo blanco
        JButton boldButton = createStyledButton("B", e -> {
            Action boldAction = new StyledEditorKit.BoldAction();
            boldAction.actionPerformed(e);
        });
        boldButton.setFont(new Font("Arial", Font.BOLD, 18));

        JButton italicButton = createStyledButton("I", e -> {
            Action italicAction = new StyledEditorKit.ItalicAction();
            italicAction.actionPerformed(e);
        });
        italicButton.setFont(new Font("Arial", Font.ITALIC, 18));

        JButton underlineButton = createStyledButton("U", e -> {
            Action underlineAction = new StyledEditorKit.UnderlineAction();
            underlineAction.actionPerformed(e);
        });
        underlineButton.setFont(new Font("Arial", Font.PLAIN, 18));

        // Botones de alineación con fondo blanco
        JButton alignLeftButton = createStyledButton("Izquierda", e -> {
            Action alignLeftAction = new StyledEditorKit.AlignmentAction("Izquierda", StyleConstants.ALIGN_LEFT);
            alignLeftAction.actionPerformed(e);
        });

        JButton alignCenterButton = createStyledButton("Centro", e -> {
            Action alignCenterAction = new StyledEditorKit.AlignmentAction("Centro", StyleConstants.ALIGN_CENTER);
            alignCenterAction.actionPerformed(e);
        });

        JButton alignRightButton = createStyledButton("Derecha", e -> {
            Action alignRightAction = new StyledEditorKit.AlignmentAction("Derecha", StyleConstants.ALIGN_RIGHT);
            alignRightAction.actionPerformed(e);
        });

        JButton justifyButton = createStyledButton("Justificar", e -> {
            Action justifyAction = new StyledEditorKit.AlignmentAction("Justificar", StyleConstants.ALIGN_JUSTIFIED);
            justifyAction.actionPerformed(e);
        });

        // ComboBox para fuentes
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String[] fonts = ge.getAvailableFontFamilyNames();
        fontComboBox = new JComboBox<>(fonts);
        fontComboBox.setSelectedItem("Arial");
        fontComboBox.setMaximumSize(new Dimension(200, 30));
        fontComboBox.addActionListener(e -> setFontStyle());

        // ComboBox para tamaños
        Integer[] sizes = {12, 16, 20, 24, 32, 48, 64};
        sizeComboBox = new JComboBox<>(sizes);
        sizeComboBox.setSelectedItem(18);
        sizeComboBox.setMaximumSize(new Dimension(80, 30));
        sizeComboBox.addActionListener(e -> setFontStyle());

        // ComboBox para interlineado
        Float[] lineSpacings = {1.0f, 1.5f, 2.0f}; // Valores de interlineado
        lineSpacingComboBox = new JComboBox<>(lineSpacings);
        lineSpacingComboBox.setSelectedItem(1.0f);  // Interlineado simple por defecto
        lineSpacingComboBox.setMaximumSize(new Dimension(100, 30));
        lineSpacingComboBox.addActionListener(e -> setLineSpacing());

        // Panel de colores
        colorPanel = new JPanel(new GridLayout(1, 8, 2, 2));
        colorPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        colorPanel.setPreferredSize(new Dimension(300, 50));

        // Botón para elegir color con fondo blanco
        JButton colorChooserButton = createStyledButton("Color", e -> elegirColor());

        // Añadir componentes a la barra de herramientas
        toolBar.add(cutButton);
        toolBar.add(copyButton);
        toolBar.add(pasteButton);
        toolBar.add(undoButton);
        toolBar.add(redoButton);
        toolBar.addSeparator();

        toolBar.add(boldButton);
        toolBar.add(italicButton);
        toolBar.add(underlineButton);
        toolBar.addSeparator();

        toolBar.add(alignLeftButton);
        toolBar.add(alignCenterButton);
        toolBar.add(alignRightButton);
        toolBar.add(justifyButton);
        toolBar.addSeparator();

        toolBar.add(new JLabel("Fuente:"));
        toolBar.add(fontComboBox);

        toolBar.add(new JLabel("Tamaño:"));
        toolBar.add(sizeComboBox);

        toolBar.add(new JLabel("Interlineado:"));
        toolBar.add(lineSpacingComboBox);

        toolBar.add(Box.createHorizontalGlue());
        toolBar.add(new JLabel("Colores:"));
        toolBar.add(colorChooserButton);
        toolBar.add(colorPanel);

        // Panel inferior con los botones de Guardar y Nuevo
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        JButton saveButton = new JButton("Guardar");
        saveButton.setBackground(Color.WHITE); // Botón blanco
        saveButton.addActionListener(e -> guardarTexto()); // Acción para guardar el texto
        bottomPanel.add(saveButton);

        JButton newButton = new JButton("Nuevo");
        newButton.setBackground(Color.WHITE); // Botón blanco
        newButton.addActionListener(e -> nuevoDocumento()); // Acción para crear un nuevo documento
        bottomPanel.add(newButton);

        // Añadir componentes al JFrame
        add(toolBar, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    // Crear botones estilizados con fondo blanco
    private JButton createStyledButton(String text, ActionListener actionListener) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setBackground(Color.WHITE);  // Fondo blanco para todos los botones
        button.setForeground(Color.BLACK);  // Texto en negro para buen contraste
        button.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        button.setPreferredSize(new Dimension(90, 40));  // Tamaño uniforme
        button.addActionListener(actionListener);
        return button;
    }

    // Cambiar el interlineado
    private void setLineSpacing() {
        float selectedSpacing = (Float) lineSpacingComboBox.getSelectedItem();
        SimpleAttributeSet attributes = new SimpleAttributeSet();
        StyleConstants.setLineSpacing(attributes, selectedSpacing - 1.0f);  // Ajustar el valor para setLineSpacing
        doc.setParagraphAttributes(textPane.getSelectionStart(), textPane.getSelectionEnd() - textPane.getSelectionStart(), attributes, false);
    }

    // Elegir color
    private void elegirColor() {
        Color newColor = JColorChooser.showDialog(this, "Seleccionar Color", Color.BLACK);
        if (newColor != null) {
            selectedColors.add(newColor);
            actualizarPanelDeColores();
            setTextColor(newColor);
        }
    }

    private void actualizarPanelDeColores() {
        colorPanel.removeAll();
        for (Color color : selectedColors) {
            JButton colorButton = new JButton();
            colorButton.setBackground(color);
            colorButton.setPreferredSize(new Dimension(30, 30));
            colorButton.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            colorButton.addActionListener(e -> setTextColor(color));
            colorPanel.add(colorButton);
        }
        colorPanel.revalidate();
        colorPanel.repaint();
    }

    // Cambiar estilo de fuente
    private void setFontStyle() {
        String selectedFont = (String) fontComboBox.getSelectedItem();
        int selectedSize = (Integer) sizeComboBox.getSelectedItem();
        SimpleAttributeSet attributes = new SimpleAttributeSet();
        StyleConstants.setFontFamily(attributes, selectedFont);
        StyleConstants.setFontSize(attributes, selectedSize);
        doc.setCharacterAttributes(textPane.getSelectionStart(), textPane.getSelectionEnd() - textPane.getSelectionStart(), attributes, false);
    }

    // Cambiar color de texto
    private void setTextColor(Color color) {
        SimpleAttributeSet attributes = new SimpleAttributeSet();
        StyleConstants.setForeground(attributes, color);
        doc.setCharacterAttributes(textPane.getSelectionStart(), textPane.getSelectionEnd() - textPane.getSelectionStart(), attributes, false);
    }

    // Método para guardar el texto
    private void guardarTexto() {
        // Aquí puedes implementar la lógica para guardar el contenido del JTextPane
        JOptionPane.showMessageDialog(this, "El texto ha sido guardado.");
    }

    // Método para crear un nuevo documento
    private void nuevoDocumento() {
        textPane.setText("");  // Limpiar el JTextPane
        JOptionPane.showMessageDialog(this, "Nuevo documento creado.");
    }

    
}
