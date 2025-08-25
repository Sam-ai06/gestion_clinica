package com.espol.proyectodb3;

import entidades.Cita;
import entidades.Tratamiento;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Optional;
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
        cargarCitasDelDoctor();
    }

    @FXML
    private void handleCitas() {
        cargarCitasDelDoctor();
        if (lbl_msg != null) {
            lbl_msg.setText("Vista de Citas cargada");
        }
    }

    @FXML
    private void handlePacientes() {
        if (lbl_msg != null) {
            lbl_msg.setText("Vista de Pacientes seleccionada");
        }
    }

    @FXML
    private void handleRecetas() {
        if (lbl_msg != null) {
            lbl_msg.setText("Vista de Recetas seleccionada");
        }
    }

    @FXML
    private void handleLogout() {
        try {
            // Cargar la vista de login
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Main-view.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) btnLogout.getScene().getWindow();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Clinic data | panel de personal");
            stage.centerOnScreen();
            stage.show();

            this.cedulaDoctor = null;

        } catch (Exception e) {
            System.err.println("Error cerrando sesión: " + e.getMessage());
            e.printStackTrace();

            if (lbl_msg != null) {
                lbl_msg.setText("Error al cerrar sesión");
            }
        }
    }

    public void cargarCitasDelDoctor() {
        try {
            if (cedulaDoctor == null) {
                if (lbl_msg != null) {
                    lbl_msg.setText("Error: Cédula del doctor no establecida");
                }
                return;
            }

            var lista = Cita.obtenerCitasPorDoctor(cedulaDoctor);

            if (tableAppointments != null) {
                tableAppointments.setItems(FXCollections.observableArrayList(lista));

                if (lbl_msg != null) {
                    lbl_msg.setText("Citas cargadas: " + lista.size());
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
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Gestión de Cita");
        dialog.setHeaderText("Cita de " + cita.clienteNombre + " " + cita.clienteApellido);

        // Crear el contenido del diálogo
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(15);
        grid.setPadding(new Insets(20, 20, 10, 10));

        // Información de la cita (solo lectura)
        grid.add(new Label("📅 Fecha:"), 0, 0);
        Label lblFecha = new Label(cita.fecha.toString());
        lblFecha.setStyle("-fx-font-weight: bold;");
        grid.add(lblFecha, 1, 0);

        grid.add(new Label("🕐 Hora:"), 0, 1);
        Label lblHora = new Label(cita.hora.toString());
        lblHora.setStyle("-fx-font-weight: bold;");
        grid.add(lblHora, 1, 1);

        grid.add(new Label("🏥 Departamento:"), 0, 2);
        grid.add(new Label(cita.departamento), 1, 2);

        grid.add(new Label("📝 Descripción:"), 0, 3);
        TextArea txtDescripcion = new TextArea(cita.descripcion != null ? cita.descripcion : "Sin descripción");
        txtDescripcion.setEditable(false);
        txtDescripcion.setPrefRowCount(2);
        txtDescripcion.setPrefColumnCount(30);
        grid.add(txtDescripcion, 1, 3);

        // ComboBox para cambiar estado
        grid.add(new Label("⚡ Cambiar Estado:"), 0, 4);
        ComboBox<String> estadoCombo = new ComboBox<>();
        estadoCombo.getItems().addAll("pendiente", "en_curso", "completada", "cancelada");
        estadoCombo.setValue(cita.estado);
        estadoCombo.setStyle("-fx-font-size: 14px;");

        // Agregar colores según el estado
        estadoCombo.setOnAction(e -> {
            String selectedState = estadoCombo.getValue();
            switch (selectedState) {
                case "pendiente":
                    estadoCombo.setStyle("-fx-base: #FFF3CD; -fx-font-size: 14px;"); // Amarillo
                    break;
                case "en_curso":
                    estadoCombo.setStyle("-fx-base: #CCE5FF; -fx-font-size: 14px;"); // Azul
                    break;
                case "completada":
                    estadoCombo.setStyle("-fx-base: #D4EDDA; -fx-font-size: 14px;"); // Verde
                    break;
                case "cancelada":
                    estadoCombo.setStyle("-fx-base: #F8D7DA; -fx-font-size: 14px;"); // Rojo
                    break;
            }
        });

        grid.add(estadoCombo, 1, 4);

        // Label informativo
        Label infoLabel = new Label("💡 Selecciona el nuevo estado y presiona 'Actualizar'");
        infoLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #666;");
        grid.add(infoLabel, 0, 5, 2, 1);

        dialog.getDialogPane().setContent(grid);

        // Botones
        ButtonType btnActualizar = new ButtonType("✅ Actualizar Estado", ButtonBar.ButtonData.OK_DONE);
        ButtonType btnCerrar = new ButtonType("❌ Cerrar", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(btnActualizar, btnCerrar);

        // Estilo del diálogo
        dialog.getDialogPane().setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 13px;");

        // Convertir resultado
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == btnActualizar) {
                return estadoCombo.getValue();
            }
            return null;
        });

        Optional<String> resultado = dialog.showAndWait();

        if (resultado.isPresent() && !resultado.get().equals(cita.estado)) {
            cambiarEstadoCita(cita.citaId, resultado.get(), cita.clienteNombre + " " + cita.clienteApellido);
        }
    }

    private void cambiarEstadoCita(int citaId, String nuevoEstado, String nombrePaciente) {
        try {
            boolean exitoso = Cita.actualizarEstadoCita(citaId, nuevoEstado);

            if (exitoso) {
                // Mostrar mensaje de éxito
                Alert confirmacion = new Alert(Alert.AlertType.INFORMATION);
                confirmacion.setTitle("✅ Estado Actualizado");
                confirmacion.setHeaderText("Actualización Exitosa");
                confirmacion.setContentText("El estado de la cita de " + nombrePaciente +
                        " se actualizó a: " + nuevoEstado.replace("_", " ").toUpperCase());
                confirmacion.showAndWait();

                // Refrescar la tabla
                cargarCitasDelDoctor();

                if (lbl_msg != null) {
                    lbl_msg.setText("Estado de cita actualizado exitosamente");
                }
            } else {
                // Mostrar mensaje de error
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setTitle("❌ Error");
                error.setHeaderText("No se pudo actualizar");
                error.setContentText("Hubo un problema al actualizar el estado de la cita. Verifica que el ID de la cita sea válido.");
                error.showAndWait();
            }

        } catch (Exception e) {
            System.err.println("Error cambiando estado de cita: " + e.getMessage());
            e.printStackTrace();

            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("❌ Error del Sistema");
            error.setHeaderText("Error Interno");
            error.setContentText("Error interno: " + e.getMessage());
            error.showAndWait();
        }
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