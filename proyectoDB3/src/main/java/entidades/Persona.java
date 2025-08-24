package entidades;

import javafx.beans.property.SimpleStringProperty;

public class Persona {
    private final SimpleStringProperty cedula;
    private final SimpleStringProperty nombre;
    private final SimpleStringProperty apellido;
    private final SimpleStringProperty correo;
    private final SimpleStringProperty rol;

    public Persona(String cedula, String nombre, String apellido, String correo, String rol) {
        this.cedula = new SimpleStringProperty(cedula);
        this.nombre = new SimpleStringProperty(nombre);
        this.apellido = new SimpleStringProperty(apellido);
        this.correo = new SimpleStringProperty(correo);
        this.rol = new SimpleStringProperty(rol);
    }

    public String getCedula() { return cedula.get(); }
    public String getNombre() { return nombre.get(); }
    public String getApellido() { return apellido.get(); }
    public String getCorreo() { return correo.get(); }
    public String getRol() { return rol.get(); }

    public SimpleStringProperty cedulaProperty() { return cedula; }
    public SimpleStringProperty nombreProperty() { return nombre; }
    public SimpleStringProperty apellidoProperty() { return apellido; }
    public SimpleStringProperty correoProperty() { return correo; }
    public SimpleStringProperty rolProperty() { return rol; }
}
