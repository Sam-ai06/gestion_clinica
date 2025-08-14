package entidades;

public class Doctor extends Persona{
    private String especialidad;


    //mega constructor de la clase:

    public Doctor(String cedula, String nombre, String apellido, int edad, String correo,
                  String telefono, String direccion1, String direccion2, String usuario,
                  String contrasena, String rol, String especialidad) {
        super(cedula, nombre, apellido, edad, correo, telefono, direccion1, usuario, contrasena, rol);
        this.especialidad = especialidad;
    }

    //getters y setters
    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }
}
