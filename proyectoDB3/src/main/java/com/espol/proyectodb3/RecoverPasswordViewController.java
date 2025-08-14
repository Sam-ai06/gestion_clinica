package com.espol.proyectodb3;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.io.IOException;

public class RecoverPasswordViewController {
    @FXML
    private Button btn_cancelar;
    @FXML
    private Button btn_verify;
    @FXML
    private TextField txt_user;

    @FXML
    private Stage stage;
    private Scene scene;
    private Parent root;

    public void switchToLoginScene(javafx.event.ActionEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("Main-view.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Clinic data | Login");
        //centrar la ventana
        stage.centerOnScreen();
        stage.show();
    }
}
