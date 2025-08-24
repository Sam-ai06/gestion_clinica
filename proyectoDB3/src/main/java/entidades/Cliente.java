package entidades;

public class Cliente extends Persona {
    boolean agendaCita = true;

    // Constructor corregido
    public Cliente(String cedula, String nombre, String apellido, String edad, String correo, boolean agendaCita) {
        super(cedula, nombre, apellido, edad, correo);
        this.agendaCita = agendaCita;
    }

    // Constructor alternativo (ajustado)
    public Cliente(String nombre, String apellido, String cedula, String edad, String correo) {
        super(cedula, nombre, apellido, edad, correo);
    }

    // Getters y setters
    public boolean isAgendaCita() {
        return agendaCita;
    }

    public void setAgendaCita(boolean agendaCita) {
        this.agendaCita = agendaCita;
    }
}