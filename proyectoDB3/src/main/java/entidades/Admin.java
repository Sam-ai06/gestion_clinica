package entidades;

import java.time.LocalDateTime;

public class Admin extends Persona{
    private String nivelAcceso = "medio";
    private LocalDateTime ultimoAcceso;
    private boolean puedeCrearUsuarios;
    private boolean puedeEliminarDatos;

    //mega constructor de la clase:
    public Admin(String cedula, String nombre, String apellido, int edad, String correo, String telefono, String direccion1,
                 String usuario, String contrasena, String rol, String nivelAcceso, LocalDateTime ultimoAcceso,
                 boolean puedeCrearUsuarios, boolean puedeEliminarDatos) {
        super(cedula, nombre, apellido, edad, correo, telefono, direccion1, usuario, contrasena, rol);
        this.nivelAcceso = nivelAcceso;
        this.ultimoAcceso = ultimoAcceso;
        this.puedeCrearUsuarios = puedeCrearUsuarios;
        this.puedeEliminarDatos = puedeEliminarDatos;
    }

    //getters y setters:
    public String getNivelAcceso() {
        return nivelAcceso;
    }

    public void setNivelAcceso(String nivelAcceso) {
        this.nivelAcceso = nivelAcceso;
    }

    public LocalDateTime getUltimoAcceso() {
        return ultimoAcceso;
    }

    public void setUltimoAcceso(LocalDateTime ultimoAcceso) {
        this.ultimoAcceso = ultimoAcceso;
    }

    public boolean isPuedeCrearUsuarios() {
        return puedeCrearUsuarios;
    }

    public void setPuedeCrearUsuarios(boolean puedeCrearUsuarios) {
        this.puedeCrearUsuarios = puedeCrearUsuarios;
    }

    public boolean isPuedeEliminarDatos() {
        return puedeEliminarDatos;
    }

    public void setPuedeEliminarDatos(boolean puedeEliminarDatos) {
        this.puedeEliminarDatos = puedeEliminarDatos;
    }

    //metodos para un administrador:

}
