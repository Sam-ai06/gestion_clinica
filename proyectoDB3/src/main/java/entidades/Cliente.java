package entidades;

public class Cliente extends Persona{
    boolean agendaCita = true;

    //mega constructor
    public Cliente(String cedula, String nombre, String apellido, int edad, String correo, String telefono, String direccion1,
                   String usuario, String contrasena, String rol, boolean agendaCita) {
        super(cedula, nombre, apellido, edad, correo, telefono, direccion1, usuario, contrasena, rol);
        this.agendaCita = agendaCita;
    }
    //getters y setters
    public boolean isAgendaCita() {
        return agendaCita;
    }

    public void setAgendaCita(boolean agendaCita) {
        this.agendaCita = agendaCita;
    }
}
