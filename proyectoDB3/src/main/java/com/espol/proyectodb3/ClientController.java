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
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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

public class ClientController {
    @FXML private TableView<?> tableMyAppointments;
    @FXML private TableColumn<?, ?> colDate, colDoctor, colSpecialty, colStatus, colActions;
    @FXML private Button btnMyAppointments, btnNewAppointment, btnMedicalHistory, btnLogout;
    @FXML private Label lblWelcome;

    // Métodos de inicialización y manejo de eventos
    //reservar una cita
    public void reservarCita(){

    }

    //mensaje de bienvenida
    public void initializeLabel(String usuario){
        lblWelcome.setText("Bienvenido/a, " + usuario + ".");
    }

    @SuppressWarnings("unused")
    public void handleLogOut(javafx.event.ActionEvent event) {
        try {
            lblWelcome.setText("Cerrando sesión...");
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
            lblWelcome.setText("Error al cerrar sesión.");
        }
    }

    public void AgendarCita(javafx.event.ActionEvent event) {
        //mostrar la ventana de agendar cita POR ENCIMA
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("NuevaCita-view.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Nueva Cita");
            stage.setScene(new Scene(root));
            stage.show();
            stage.centerOnScreen();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al abrir la ventana");
        }
    }

    public class DoctorController implements Initializable {
        @FXML private TableView<Cliente> tableAppointments;
        @FXML private TableColumn<Cita, String> colDate, colPatient, colReason, colStatus, colActions;
        @FXML private Button btnAppointments, btnPatients, btnPrescriptions, btnLogout;
        @FXML private Label lbl_msg;

        private ObservableList<Cliente> appointmentsList = FXCollections.observableArrayList();
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
            try{
                Connection con = DatabaseConnection.getConnection();
                String sql = "SELECT c.id, c.fecha, c.hora, p.nombre, p.apellido, c.descripcion, c.estado "
                        + "FROM citas c JOIN personas p ON c.cedula_cliente = p.cedula "
                        + "WHERE c.cedula_doctor = ? " // <-- Filtra por el doctor
                        + "ORDER BY c.fecha, c.hora";
                PreparedStatement statement = con.prepareStatement(sql);
                statement.setString(1, cedulaDoctor); // <-- Asigna la cédula del doctor
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

        @FXML
        public void handleCitas(ActionEvent event){
            loadAppointmentsData();
        }

        @FXML
        public void handlePacientes() {//metodo para cambiar la tabla y mostrar a los pacientes de la clínica
                // Definir nuevas columnas si es necesario
                TableColumn<Cita, String> colNombre = new TableColumn<>("Nombre");
                TableColumn<Cita, String> colApellido = new TableColumn<>("Apellido");
                TableColumn<Cita, String> colCedula = new TableColumn<>("Cédula");
                TableColumn<Cita, String> colTelefono = new TableColumn<>("Teléfono");

                colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
                colApellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));
                colCedula.setCellValueFactory(new PropertyValueFactory<>("cedula"));
                colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));

                // Limpiar columnas y datos anteriores
                tableAppointments.getColumns().clear();
                tableAppointments.getItems().clear();

                // Agregar las nuevas columnas
                tableAppointments.getColumns().addAll(colNombre, colApellido, colCedula, colTelefono);

                ObservableList<Cliente> CitaList = FXCollections.observableArrayList();

                try {
                    Connection con = DatabaseConnection.getConnection();
                    String sql = "SELECT nombre, apellido, cedula, telefono FROM personas WHERE tipo = 'paciente'";
                    PreparedStatement statement = con.prepareStatement(sql);
                    ResultSet set = statement.executeQuery();

                    while (set.next()) {
                        String nombre = set.getString("nombre");
                        String apellido = set.getString("apellido");
                        String cedula = set.getString("cedula");
                        int telefono = set.getInt("telefono");
                        CitaList.add(new Cliente(nombre, apellido, cedula, telefono));
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                tableAppointments.setItems(CitaList);
            }
        }
}