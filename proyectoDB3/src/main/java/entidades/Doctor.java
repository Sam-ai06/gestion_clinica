package entidades;

public class Doctor extends Persona {
    private String telefono;
    private String direccion1;
    private String usuario;
    private String contrasena;
    private String rol;
    private String especialidad;

    public Doctor(String cedula, String nombre, String apellido, String edad, String correo,
                  String telefono, String direccion1, String usuario,
                  String contrasena, String rol, String especialidad) {
        super(cedula, nombre, apellido, edad, correo);
        this.telefono = telefono;
        this.direccion1 = direccion1;
        this.usuario = usuario;
        this.contrasena = contrasena;
        this.rol = rol;
        this.especialidad = especialidad;
    }

    // Getters y setters
    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

}