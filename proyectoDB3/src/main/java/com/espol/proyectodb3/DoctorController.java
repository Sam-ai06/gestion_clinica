package com.espol.proyectodb3;

import SQL.DatabaseConnection;
import entidades.Cita;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class DoctorController implements Initializable {
    @FXML private TableView<Cita> tableAppointments;
    @FXML private TableColumn<Cita, String> colDate, colPatient, colReason, colStatus, colActions;
    @FXML private Button btnAppointments, btnPatients, btnPrescriptions, btnLogout;

    private ObservableList<Cita> appointmentsList = FXCollections.observableArrayList();
    private DatabaseConnection connection;

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
        try{
            Connection con = DatabaseConnection.getConnection();
            String sql = "SELECT c.id, c.fecha, c.hora, p.nombre, p.apellido, c.descripcion, c.estado "
                    + "FROM citas c " + "JOIN personas p ON c.cedula_cliente = p.cedula "
                    + "ORDER BY c.fecha, c.hora";
            PreparedStatement statement = con.prepareStatement(sql);
            ResultSet set = statement.executeQuery();
            appointmentsList.clear();

            while (set.next()){
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


    // Métodos de inicialización y manejo de eventos
}