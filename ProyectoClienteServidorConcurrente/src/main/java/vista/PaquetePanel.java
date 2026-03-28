package vista;

import Dominio.Paquete;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class PaquetePanel extends JPanel {

    private final PaqueteController controller = new PaqueteController();
    private final DefaultListModel<String> modeloLista = new DefaultListModel<>();

    public PaquetePanel() {
        setLayout(new BorderLayout());

        // Panel superior con campos
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField tfId = new JTextField(10);
        JTextField tfDestino = new JTextField(15);
        JButton btnAgregar = new JButton("Agregar Paquete");

        top.add(new JLabel("ID"));
        top.add(tfId);
        top.add(new JLabel("Destino"));
        top.add(tfDestino);
        top.add(btnAgregar);

        add(top, BorderLayout.NORTH);

        // Lista de paquetes
        JList<String> lstPaquetes = new JList<>(modeloLista);
        JScrollPane scroll = new JScrollPane(lstPaquetes);
        add(scroll, BorderLayout.CENTER);

        // Acción de agregar
        btnAgregar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = tfId.getText().trim();
                String destino = tfDestino.getText().trim();
                if (!id.isEmpty() && !destino.isEmpty()) {
                    Paquete p = new Paquete(id, destino);
                    controller.addPaquete(p);
                    refreshLista();
                    tfId.setText("");
                    tfDestino.setText("");
                }
            }
        });

        // Cargar datos inicial (opcional)
        refreshLista();
    }

    private void refreshLista() {
        List<Paquete> lista = controller.getPaquetes();
        modeloLista.clear();
        for (Paquete p : lista) {
            modeloLista.addElement(p.toString());
        }
    }
}