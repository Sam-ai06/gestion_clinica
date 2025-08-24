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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class RegisterViewController {

    @FXML
    private Button btnCancelRegister;
    @FXML
    private Button btnSaveRegister;

    @FXML
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private Label labelRegister;
    @FXML
    private TextField txtCedula, txtNombre, txtApellido, txtEdad, txtCorreo, txtTelefono, txtDireccion, txtUsuario, txtContrasena;
    @FXML
    private ComboBox<String> comboRol;


    //inicializar los valores del comboBox
    @FXML
    public void initialize(){
        comboRol.getItems().addAll("admin", "staff", "cliente");
    }

    @FXML
    protected void SwitchToLoginScene(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("Main-view.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        stage.setTitle("Clinic data | login");
        stage.centerOnScreen();
    }

    @SuppressWarnings("unused")
    @FXML
    public void handleRegister(ActionEvent event) {
        try {
            String cedula = txtCedula.getText();
            String nombrePersona = txtNombre.getText();
            String apellidoPersona = txtApellido.getText();
            int edadPersona = Integer.parseInt(txtEdad.getText()); // Puede lanzar NumberFormatException
            String correoPersona = txtCorreo.getText();
            String telefonoPersona = txtTelefono.getText();
            String direccionPersona = txtDireccion.getText();
            String usuarioPersona = txtUsuario.getText();
            String contrasenaPersona = txtContrasena.getText();
            String rolPersona = comboRol.getValue();

            if (cedula.isEmpty() || nombrePersona.isEmpty() || apellidoPersona.isEmpty() ||
                    correoPersona.isEmpty() || telefonoPersona.isEmpty() || direccionPersona.isEmpty() ||
                    usuarioPersona.isEmpty() || contrasenaPersona.isEmpty() || rolPersona == null) {

                labelRegister.setText("Debe rellenar todos los campos.");
                labelRegister.setStyle("-fx-text-fill: red;"); // Cambia color a rojo
            }
            else {
                labelRegister.setText("Registro exitoso");
                labelRegister.setStyle("-fx-text-fill: green;");

                //guardar la información en la base de datos:
                UserValidations.guardarRegistro(cedula, nombrePersona, apellidoPersona, edadPersona,
                        correoPersona, telefonoPersona, direccionPersona, usuarioPersona, contrasenaPersona, rolPersona);

                // Mostrar mensaje de éxito
                labelRegister.setText("Registro exitoso, redirigiendo...");
                labelRegister.setStyle("-fx-text-fill: green;");

                PauseTransition delay = new PauseTransition(Duration.seconds(2));
                delay.setOnFinished(e -> {
                    try {
                        Parent root = FXMLLoader.load(getClass().getResource("Main-view.fxml"));
                        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        stage.setScene(new Scene(root));
                        stage.centerOnScreen();
                        stage.show();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                });
                delay.play();
            }
        } catch (NumberFormatException e) {
            labelRegister.setText("Error: La edad debe ser un número válido");
            labelRegister.setStyle("-fx-text-fill: red;");
        }
    }
}
