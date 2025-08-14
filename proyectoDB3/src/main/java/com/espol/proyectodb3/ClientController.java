package com.espol.proyectodb3;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.event.ActionEvent;
import java.io.IOException;

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

}