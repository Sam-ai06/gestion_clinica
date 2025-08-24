package com.espol.proyectodb3;

import SQL.DatabaseConnection;
import entidades.Cita;
import entidades.Cliente;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class DoctorController implements Initializable {
    @FXML
    private TableView<Cita> tableAppointments;
    @FXML
    private TableColumn<Cita, String> colDate, colPatient, colReason, colStatus, colActions;
    @FXML
    private Button btnAppointments, btnPatients, btnPrescriptions, btnLogout;
    @FXML
    private Label lbl_msg;

    private ObservableList<Cita> appointmentsList = FXCollections.observableArrayList();
    private DatabaseConnection connection;

    private String cedulaDoctor;

    public void setCedulaDoctor(String cedulaDoctor) {
        this.cedulaDoctor = cedulaDoctor;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        colDate.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colPatient.setCellValueFactory(new PropertyValueFactory<>("paciente"));
        colReason.setCellValueFactory(new PropertyValueFactory<>("motivo"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colActions.setCellValueFactory(new PropertyValueFactory<>("acciones"));

        // Cargar datos desde la base de datos
        loadAppointmentsData();


        tableAppointments.setItems(appointmentsList);
    }

    private void loadAppointmentsData() {
        try {
            Connection con = DatabaseConnection.getConnection();
            String sql = "SELECT c.id, c.fecha, c.hora, p.nombre, p.apellido, c.descripcion, c.estado "
                    + "FROM citas c JOIN personas p ON c.cedula_cliente = p.cedula "
                    + "WHERE c.cedula_doctor = ? " // <-- Filtra por el doctor
                    + "ORDER BY c.fecha, c.hora";
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, cedulaDoctor); // <-- Asigna la cédula del doctor
            ResultSet set = statement.executeQuery();
            appointmentsList.clear();

            while (set.next()) {
                String fecha = set.getDate("fecha").toString() + " " + set.getTime("hora").toString();
                String paciente = set.getString("nombre") + " " + set.getString("apellido");
                String motivo = set.getString("descripcion");
                String estado = set.getString("estado");

                appointmentsList.add(new Cita(fecha, paciente, motivo, estado, "Ver detalles"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void handleCitas(ActionEvent event) {
        loadAppointmentsData();
    }

    @FXML
    public void handlePacientes() {
        // Cambia la vista a la tabla de pacientes
        try {
            Parent root = FXMLLoader.load(getClass().getResource("Pacientes-view.fxml"));
            Stage stage = (Stage) btnPatients.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
            stage.centerOnScreen();
        } catch (IOException e) {
            lbl_msg.setText("Error al mostrar pacientes.");
            e.printStackTrace();
        }
    }

    @FXML
    public void handleRecetas() {
        // Cambia la vista a la tabla de recetas enviadas
        try {
            Parent root = FXMLLoader.load(getClass().getResource("Recetas-view.fxml"));
            Stage stage = (Stage) btnPrescriptions.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
            stage.centerOnScreen();
        } catch (IOException e) {
            lbl_msg.setText("Error al mostrar recetas.");
            e.printStackTrace();
        }
    }

    @FXML
    public void handleLogOut(ActionEvent event) {
        try {
            lbl_msg.setText("Cerrando sesión...");
            PauseTransition delay = new PauseTransition(Duration.seconds(2));
            delay.setOnFinished(e -> {
                try {
                    Parent root = FXMLLoader.load(getClass().getResource("Main-view.fxml"));
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.show();
                    stage.centerOnScreen();

                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });
            delay.play();


        } catch (Exception e) {
            e.printStackTrace();
            lbl_msg.setText("Error al cerrar sesión.");
        }
    }

    public void initializeLabel(String text) {
    }
}

