package com.espol.proyectodb3;

import SQL.UserValidations;
import javafx.animation.PauseTransition;
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

public class RegistrarStaffController {

    @FXML
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private Button btn_cancelar;
    @FXML
    private Button btnRegister;
    @FXML
    private TextField txtCedula;
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtApellido;
    @FXML
    private TextField txtEdad;
    @FXML
    private TextField txtCorreo;
    @FXML
    private TextField txtTelefono;
    @FXML
    private TextField txtDireccion;
    @FXML
    private TextField txtUsuario;
    @FXML
    private TextField txtPassword;
    @FXML
    private Label lbl_msg;
    @FXML
    private ComboBox<String> cbRol;

    public void switchToAminPanel(javafx.event.ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("Admin-view.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        stage.setTitle("Clinic data | Panel de administrador");
        stage.centerOnScreen();
    }

    public void initialize() {
        cbRol.getItems().addAll("admin", "staff", "cliente");
        cbRol.setValue("Elige una opción"); // Establecer "staff" como valor predeterminado
    }

    @FXML
    public void HandleAdminRegister(){
        try{
            String cedula = txtCedula.getText();
            String nombrePersona = txtNombre.getText();
            String apellidoPersona = txtApellido.getText();
            int edad = Integer.parseInt(txtEdad.getText());
            String correo = txtCorreo.getText();
            String telefono = txtTelefono.getText();
            String direccion = txtDireccion.getText();
            String usuario = txtUsuario.getText();
            String contrasena = txtPassword.getText();
            String rol = cbRol.getValue();
            //validar campos vacios
            if (cedula == "" || nombrePersona == "" || apellidoPersona == "" || correo == "" || telefono == "" || direccion == "" || usuario == "" || contrasena == "" || rol == "Elige una opción") {
                lbl_msg.setText("Error, complete todos los campos.");
                return;
            }
            UserValidations.guardarRegistro(cedula, nombrePersona, apellidoPersona, edad, correo, telefono, direccion, usuario, contrasena, rol);

            lbl_msg.setStyle("-fx-text-fill: green;");
            lbl_msg.setText("Usuario registrado exitosamente.");
            Stage stage = (Stage) btnRegister.getScene().getWindow();
            PauseTransition delay = new PauseTransition(Duration.seconds(1));
            delay.setOnFinished(event -> stage.close());
            delay.play();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
