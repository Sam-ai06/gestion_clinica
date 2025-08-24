package com.espol.proyectodb3;

import SQL.DatabaseConnection;
import SQL.UserValidations;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;

public class RecoverPasswordViewController {
    @FXML
    private Button btn_cancelar;
    @FXML
    private Button btn_verify;
    @FXML
    private TextField txt_user, txt_password;
    @FXML
    private Label lbl_msg;

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

    //cambiar la contraseña y cerrar
    public void cambiarPassword(){
        try{
            if (txt_user.getText() == "" || txt_password.getText() == ""){
                lbl_msg.setText("Error. Debe rellenar todos los campos");
                return;
            }
            String user = txt_user.getText();
            String newPassword = txt_password.getText();
            //verificar el el usuario existe en la base de datos
            UserValidations validator = new UserValidations();
            if (validator.validateUser(user)){
                validator.changePassword(user, newPassword, lbl_msg);
            }


        } catch (Exception e) {
            System.out.println("error en la clase de recuperación de contraseña");
            throw new RuntimeException(e);
        }

    }


}
