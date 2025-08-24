package com.espol.proyectodb3;

import entidades.Cita;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.application.Platform;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ClientController implements Initializable {
    @FXML
    private TableView<Cita.CitaClienteRow> tableMyAppointments;
    @FXML
    private TableColumn<Cita.CitaClienteRow, String> colDate, colDoctor, colSpecialty, colStatus;
    @FXML
    private TableColumn<Cita.CitaClienteRow, Void> colActions;
    @FXML
    private Button btnMyAppointments, btnNewAppointment, btnMedicalHistory, btnLogout;
    @FXML
    private Label lblWelcome;

    private String usuarioActual;
    private String cedulaCliente;
    private ObservableList<Cita.CitaClienteRow> citasList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configurarTabla();
        configurarEventosBotones();
    }

    private void configurarTabla() {
        colDate.setCellValueFactory(new PropertyValueFactory<>("fechaFormateada"));
        colDoctor.setCellValueFactory(new PropertyValueFactory<>("doctorCompleto"));
        colSpecialty.setCellValueFactory(new PropertyValueFactory<>("doctorEspecialidad"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colActions.setCellFactory(param -> new TableCell<>() {
            private final Button btnEliminar = new Button("Eliminar");

            {
                btnEliminar.setStyle("-fx-background-color: #ff4444; -fx-text-fill: white; -fx-background-radius: 5;");
                btnEliminar.setOnAction(event -> {
                    Cita.CitaClienteRow cita = getTableView().getItems().get(getIndex());
                    eliminarCitaSeleccionada(cita);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btnEliminar);
                }
            }
        });

        tableMyAppointments.setItems(citasList);
    }

    private void configurarEventosBotones() {
        btnMyAppointments.setOnAction(event -> cargarMisCitas());
        btnNewAppointment.setOnAction(event -> AgendarCita(event));
        //btnMedicalHistory.setOnAction(event -> mostrarHistorialMedico());
    }


    public void initializeLabel(String usuario) {
        this.usuarioActual = usuario;
        lblWelcome.setText("Bienvenido/a, " + usuario + ".");

        // Obtener la cédula del cliente desde la base de datos
        obtenerCedulaCliente(usuario);

        javafx.application.Platform.runLater(() -> {
            cargarMisCitas();
        });
    }

    private void obtenerCedulaCliente(String usuario) {
        try {
            // Aquí deberías implementar un método para obtener la cédula por usuario
            // Por ahora, usaremos una consulta directa
            java.sql.Connection cn = SQL.DatabaseConnection.getConnection();
            String sql = "SELECT cedula FROM personas WHERE usuario = ?";
            java.sql.PreparedStatement ps = cn.prepareStatement(sql);
            ps.setString(1, usuario);
            java.sql.ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                this.cedulaCliente = rs.getString("cedula");
            }
            rs.close();
            ps.close();
            cn.close();
        } catch (SQLException e) {
            mostrarError("Error al obtener información del cliente", e.getMessage());
        }
    }

    @FXML
    private void cargarMisCitas() {
        if (cedulaCliente == null) {
            mostrarError("Error", "No se pudo obtener la información del cliente");
            return;
        }

        try {
            List<Cita.CitaClienteRow> citas = Cita.obtenerCitasPorCliente(cedulaCliente);
            citasList.clear();
            citasList.addAll(citas);

        } catch (SQLException e) {
            mostrarError("Error al cargar citas", e.getMessage());
            e.printStackTrace();
        }
    }

    private void eliminarCitaSeleccionada(Cita.CitaClienteRow cita) {
        // Usar Platform.runLater para el diálogo de confirmación
        javafx.application.Platform.runLater(() -> {
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmar eliminación");
            confirmacion.setHeaderText("¿Está seguro que desea eliminar esta cita?");
            confirmacion.setContentText("Fecha: " + cita.getFechaFormateada() +
                    "\nDoctor: " + cita.getDoctorCompleto() +
                    "\nEspecialidad: " + cita.doctorEspecialidad);

            Optional<ButtonType> resultado = confirmacion.showAndWait();
            if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
                try {
                    boolean eliminada = Cita.eliminarCita(cita.citaId, cedulaCliente);
                    if (eliminada) {
                        citasList.remove(cita);
                        mostrarInfo("Éxito", "Cita eliminada correctamente");
                    } else {
                        mostrarError("Error", "No se pudo eliminar la cita");
                    }
                } catch (SQLException e) {
                    mostrarError("Error al eliminar cita", e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }

    @FXML
    public void AgendarCita(javafx.event.ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("NuevaCita-view.fxml"));
            Parent root = loader.load();

            // Pasar información del cliente al controlador de nueva cita
            NuevaCitaController nuevaCitaController = loader.getController();
            if (nuevaCitaController != null) {
                nuevaCitaController.setCedulaCliente(cedulaCliente);
                nuevaCitaController.setUsuarioCliente(usuarioActual);
            }

            Stage stage = new Stage();
            stage.setTitle("Nueva Cita");
            stage.setScene(new Scene(root));
            stage.show();
            stage.centerOnScreen();

            // Recargar citas cuando se cierre la ventana
            stage.setOnHidden(e -> cargarMisCitas());

        } catch (IOException e) {
            mostrarError("Error", "No se pudo abrir la ventana de nueva cita");
            e.printStackTrace();
        }
    }

    private void mostrarHistorialMedico() {
        // Mostrar información del historial médico
        Alert info = new Alert(Alert.AlertType.INFORMATION);
        info.setTitle("Historial Médico");
        info.setHeaderText("Historial Médico de " + usuarioActual);
        info.setContentText("Funcionalidad en desarrollo.\nAquí se mostrará el historial médico completo.");
        info.showAndWait();
    }

    @FXML
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

    // Métodos auxiliares para mostrar mensajes
    private void mostrarError(String titulo, String mensaje) {
        javafx.application.Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(titulo);
            alert.setHeaderText(null);
            alert.setContentText(mensaje);
            alert.show();
        });
    }

    private void mostrarInfo(String titulo, String mensaje) {
        javafx.application.Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(titulo);
            alert.setHeaderText(null);
            alert.setContentText(mensaje);
            alert.show();
        });
    }

    public void refrescarCitas() {
        cargarMisCitas();
    }
}