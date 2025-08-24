package com.espol.proyectodb3;

import SQL.DatabaseConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class NuevaCitaController {
    @FXML
    private DatePicker datePicker;
    @FXML
    private ComboBox<String> cbSpecialty;
    @FXML
    private ComboBox<String> cbDoctor;
    @FXML
    private TextArea taDescription;


    @FXML
    public void initialize() {
        // Ejemplo de llenado de ComboBox de especialidades.
        // Se debe obtener de la base de datos o de un listado predefinido.
        cbSpecialty.getItems().addAll("Cardiología", "Dermatología", "Pediatría");
    }


    @FXML
    public void handleSave(ActionEvent event) {
        LocalDate fecha = datePicker.getValue();
        String especialidad = cbSpecialty.getValue();
        String doctor = cbDoctor.getValue();
        String descripcion = taDescription.getText();

        // Aquí debes obtener el ID del cliente logeado para la cita.
        //valor de ejemplo
        String cedulaCliente = "ejemploCedulaCliente";

        // Aquí debes obtener el ID del doctor seleccionado.
        //valor de ejemplo

        String cedulaDoctor = "ejemploCedulaDoctor";

        if (fecha == null || especialidad == null || doctor == null || descripcion.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Por favor, complete todos los campos.");
            return;
        }

        String sql = "INSERT INTO citas (fecha, hora, cedula_cliente, cedula_doctor, descripcion, estado) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, fecha.toString());
            pstmt.setString(2, "08:00:00");
            pstmt.setString(3, cedulaCliente);
            pstmt.setString(4, cedulaDoctor);
            pstmt.setString(5, descripcion);
            pstmt.setString(6, "Pendiente");

            pstmt.executeUpdate();
            showAlert(Alert.AlertType.INFORMATION, "Éxito", "Cita guardada con éxito.");

            handleCancel(event);

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Error al guardar la cita en la base de datos.");
        }
    }

    @FXML
    public void handleCancel(ActionEvent event) {
        try {
            // Cerrar
            javafx.scene.Node source = (javafx.scene.Node) event.getSource();
            javafx.stage.Stage stage = (javafx.stage.Stage) source.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al cerrar la ventana de nueva cita.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}