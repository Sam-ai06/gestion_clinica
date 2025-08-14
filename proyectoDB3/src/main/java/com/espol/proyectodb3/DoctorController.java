package com.espol.proyectodb3;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Button;

public class DoctorController {
    @FXML private TableView<?> tableAppointments;
    @FXML private TableColumn<?, ?> colDate, colPatient, colReason, colStatus, colActions;
    @FXML private Button btnAppointments, btnPatients, btnPrescriptions, btnLogout;

    // Métodos de inicialización y manejo de eventos
}