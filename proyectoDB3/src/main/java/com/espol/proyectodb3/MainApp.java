package com.espol.proyectodb3;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("Main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 650, 450);
        stage.setTitle("Clinic data | login");
        //centrar
        stage.centerOnScreen();
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}

//borrador para el borde de un boton
// btnLogin.setStyle("-fx-border-color: #000000; -fx-border-width: 2px; -fx-border-radius: 5px;");
