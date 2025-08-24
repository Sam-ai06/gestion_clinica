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

        }
        else if (UserValidations.validateUser(usuario, contrasenia, "staff")){ //validar y saltar a la vista de doctores
            try {
                LabelMsg.setText("redigiriendo...");
                PauseTransition delay = new PauseTransition(Duration.seconds(2));
                delay.setOnFinished(e ->{
                    try {
                        // Usar FXMLLoader con controlador
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("Doctor-view.fxml"));
                        root = loader.load();

                        // Obtener el controlador y establecer la cédula del doctor
                        DoctorController doctorController = loader.getController();
                        if (doctorController != null) {
                            // Obtener la cédula real del doctor usando su usuario
                            String cedulaDoctor = UserValidations.obtenerCedulaPorUsuario(usuario, "staff");

                            if (cedulaDoctor != null) {
                                doctorController.setCedulaDoctor(cedulaDoctor);
                            } else {
                                System.err.println("ERROR: No se pudo obtener la cédula del doctor para usuario: " + usuario);
                                LabelMsg.setText("Error: No se encontró información del doctor");
                            }
                        } else {
                            System.err.println("ERROR: No se pudo obtener el DoctorController");
                        }

                        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        scene = new Scene(root);
                        stage.setScene(scene);
                        stage.setTitle("Clinic data | panel de personal");
                        //centrar la ventana
                        stage.centerOnScreen();
                        stage.show();
                    } catch (IOException ex) {
                        System.err.println("Error cargando vista de doctor: " + ex.getMessage());
                        ex.printStackTrace();
                    }

                });
                delay.play();

            } catch (Exception e){
                System.err.println("Error en login de staff: " + e.getMessage());
                e.printStackTrace();
            }
        }
        else if (UserValidations.validateUser(usuario, contrasenia, "cliente")){ //validar y saltar a la vista de cliente
            LabelMsg.setText("redigiriendo...");

            PauseTransition delay = getPauseTransition(event);
            delay.play();
        }
        else if (UserValidations.validateUser(usuario, contrasenia, "enfermero")){ //validar y saltar a la vista de enfermero
            try {
                LabelMsg.setText("redigiriendo...");
                // Aquí puedes agregar la lógica para enfermeros si la necesitas
                LabelMsg.setText("Funcionalidad de enfermero en desarrollo");
            } catch (Exception e) {
                System.err.println("Error en login de enfermero: " + e.getMessage());
                e.printStackTrace();
            }
        }
        else {
            LabelMsg.setText("Usuario o contraseña incorrectos");
        }
    }

    @SuppressWarnings("unused")
    private PauseTransition getPauseTransition(ActionEvent event) {
        PauseTransition delay = new PauseTransition(Duration.seconds(2));
        delay.setOnFinished(e -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("Client-view.fxml"));
                root = loader.load();
                ClientController clientController = loader.getController();

                if (clientController != null) {
                    // También obtener la cédula del cliente para consistencia
                    String cedulaCliente = UserValidations.obtenerCedulaPorUsuario(txtUsername.getText(), "cliente");
                    if (cedulaCliente != null) {
                        clientController.initializeLabel(txtUsername.getText());
                        // Si tienes un método para establecer la cédula del cliente, úsalo aquí:
                        // clientController.setCedulaCliente(cedulaCliente);
                    }
                } else {
                    System.err.println("ERROR: No se pudo obtener el ClientController");
                }

                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.setTitle("Clinic data | panel de clientes");

                // Centrar la ventana
                stage.centerOnScreen();
                stage.show();
            } catch (IOException ex) {
                System.err.println("Error cargando vista de cliente: " + ex.getMessage());
                ex.printStackTrace();
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