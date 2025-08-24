package com.espol.proyectodb3;

import entidades.Cita;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.ResourceBundle;

public class NuevaCitaController implements Initializable {

    @FXML
    private ComboBox<Cita.DoctorInfo> cbDoctor;
    @FXML
    private DatePicker dpFecha;
    @FXML
    private ComboBox<String> cbHora;
    @FXML
    private TextArea taDescripcion;
    @FXML
    private Button btnAgendar, btnCancelar;
    @FXML
    private Label lblTitulo;

    private String cedulaCliente;
    private String usuarioCliente;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configurarComponentes();
        cargarDoctores();
        configurarEventos();
    }

    private void configurarComponentes() {
        cbHora.setItems(FXCollections.observableArrayList(
                "08:00", "08:30", "09:00", "09:30", "10:00", "10:30",
                "11:00", "11:30", "14:00", "14:30", "15:00", "15:30",
                "16:00", "16:30", "17:00", "17:30"
        ));

        dpFecha.setValue(LocalDate.now().plusDays(1));
        dpFecha.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(LocalDate.now()));
            }
        });

        cbDoctor.setCellFactory(listView -> new ListCell<Cita.DoctorInfo>() {
            @Override
            protected void updateItem(Cita.DoctorInfo item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getNombreCompleto());
                }
            }
        });

        cbDoctor.setButtonCell(new ListCell<Cita.DoctorInfo>() {
            @Override
            protected void updateItem(Cita.DoctorInfo item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getNombreCompleto());
                }
            }
        });
    }

    private void cargarDoctores() {
        try {
            List<Cita.DoctorInfo> doctores = Cita.obtenerDoctoresDisponibles();
            cbDoctor.setItems(FXCollections.observableArrayList(doctores));
        } catch (SQLException e) {
            mostrarError("Error", "No se pudieron cargar los doctores disponibles");
            e.printStackTrace();
        }
    }

    private void configurarEventos() {
        btnAgendar.setOnAction(event -> agendarCita());
        btnCancelar.setOnAction(event -> cerrarVentana());
    }

    public void setCedulaCliente(String cedulaCliente) {
        this.cedulaCliente = cedulaCliente;
    }

    public void setUsuarioCliente(String usuarioCliente) {
        this.usuarioCliente = usuarioCliente;
        if (lblTitulo != null) {
            lblTitulo.setText("Agendar Nueva Cita - " + usuarioCliente);
        }
    }

    @FXML
    private void agendarCita() {
        if (!validarDatos()) {
            return;
        }

        try {
            // Obtener datos del formulario
            Cita.DoctorInfo doctorSeleccionado = cbDoctor.getValue();
            LocalDate fechaSeleccionada = dpFecha.getValue();
            String horaSeleccionada = cbHora.getValue();
            String descripcion = taDescripcion.getText().trim();

            // Convertir a tipos SQL
            Date fecha = Date.valueOf(fechaSeleccionada);
            Time hora = Time.valueOf(horaSeleccionada + ":00");

            // Crear la cita
            boolean citaCreada = Cita.crearCita(
                    doctorSeleccionado.cedula,
                    cedulaCliente,
                    fecha,
                    hora,
                    doctorSeleccionado.especialidad,
                    descripcion,
                    "Observaciones iniciales",
                    "Pendiente de consulta"
            );

            if (citaCreada) {
                mostrarInfo("Éxito", "Cita agendada correctamente para el " +
                        fechaSeleccionada + " a las " + horaSeleccionada);
                cerrarVentana();
            } else {
                mostrarError("Error", "No se pudo agendar la cita. Inténtelo nuevamente.");
            }

        } catch (SQLException e) {
            if (e.getMessage().contains("uc_cita_doctor_hora")) {
                mostrarError("Conflicto de horario",
                        "El doctor ya tiene una cita programada en ese horario. " +
                                "Por favor seleccione otro horario.");
            } else {
                mostrarError("Error de base de datos", e.getMessage());
            }
            e.printStackTrace();
        }
    }

    private boolean validarDatos() {
        if (cbDoctor.getValue() == null) {
            mostrarError("Validation Error", "Por favor seleccione un doctor");
            return false;
        }

        if (dpFecha.getValue() == null) {
            mostrarError("Validation Error", "Por favor seleccione una fecha");
            return false;
        }

        if (cbHora.getValue() == null) {
            mostrarError("Validation Error", "Por favor seleccione una hora");
            return false;
        }

        if (taDescripcion.getText().trim().isEmpty()) {
            mostrarError("Validation Error", "Por favor ingrese una descripción del motivo de la cita");
            return false;
        }

        // Validar que la fecha no sea en el pasado
        if (dpFecha.getValue().isBefore(LocalDate.now())) {
            mostrarError("Validation Error", "No se puede agendar una cita en una fecha pasada");
            return false;
        }

        return true;
    }

    @FXML
    private void cerrarVentana() {
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }

    private void mostrarError(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarInfo(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}