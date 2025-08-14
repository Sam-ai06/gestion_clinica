package entidades;

public class Enfermero extends Persona {
    private String areaEspecializacion;

    //mega constructor de la clase:
    public Enfermero(String cedula, String nombre, String apellido, int edad, String correo, String telefono,
                     String direccion1, String usuario, String contrasena, String rol, String areaEspecializacion) {
        super(cedula, nombre, apellido, edad, correo, telefono, direccion1, usuario, contrasena, rol);
        this.areaEspecializacion = areaEspecializacion;
    }
    
    //getters y setters
    public String getAreaEspecializacion() {
        return areaEspecializacion;
    }

    public void setAreaEspecializacion(String areaEspecializacion) {
        this.areaEspecializacion = areaEspecializacion;
    }
}
