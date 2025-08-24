package com.espol.proyectodb3;

import entidades.Cita;
import entidades.Tratamiento;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class DoctorController implements Initializable {

    @FXML private TableView<Cita.CitaRow> tableAppointments;
    @FXML private Label lbl_msg;
    @FXML private Button btnAppointments;
    @FXML private Button btnPatients;
    @FXML private Button btnPrescriptions;
    @FXML private Button btnLogout;

    @FXML private TableColumn<Cita.CitaRow, String> colDate;
    @FXML private TableColumn<Cita.CitaRow, String> colPatient;
    @FXML private TableColumn<Cita.CitaRow, String> colReason;
    @FXML private TableColumn<Cita.CitaRow, String> colStatus;
    @FXML private TableColumn<Cita.CitaRow, Void> colActions;

    private String cedulaDoctor;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (tableAppointments != null) {
            // PropertyValueFactory corregidos
            colDate.setCellValueFactory(new PropertyValueFactory<>("fechaFormateada"));
            colPatient.setCellValueFactory(new PropertyValueFactory<>("paciente"));
            colReason.setCellValueFactory(new PropertyValueFactory<>("motivo"));
            colStatus.setCellValueFactory(new PropertyValueFactory<>("estado"));

            // Configurar columna de acciones con botón
            colActions.setCellFactory(param -> new TableCell<>() {
                private final Button btnDetalles = new Button("Ver Detalles");

                {
                    btnDetalles.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-background-radius: 5;");
                    btnDetalles.setOnAction(event -> {
                        Cita.CitaRow cita = getTableView().getItems().get(getIndex());
                        verDetallesCita(cita);
                    });
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(btnDetalles);
                    }
                }
            });
        }
    }

    public void setCedulaDoctor(String cedula) {
        this.cedulaDoctor = cedula;
        System.out.println("Doctor cedula establecida: " + cedula);
        cargarCitasDelDoctor();
    }

    @FXML
    private void handleCitas() {
        System.out.println("Botón Citas presionado");
        cargarCitasDelDoctor();
        if (lbl_msg != null) {
            lbl_msg.setText("Vista de Citas cargada");
        }
    }

    @FXML
    private void handlePacientes() {
        System.out.println("Botón Pacientes presionado");
        if (lbl_msg != null) {
            lbl_msg.setText("Vista de Pacientes seleccionada");
        }
    }

    @FXML
    private void handleRecetas() {
        System.out.println("Botón Recetas presionado");
        if (lbl_msg != null) {
            lbl_msg.setText("Vista de Recetas seleccionada");
        }
    }

    @FXML
    private void handleLogout() {
        System.out.println("Cerrando sesión");
    }

    public void cargarCitasDelDoctor() {
        try {
            if (cedulaDoctor == null) {
                if (lbl_msg != null) {
                    lbl_msg.setText("Error: Cédula del doctor no establecida");
                }
                return;
            }

            System.out.println("Cargando citas para doctor: " + cedulaDoctor);
            var lista = Cita.obtenerCitasPorDoctor(cedulaDoctor);

            if (tableAppointments != null) {
                tableAppointments.setItems(FXCollections.observableArrayList(lista));
                System.out.println("Citas cargadas: " + lista.size());

                if (lbl_msg != null) {
                    lbl_msg.setText("Citas cargadas: " + lista.size());
                }

                // Debug: imprimir las citas encontradas
                for (Cita.CitaRow cita : lista) {
                    System.out.println("Cita: " + cita.clienteNombre + " " + cita.clienteApellido +
                            " - " + cita.fecha + " " + cita.hora);
                }
            }
        } catch (Exception e) {
            System.err.println("Error cargando citas: " + e.getMessage());
            e.printStackTrace();
            if (lbl_msg != null) {
                lbl_msg.setText("Error cargando citas: " + e.getMessage());
            }
        }
    }

    private void verDetallesCita(Cita.CitaRow cita) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Detalles de la Cita");
        alert.setHeaderText("Información de la Cita");
        alert.setContentText(
                "Paciente: " + cita.clienteNombre + " " + cita.clienteApellido + "\n" +
                        "Fecha: " + cita.fecha + "\n" +
                        "Hora: " + cita.hora + "\n" +
                        "Departamento: " + cita.departamento + "\n" +
                        "Estado: " + cita.estado + "\n" +
                        "Descripción: " + (cita.descripcion != null ? cita.descripcion : "Sin descripción")
        );
        alert.showAndWait();
    }

    public void cargarRecetasDelDoctor() {
        try {
            if (cedulaDoctor != null) {
                var lista = Tratamiento.obtenerTratamientosPorDoctor(cedulaDoctor);
                // tblRecetas.setItems(FXCollections.observableArrayList(lista));
            }
        } catch (Exception e) {
            if (lbl_msg != null) {
                lbl_msg.setText("Error recetas: " + e.getMessage());
            }
        }
    }

    public void initializeLabel(String text) {
        if (lbl_msg != null) {
            lbl_msg.setText(text);
        }
    }

    // Método para refrescar las citas (puedes llamarlo desde otras ventanas)
    public void refrescarCitas() {
        cargarCitasDelDoctor();
    }
}