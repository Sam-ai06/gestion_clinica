package com.espol.proyectodb3;

import SQL.DatabaseConnection;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.IOException;
import java.sql.*;

public class AdminController {
    @FXML
    private TableView<?> tableUsers;
    @FXML
    private TableColumn<?, ?> colId, colName, colRole, colEmail, colActions;
    @FXML
    private Button btn_agregar, btn_logOut, btn_eliminar;
    @FXML
    private Label lbl_messageAdminPanel;


    // Métodos de inicialización y manejo de eventos
    public void switchToRegisterScene(javafx.event.ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("RegistrarStaff-view.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Clinic data | agregar usuario");
        //centrar la ventana
        stage.centerOnScreen();
        stage.show();
    }

    @SuppressWarnings("unused")
    public void handleLogOut(javafx.event.ActionEvent event) {
        try {
            lbl_messageAdminPanel.setText("Cerrando sesión...");
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
            lbl_messageAdminPanel.setText("Error al cerrar sesión.");
        }
    }

    //cambiar a la vista de eliminación de usuario
    public void switchToEliminarUsuarioScene(javafx.event.ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("EliminarUsuarioView.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Clinic data | eliminar usuario");
        //centrar la ventana
        stage.centerOnScreen();
        stage.show();
    }
}