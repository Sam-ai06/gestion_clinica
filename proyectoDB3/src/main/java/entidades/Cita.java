package entidades;

import SQL.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Cita {
    String fecha;
    String paciente;
    String motivo;
    String estado;
    String verDetalles;

    public Cita(String fecha, String paciente, String motivo, String estado, String verDetalles) {
        this.fecha  = fecha;
        this.paciente = paciente;
        this.motivo = motivo;
        this.estado = estado;
        this.verDetalles = verDetalles;
    }

    public static boolean crearCita(
            String cedulaDoctor,
            String cedulaCliente,
            Date fecha,
            Time hora,
            String departamento,
            String descripcion,
            String observaciones,
            String diagnostico
    ) throws SQLException {

        String call = "{ CALL sp_crear_cita(?,?,?,?,?,?,?,?) }";
        try (Connection cn = DatabaseConnection.getConnection();
             CallableStatement cs = cn.prepareCall(call)) {
            cs.setString(1, cedulaDoctor);
            cs.setString(2, cedulaCliente);
            cs.setDate(3, fecha);
            cs.setTime(4, hora);
            cs.setString(5, departamento);
            cs.setString(6, descripcion);
            cs.setString(7, observaciones);
            cs.setString(8, diagnostico);
            cs.execute();
            return true;
        }
    }

    public static List<CitaRow> obtenerCitasPorDoctor(String cedulaDoctor) throws SQLException {
        String sql = "SELECT * FROM view_citas_doctor WHERE cedula_doctor = ? ORDER BY fecha, hora";
        try (Connection cn = DatabaseConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, cedulaDoctor);
            try (ResultSet rs = ps.executeQuery()) {
                List<CitaRow> out = new ArrayList<>();
                while (rs.next()) {
                    out.add(CitaRow.from(rs));
                }
                return out;
            }
        }
    }

    public static List<CitaClienteRow> obtenerCitasPorCliente(String cedulaCliente) throws SQLException {
        String sql = "SELECT * FROM vw_citas_cliente WHERE cedula_cliente = ? ORDER BY fecha DESC, hora DESC";
        try (Connection cn = DatabaseConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, cedulaCliente);
            try (ResultSet rs = ps.executeQuery()) {
                List<CitaClienteRow> out = new ArrayList<>();
                while (rs.next()) {
                    out.add(CitaClienteRow.from(rs));
                }
                return out;
            }
        }
    }

    public static boolean eliminarCita(int citaId, String cedulaCliente) throws SQLException {
        String call = "{ CALL sp_eliminar_cita(?, ?) }";
        try (Connection cn = DatabaseConnection.getConnection();
             CallableStatement cs = cn.prepareCall(call)) {
            cs.setInt(1, citaId);
            cs.setString(2, cedulaCliente);
            int rowsAffected = cs.executeUpdate();
            return rowsAffected > 0;
        }
    }


    public static List<DoctorInfo> obtenerDoctoresDisponibles() throws SQLException {
        String sql = "SELECT p.cedula, p.nombre, p.apellido, d.especialidad " +
                "FROM personas p JOIN doctores d ON p.cedula = d.cedula " +
                "WHERE p.rol = 'staff' ORDER BY p.apellido";
        try (Connection cn = DatabaseConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                List<DoctorInfo> doctores = new ArrayList<>();
                while (rs.next()) {
                    doctores.add(new DoctorInfo(
                            rs.getString("cedula"),
                            rs.getString("nombre"),
                            rs.getString("apellido"),
                            rs.getString("especialidad")
                    ));
                }
                return doctores;
            }
        }
    }

    public static class CitaRow {
        public final int citaId;
        public final String doctorCedula, doctorNombre, doctorApellido;
        public final String clienteCedula, clienteNombre, clienteApellido;
        public final Date fecha;
        public final Time hora;
        public final String departamento, estado, descripcion, observaciones, diagnostico;

        private CitaRow(int id, String dCed, String dNom, String dApe,
                        String cCed, String cNom, String cApe,
                        Date f, Time h, String dep, String est, String des, String obs, String diag) {
            citaId=id; doctorCedula=dCed; doctorNombre=dNom; doctorApellido=dApe;
            clienteCedula=cCed; clienteNombre=cNom; clienteApellido=cApe;
            fecha=f; hora=h; departamento=dep; estado=est; descripcion=des; observaciones=obs; diagnostico=diag;
        }

        public static CitaRow from(ResultSet rs) throws SQLException {
            return new CitaRow(
                    rs.getInt("cita_id"),
                    rs.getString("cedula_doctor"),
                    rs.getString("doctor_nombre"),
                    rs.getString("doctor_apellido"),
                    rs.getString("cedula_cliente"),
                    rs.getString("cliente_nombre"),
                    rs.getString("cliente_apellido"),
                    rs.getDate("fecha"),
                    rs.getTime("hora"),
                    rs.getString("departamento"),
                    rs.getString("estado"),
                    rs.getString("descripcion"),
                    rs.getString("observaciones"),
                    rs.getString("diagnostico")
            );
        }

        // Getters básicos
        public int getCitaId() {
            return citaId;
        }

        public String getDoctorCedula() {
            return doctorCedula;
        }

        public String getDoctorNombre() {
            return doctorNombre;
        }

        public String getDoctorApellido() {
            return doctorApellido;
        }

        public String getClienteCedula() {
            return clienteCedula;
        }

        public String getClienteNombre() {
            return clienteNombre;
        }

        public String getClienteApellido() {
            return clienteApellido;
        }

        public Date getFecha() {
            return fecha;
        }

        public Time getHora() {
            return hora;
        }

        public String getDepartamento() {
            return departamento;
        }

        public String getEstado() {
            return estado;
        }

        public String getDescripcion() {
            return descripcion;
        }

        public String getObservaciones() {
            return observaciones;
        }

        public String getDiagnostico() {
            return diagnostico;
        }

        // Métodos formateados para la tabla del doctor
        public String getFechaFormateada() {
            return fecha.toString();
        }

        public String getPaciente() {
            return clienteNombre + " " + clienteApellido;
        }

        public String getMotivo() {
            return descripcion != null ? descripcion : "Sin descripción";
        }

        public String getAcciones() {
            return "Ver detalles";
        }

        public String getHoraFormateada() {
            return hora.toString();
        }

        public String getFechaHoraCompleta() {
            return fecha.toString() + " " + hora.toString();
        }

        public String getDoctorCompleto() {
            return "Dr. " + doctorNombre + " " + doctorApellido;
        }
    }

    public static class CitaClienteRow {
        public final int citaId;
        public final String doctorNombre, doctorApellido, doctorEspecialidad;
        public final Date fecha;
        public final Time hora;
        public final String departamento, estado, descripcion;

        private CitaClienteRow(int id, String dNom, String dApe, String esp,
                               Date f, Time h, String dep, String est, String desc) {
            citaId = id;
            doctorNombre = dNom;
            doctorApellido = dApe;
            doctorEspecialidad = esp;
            fecha = f;
            hora = h;
            departamento = dep;
            estado = est;
            descripcion = desc;
        }

        public static CitaClienteRow from(ResultSet rs) throws SQLException {
            return new CitaClienteRow(
                    rs.getInt("cita_id"),
                    rs.getString("doctor_nombre"),
                    rs.getString("doctor_apellido"),
                    rs.getString("doctor_especialidad"),
                    rs.getDate("fecha"),
                    rs.getTime("hora"),
                    rs.getString("departamento"),
                    rs.getString("estado"),
                    rs.getString("descripcion")
            );
        }
        public int getCitaId() {
            return citaId;
        }

        public String getDoctorNombre() {
            return doctorNombre;
        }

        public String getDoctorApellido() {
            return doctorApellido;
        }

        public String getDoctorEspecialidad() {
            return doctorEspecialidad;
        }

        public Date getFecha() {
            return fecha;
        }

        public Time getHora() {
            return hora;
        }

        public String getDepartamento() {
            return departamento;
        }

        public String getEstado() {
            return estado;
        }

        public String getDescripcion() {
            return descripcion;
        }

        //MÉTODOS FORMATADOS PARA LA TABLA

        public String getDoctorCompleto() {
            return "Dr. " + doctorNombre + " " + doctorApellido;
        }

        public String getFechaFormateada() {
            return fecha.toString();
        }

        public String getHoraFormateada() {
            return hora.toString();
        }
    }

    public static class DoctorInfo {
        public final String cedula, nombre, apellido, especialidad;

        public DoctorInfo(String cedula, String nombre, String apellido, String especialidad) {
            this.cedula = cedula;
            this.nombre = nombre;
            this.apellido = apellido;
            this.especialidad = especialidad;
        }

        public String getNombreCompleto() {
            return "Dr. " + nombre + " " + apellido + " (" + especialidad + ")";
        }

        public String getCedula() {
            return cedula;
        }

        public String getNombre() {
            return nombre;
        }

        public String getApellido() {
            return apellido;
        }

        public String getEspecialidad() {
            return especialidad;
        }
    }
}