package entidades;

import SQL.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Cita {

    public Cita(String fecha, String paciente, String motivo, String estado, String verDetalles) {
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
        String sql = "SELECT * FROM vw_citas_doctor WHERE cedula_doctor = ? ORDER BY fecha, hora";
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

    // DTO simple para tablas JavaFX
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
    }
}