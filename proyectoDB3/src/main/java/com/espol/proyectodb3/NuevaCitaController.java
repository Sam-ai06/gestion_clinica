package com.espol.proyectodb3;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;


public class NuevaCitaController {

    @FXML
    private DatePicker datePicker;
    @FXML
    private ComboBox<String> cbSpecialty;
    @FXML
    private ComboBox<String> cbDoctor;
    @FXML
    private TextArea taDescription;

    public void handleCancel(ActionEvent event) {
        try {
            //cerrar
            javafx.scene.Node source = (javafx.scene.Node) event.getSource();
            javafx.stage.Stage stage = (javafx.stage.Stage) source.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al cerrar la ventana de nueva cita.");
        }
    }
}
