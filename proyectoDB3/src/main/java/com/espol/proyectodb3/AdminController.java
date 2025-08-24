package com.espol.proyectodb3;

import SQL.DatabaseConnection;
import entidades.Persona;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.*;

public class AdminController {

    @FXML
    private TableView<Persona> tableUsers;
    @FXML
    private TableColumn<Persona, String> colId, colName, colRole, colEmail;
    @FXML
    private Button btn_agregar, btn_logOut, btn_eliminar, btn_cargar;
    @FXML
    private TextField txtBuscarNombre, txtBuscarCorreo;
    @FXML
    private Label lbl_messageAdminPanel;

    private ObservableList<Persona> usuariosList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(data -> data.getValue().cedulaProperty());
        colName.setCellValueFactory(data -> data.getValue().nombreProperty());
        colEmail.setCellValueFactory(data -> data.getValue().correoProperty());
        colRole.setCellValueFactory(data -> data.getValue().rolProperty());

        cargarUsuarios(); // carga inicial
    }

    private void cargarUsuarios() {
        usuariosList.clear();
        String sql = "SELECT cedula, nombre, apellido, correo, rol FROM personas";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                usuariosList.add(new Persona(
                        rs.getString("cedula"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("correo"),
                        rs.getString("rol")
                ));
            }
            tableUsers.setItems(usuariosList);

        } catch (SQLException e) {
            e.printStackTrace();
            lbl_messageAdminPanel.setText("Error al cargar usuarios.");
        }
    }

    @FXML
    private void eliminarUsuarios() {
        String nombre = txtBuscarNombre.getText();
        String correo = txtBuscarCorreo.getText();

        if (nombre.isEmpty() && correo.isEmpty()) {
            lbl_messageAdminPanel.setText("Ingrese un nombre o correo para eliminar.");
            return;
        }

        String sql = "DELETE FROM personas WHERE nombre LIKE ? OR correo LIKE ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + nombre + "%");
            ps.setString(2, "%" + correo + "%");

            int filas = ps.executeUpdate();
            lbl_messageAdminPanel.setText("Usuarios eliminados: " + filas);

            cargarUsuarios(); // refrescar tabla

        } catch (SQLException e) {
            e.printStackTrace();
            lbl_messageAdminPanel.setText("Error al eliminar usuario.");
        }
    }

    // Método para registrar usuarios nuevos
    public boolean registerUser(String cedula, String nombre, String apellido, String edad,
                                String correo, String telefono, String direccion,
                                String usuario, String contrasena, String rol) {

        String sql = "INSERT INTO personas (cedula, nombre, apellido, edad, correo, telefono, direccion1, usuario, contrasena, rol) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, cedula);
            pstmt.setString(2, nombre);
            pstmt.setString(3, apellido);
            pstmt.setInt(4, Integer.parseInt(edad));
            pstmt.setString(5, correo);
            pstmt.setString(6, telefono);
            pstmt.setString(7, direccion);
            pstmt.setString(8, usuario);
            pstmt.setString(9, contrasena); // ideal: guardar hash usando tu Utileria
            pstmt.setString(10, rol);

            pstmt.executeUpdate();
            lbl_messageAdminPanel.setText("Usuario registrado con éxito.");
            cargarUsuarios();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            lbl_messageAdminPanel.setText("Error al registrar usuario: " + e.getMessage());
            return false;
        }
    }

    @FXML
    public void switchToRegisterScene(javafx.event.ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("RegistrarStaff-view.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Clinic data | agregar usuario");
        stage.centerOnScreen();
        stage.show();
    }

    @FXML
    public void handleLogOut(javafx.event.ActionEvent event) {
        try {
            lbl_messageAdminPanel.setText("Cerrando sesión...");
            PauseTransition delay = new PauseTransition(Duration.seconds(2));
            delay.setOnFinished(e -> {
                try {
                    Parent root = FXMLLoader.load(getClass().getResource("Main-view.fxml"));
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.centerOnScreen();
                    stage.show();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });
            delay.play();
        } catch (Exception e) {
            e.printStackTrace();
            lbl_messageAdminPanel.setText("Error al cerrar sesión.");
        }
    }
}
