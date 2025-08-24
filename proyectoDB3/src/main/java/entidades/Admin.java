package entidades;

import java.time.LocalDateTime;

public class Admin extends Persona {
    private String telefono;
    private String direccion1;
    private String usuario;
    private String contrasena;
    private String rol;
    private String nivelAcceso = "medio";
    private LocalDateTime ultimoAcceso;
    private boolean puedeCrearUsuarios;
    private boolean puedeEliminarDatos;

    // Constructor corregido
    public Admin(String cedula, String nombre, String apellido, String edad, String correo,
                 String telefono, String direccion1, String usuario, String contrasena, String rol,
                 String nivelAcceso, LocalDateTime ultimoAcceso,
                 boolean puedeCrearUsuarios, boolean puedeEliminarDatos) {
        super(cedula, nombre, apellido, edad, correo);
        this.telefono = telefono;
        this.direccion1 = direccion1;
        this.usuario = usuario;
        this.contrasena = contrasena;
        this.rol = rol;
        this.nivelAcceso = nivelAcceso;
        this.ultimoAcceso = ultimoAcceso;
        this.puedeCrearUsuarios = puedeCrearUsuarios;
        this.puedeEliminarDatos = puedeEliminarDatos;
    }

    // Getters y setters
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
}




