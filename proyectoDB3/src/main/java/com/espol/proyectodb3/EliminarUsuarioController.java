package com.espol.proyectodb3;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class EliminarUsuarioController {
    @FXML
    private Button btn_eliminarUsuario, btn_cancelar;
    @FXML
    private TextField txt_usuarioAeliminar;
    @FXML
    private Label lbl_msg;


    //cancelar y volver al panel de admin
    @FXML
    public void cancelarEliminacion(){
        //volver a la ventana de admin
        

    }

    //eliminar el usuario
    @FXML
    public void eliminarUsuario(){
        if (txt_usuarioAeliminar.getText() == "" || txt_usuarioAeliminar.getText().isEmpty()){
            lbl_msg.setText("Error, rellene todos los campos.");
        } else {
            String usuario = txt_usuarioAeliminar.getText();

        }
    }
}
