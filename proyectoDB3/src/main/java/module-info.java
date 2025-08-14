module com.espol.proyectodb3 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;
    requires java.sql;
    requires jbcrypt;
    requires java.desktop;


    opens com.espol.proyectodb3 to javafx.fxml;
    exports com.espol.proyectodb3;
}