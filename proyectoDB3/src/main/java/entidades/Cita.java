package entidades;

public class Cita {
    private String fecha;
    private String nombrePaciente;
    private String motivo;
    private String estado;
    private String acciones;

    //constructor de la clase
    public Cita(String fecha, String nombrePaciente, String motivo, String estado, String acciones) {
        this.fecha = fecha;
        this.nombrePaciente = nombrePaciente;
        this.motivo = motivo;
        this.estado = estado;
        this.acciones = acciones;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getNombrePaciente() {
        return nombrePaciente;
    }

    public void setNombrePaciente(String nombrePaciente) {
        this.nombrePaciente = nombrePaciente;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getAcciones() {
        return acciones;
    }

    public void setAcciones(String acciones) {
        this.acciones = acciones;
    }
}
