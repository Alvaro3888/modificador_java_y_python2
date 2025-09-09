// Importamos las librerías necesarias
import javax.swing.*;       // Componentes gráficos (ventanas, botones, campos de texto)
import java.awt.*;          // Layouts y diseño visual
import java.awt.event.*;    // Eventos (como clics de botones)
import java.sql.*;          // Conexión y operaciones con bases de datos SQL

// Clase principal que extiende JFrame (ventana gráfica)
public class FormularioJava extends JFrame {

    // Declaramos los campos de texto y el botón como atributos
    private JTextField txtId, txtNombre, txtApellido, txtEdad;
    private JButton btnModificar;

    // Constructor: arma la interfaz gráfica
    public FormularioJava() {
        setTitle("Modificar Alumno");                    // Título de la ventana
        setSize(350, 250);                               // Tamaño de la ventana
        setDefaultCloseOperation(EXIT_ON_CLOSE);         // Cierra la app al cerrar la ventana
        setLayout(new GridLayout(5, 2, 5, 5));            // Diseño en forma de tabla (5 filas, 2 columnas)

        // Etiqueta y campo para el ID
        add(new JLabel("ID del alumno:"));
        txtId = new JTextField();
        add(txtId);

        // Etiqueta y campo para el nombre
        add(new JLabel("Nombre:"));
        txtNombre = new JTextField();
        add(txtNombre);

        // Etiqueta y campo para el apellido
        add(new JLabel("Apellido:"));
        txtApellido = new JTextField();
        add(txtApellido);

        // Etiqueta y campo para la edad
        add(new JLabel("Edad:"));
        txtEdad = new JTextField();
        add(txtEdad);

        // Botón para modificar
        btnModificar = new JButton("Modificar");
        add(btnModificar);
        add(new JLabel()); // Espacio vacío para completar la grilla

        // Acción del botón: llama al método modificarAlumno()
        btnModificar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                modificarAlumno();
            }
        });

        setLocationRelativeTo(null);  // Centra la ventana en la pantalla
        setVisible(true);             // Muestra la ventana
    }

    // Método que modifica el alumno en la base de datos
    private void modificarAlumno() {
        // Tomamos los valores ingresados
        String id = txtId.getText().trim();
        String nombre = txtNombre.getText().trim();
        String apellido = txtApellido.getText().trim();
        String edadStr = txtEdad.getText().trim();

        // Validamos que ningún campo esté vacío
        if (id.isEmpty() || nombre.isEmpty() || apellido.isEmpty() || edadStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.");
            return;
        }

        try {
            int edad = Integer.parseInt(edadStr);  // Convertimos edad a entero

            // Conectamos con la base de datos MySQL
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/escuela?useSSL=false&serverTimezone=UTC",
                "root", ""  // Usuario y contraseña (vacía si no configuraste una)
            );

            // Preparamos la consulta SQL
            String sql = "UPDATE alumno SET nombre = ?, apellido = ?, edad = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nombre);                   // Reemplaza el primer ?
            stmt.setString(2, apellido);                 // Reemplaza el segundo ?
            stmt.setInt(3, edad);                        // Reemplaza el tercero ?
            stmt.setInt(4, Integer.parseInt(id));        // Reemplaza el cuarto ?

            int filas = stmt.executeUpdate();            // Ejecuta la consulta y devuelve cuántas filas se modificaron

            // Mostramos el resultado
            if (filas > 0) {
                JOptionPane.showMessageDialog(this, "Alumno modificado correctamente.");
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró el alumno con ese ID.");
            }

            conn.close();  // Cerramos la conexión

        } catch (NumberFormatException ex) {
            // Si edad o ID no son números válidos
            JOptionPane.showMessageDialog(this, "Edad e ID deben ser números.");
        } catch (SQLException ex) {
            // Si hay un error con la base de datos
            JOptionPane.showMessageDialog(this, "Error al modificar: " + ex.getMessage());
        }
    }

    // Método principal: crea y muestra la ventana
    public static void main(String[] args) {
        new FormularioJava();
    }
}