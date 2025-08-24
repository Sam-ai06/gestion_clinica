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
import seguridad_Otros.Utileria;

import java.io.IOException;

public class MainController {
    @FXML
    private Button btnLogin;

    @FXML
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private TextField txtUsername;
    @FXML
    private TextField txtPassword;
    @FXML
    private TextField txtRole;
    @FXML
    private Label LabelMsg;




   @FXML
    protected void switchToRegisterScene(ActionEvent event) throws IOException {
       root = FXMLLoader.load(getClass().getResource("Register-view.fxml"));
       stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
       scene = new Scene(root);
       stage.setTitle("Clinic data | registro");
       stage.setScene(scene);
       stage.show();

       // Centrar la ventana
       stage.centerOnScreen();
   }

   @SuppressWarnings("unused")
@FXML
    protected void handleLogin(ActionEvent event){
        String usuario = txtUsername.getText();
        String contrasenia = txtPassword.getText();
        String hashedPassword = Utileria.encriptarContrasena(contrasenia);

        if (UserValidations.validateUser(usuario, contrasenia, "admin")){ //validar y saltar a la vista de admin
            try {
                LabelMsg.setText("redigiriendo...");
                root = FXMLLoader.load(getClass().getResource("Admin-view.fxml"));
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.setTitle("Clinic data | panel de administrador");
                stage.show();
            } catch (IOException e) {
                LabelMsg.setText("Usuario o contraseña incorrectos");
                e.printStackTrace();
            }

        } //vista staff
        else if (UserValidations.validateUser(usuario, contrasenia, "staff")){ //validar y saltar a la vista de doctores
            try {
                LabelMsg.setText("redigiriendo...");
                PauseTransition delay = new PauseTransition(Duration.seconds(2));
                delay.setOnFinished(e ->{
                    try {
                        root = FXMLLoader.load(getClass().getResource("Doctor-view.fxml"));
                        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        scene = new Scene(root);
                        stage.setScene(scene);
                        stage.setTitle("Clinic data | panel de personal");
                        //centrar la ventana
                        stage.centerOnScreen();
                        stage.show();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }

                        });
                    delay.play();

            } catch (Exception e){
                e.printStackTrace();
            }
        } else if (UserValidations.validateUser(usuario, contrasenia, "cliente")){ //validar y saltar a la vista de cliente
            LabelMsg.setText("redigiriendo...");

            PauseTransition delay = getPauseTransition(event);
            delay.play();
        } else {
            LabelMsg.setText("Usuario o contraseña incorrectos");
        }
   }
    @SuppressWarnings("unused")
    private PauseTransition getPauseTransition(ActionEvent event) {
        PauseTransition delay = new PauseTransition(Duration.seconds(2));
        delay.setOnFinished(e ->{
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("Client-view.fxml"));
                root = loader.load();

                DoctorController doctorController = loader.getController();
                doctorController.initializeLabel(txtUsername.getText());

                root = FXMLLoader.load(getClass().getResource("Client-view.fxml"));
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.setTitle("Clinic data | panel de clientes");
                //centrar la ventana
                stage.centerOnScreen();
                stage.show();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        return delay;
    }

    public void switchToRecoverPasswordScene(ActionEvent event) throws IOException {
       root = FXMLLoader.load(getClass().getResource("recoverPassword-view.fxml"));
       stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
       scene = new Scene(root);
       stage.setScene(scene);
       stage.setTitle("Clinic data | recuperar contraseña");
       //centrar la ventana
       stage.centerOnScreen();
       stage.show();
   }


}