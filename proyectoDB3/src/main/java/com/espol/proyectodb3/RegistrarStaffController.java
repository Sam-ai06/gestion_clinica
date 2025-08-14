package com.espol.proyectodb3;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


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

}
