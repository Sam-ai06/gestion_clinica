package SQL;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserValidations {

    //validación de usuario
    public static boolean validateUser(String username, String plainPassword, String role) {
        String sql = "SELECT contraseña FROM personas WHERE usuario = ? AND rol = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // Buscar el hash almacenado para ese usuario
            statement.setString(1, username);
            statement.setString(2, role);

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    String storedHash = result.getString("contraseña");
                    // Verificar si el password plano coincide con el hash almacenado
                    return BCrypt.checkpw(plainPassword, storedHash);
                }
                return false; // Usuario no encontrado
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void guardarRegistro(String cedula, String nombre, String apellido, int edad, String correo,
                                       String telefono, String direccion1, String usuario, String contrasena, String rol) {

        String hashedPassword = BCrypt.hashpw(contrasena, BCrypt.gensalt());
        Connection connection = null;

        try {
            connection = DatabaseConnection.getConnection();
            connection.setAutoCommit(false); // Iniciar transacción

            // 1. Insertar en tabla personas
            String sqlPersonas = "INSERT INTO personas (cedula, nombre, apellido, edad, correo, telefono, " +
                    "direccion1, usuario, contraseña, rol) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement stmtPersonas = connection.prepareStatement(sqlPersonas)) {
                stmtPersonas.setString(1, cedula);
                stmtPersonas.setString(2, nombre);
                stmtPersonas.setString(3, apellido);
                stmtPersonas.setInt(4, edad);
                stmtPersonas.setString(5, correo);
                stmtPersonas.setString(6, telefono);
                stmtPersonas.setString(7, direccion1);
                stmtPersonas.setString(8, usuario);
                stmtPersonas.setString(9, hashedPassword);
                stmtPersonas.setString(10, rol);
                stmtPersonas.executeUpdate();
            }

            switch (rol.toLowerCase()) {
                case "admin":
                    guardarAdmin(connection, cedula);
                    break;
                case "staff":
                    guardarDoctor(connection, cedula, "General");
                    break;
                case "enfermero":
                    guardarEnfermero(connection, cedula, "General");
                    break;
                case "cliente":
                    guardarCliente(connection, cedula);
                    break;
                default:
                    throw new SQLException("Rol no válido: " + rol);
            }

            connection.commit();
            System.out.println("Registro guardado exitosamente.");

        } catch (SQLException e) {
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            System.out.println("Error al guardar el registro: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.setAutoCommit(true);
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    //guardar en admins
    public static void guardarAdmin(Connection connection, String cedula) throws SQLException{
        String sql = "INSERT INTO administradores (cedula) VALUES (?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, cedula);
            statement.executeUpdate();
            System.out.println("Admin Guardado Exitosamente.");
        } catch (SQLException e) {
            System.out.println("Error al guardar el admin: " + e.getMessage());
            throw e;
        }

    }

    //guardar en doctores
    public static void guardarDoctor(Connection connection, String cedula, String especialidad) throws SQLException{
        String sql = "INSERT INTO doctores (cedula, especialidad) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, cedula);
            statement.setString(2, especialidad);
            statement.executeUpdate();
            System.out.println("Doctor Guardado Exitosamente.");
        } catch (SQLException e) {
            System.out.println("Error al guardar el doctor: " + e.getMessage());
            throw e;
        }
    }

    //guardar en enfermeros
    public static void guardarEnfermero(Connection connection, String cedula, String area) throws SQLException{
        String sql = "INSERT INTO enfermeros (cedula, area) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, cedula);
            statement.setString(2, area);
            statement.executeUpdate();
            System.out.println("Enfermero Guardado Exitosamente.");
        } catch (SQLException e) {
            System.out.println("Error al guardar el enfermero: " + e.getMessage());
            throw e;
        }
    }

    //guardar en clientes
    public static void guardarCliente(Connection connection, String cedula) throws SQLException{
        String sql = "INSERT INTO clientes (cedula, puede_agendar_citas) VALUES (?, TRUE)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, cedula);
            statement.executeUpdate();
            System.out.println("Cliente Guardado Exitosamente.");
        } catch (SQLException e) {
            System.out.println("Error al guardar el cliente: " + e.getMessage());
            throw e;
        }
    }

    //update, delete, insert(solo admins) aquí
}
