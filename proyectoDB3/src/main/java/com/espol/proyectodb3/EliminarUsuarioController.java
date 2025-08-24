package com.espol.proyectodb3;

import SQL.UserValidations;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class EliminarUsuarioController {
    @FXML
    private Button btn_eliminarUsuario, btn_cancelar;
    @FXML
    private TextField txt_usuarioAeliminar;
    @FXML
    private Label lbl_msg;


    //cancelar y volver al panel de admin
    @FXML
    public void cancelarEliminacion(ActionEvent event){
        //volver a la ventana de admin
        try {
            lbl_msg.setText("Cancelando...");
            PauseTransition delay = new PauseTransition(Duration.seconds(2));
            delay.setOnFinished(e -> {
                try {
                    Parent root = FXMLLoader.load(getClass().getResource("Admin-view.fxml"));
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
        }
    }

    //eliminar el usuario
    @FXML
    public void eliminarUsuario(){
        if (txt_usuarioAeliminar.getText() == "" || txt_usuarioAeliminar.getText().isEmpty()){
            lbl_msg.setText("Error, rellene todos los campos.");
        } else {
            String usuario = txt_usuarioAeliminar.getText();
            UserValidations.

        }
    }
}
