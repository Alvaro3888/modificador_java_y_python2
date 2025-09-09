import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class FormularioJava extends JFrame {
    private JTextField txtId, txtNombre, txtApellido, txtEdad;
    private JButton btnModificar;

    public  FormularioJava() {
        setTitle("Modificar Alumno");
        setSize(350, 250);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(5, 2, 5, 5));

        // Labels y campos
        add(new JLabel("ID del alumno:"));
        txtId = new JTextField();
        add(txtId);

        add(new JLabel("Nombre:"));
        txtNombre = new JTextField();
        add(txtNombre);

        add(new JLabel("Apellido:"));
        txtApellido = new JTextField();
        add(txtApellido);

        add(new JLabel("Edad:"));
        txtEdad = new JTextField();
        add(txtEdad);

        // Botón
        btnModificar = new JButton("Modificar");
        add(btnModificar);
        add(new JLabel()); // Espacio vacío

        btnModificar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                modificarAlumno();
            }
        });

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void modificarAlumno() {
        String id = txtId.getText().trim();
        String nombre = txtNombre.getText().trim();
        String apellido = txtApellido.getText().trim();
        String edadStr = txtEdad.getText().trim();

        if (id.isEmpty() || nombre.isEmpty() || apellido.isEmpty() || edadStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.");
            return;
        }

        try {
            int edad = Integer.parseInt(edadStr);

            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/escuela?useSSL=false&serverTimezone=UTC",
                "root", ""
            );

            String sql = "UPDATE alumno SET nombre = ?, apellido = ?, edad = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nombre);
            stmt.setString(2, apellido);
            stmt.setInt(3, edad);
            stmt.setInt(4, Integer.parseInt(id));

            int filas = stmt.executeUpdate();

            if (filas > 0) {
                JOptionPane.showMessageDialog(this, "Alumno modificado correctamente.");
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró el alumno con ese ID.");
            }

            conn.close();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Edad e ID deben ser números.");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al modificar: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        new  FormularioJava();
    }
}