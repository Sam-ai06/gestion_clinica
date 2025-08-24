package entidades;
import java.util.List;
public class Recetas {
    private int id;
    private String fecha;
    private String paciente;
    private String doctor;
    private List<String> medicamentos;
    private String instrucciones;

    public Recetas(int id, String fecha, String paciente, String doctor, List<String> medicamentos, String instrucciones) {
        this.id = id;
        this.fecha = fecha;
        this.paciente = paciente;
        this.doctor = doctor;
        this.medicamentos = medicamentos;
        this.instrucciones = instrucciones;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getPaciente() {
        return paciente;
    }

    public void setPaciente(String paciente) {
        this.paciente = paciente;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public List<String> getMedicamentos() {
        return medicamentos;
    }

    public void setMedicamentos(List<String> medicamentos) {
        this.medicamentos = medicamentos;
    }

    public String getInstrucciones() {
        return instrucciones;
    }

    public void setInstrucciones(String instrucciones) {
        this.instrucciones = instrucciones;
    }
}