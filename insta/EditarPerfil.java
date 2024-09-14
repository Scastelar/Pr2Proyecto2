/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package insta;

import java.awt.GridLayout;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class EditarPerfil extends JPanel {
    private InstaLogin Log;

    public EditarPerfil(InstaLogin Log) {
        this.Log=Log;
        setSize(400, 300);
        setLayout(new GridLayout(4, 1));

        JButton btnBuscarPersonas = new JButton("Buscar Personas");
        JButton btnSeguir = new JButton("Seguir/Dejar de Seguir");
        JButton btnActivarDesactivar = new JButton("Activar/Desactivar Cuenta");

        btnBuscarPersonas.addActionListener(e -> buscarPersonas());
        btnSeguir.addActionListener(e -> seguirDejarSeguir());
        btnActivarDesactivar.addActionListener(e -> activarDesactivarCuenta());

        add(btnBuscarPersonas);
        add(btnSeguir);
        add(btnActivarDesactivar);
    }

    private void buscarPersonas() {

    }

    private void seguirDejarSeguir() {

    }

    private void activarDesactivarCuenta() {
        if (Log.cuentas.getUsuario().isActivo()) {
            int confirm = JOptionPane.showConfirmDialog(null, "¿Deseas desactivar tu cuenta?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                Log.cuentas.getUsuario().setEstado(false);
                actualizarUsuario(Log.cuentas.getUsuario());
                System.out.println("Cuenta desactivada.");
            }
        } else {
            Log.cuentas.getUsuario().setEstado(true);
            actualizarUsuario(Log.cuentas.getUsuario());
            System.out.println("Cuenta activada.");
        }
    }

    private void actualizarUsuario(IgUser usuario) {
        List<IgUser> usuarios = IgCuentas.leerUsuarios();
        for (IgUser u : usuarios) {
            if (u.getUsername().equals(usuario.getUsername())) {
                u.setEstado(usuario.isActivo());
            }
        }
        IgCuentas.escribirUsuarios(usuarios);
    }
}

