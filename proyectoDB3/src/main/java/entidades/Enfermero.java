package entidades;

import java.time.LocalDateTime;

public class Enfermero extends Persona {
    private String telefono;
    private String direccion1;
    private String usuario;
    private String contrasena;
    private String rol;
    private String areaEspecializacion;

    // Constructor corregido
    public Enfermero(String cedula, String nombre, String apellido, String edad, String correo,
                     String telefono, String direccion1, String usuario, String contrasena, String rol,
                     String areaEspecializacion) {
        super(cedula, nombre, apellido, edad, correo);
        this.telefono = telefono;
        this.direccion1 = direccion1;
        this.usuario = usuario;
        this.contrasena = contrasena;
        this.rol = rol;
        this.areaEspecializacion = areaEspecializacion;
    }

    // Getters y setters
    public String getAreaEspecializacion() {
        return areaEspecializacion;
    }

    public void setAreaEspecializacion(String areaEspecializacion) {
        this.areaEspecializacion = areaEspecializacion;
    }

    // Agrega getters y setters para los otros campos si los necesitas
}