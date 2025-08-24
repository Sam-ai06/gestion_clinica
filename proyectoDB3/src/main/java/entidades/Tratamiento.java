package entidades;

import SQL.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Tratamiento {

    public static List<TratamientoRow> obtenerTratamientosPorDoctor(String cedulaDoctor) throws SQLException {
        String sql = "SELECT * FROM vw_tratamientos_doctor WHERE cedula_doctor = ? ORDER BY fecha DESC";
        try (Connection cn = DatabaseConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, cedulaDoctor);
            try (ResultSet rs = ps.executeQuery()) {
                List<TratamientoRow> out = new ArrayList<>();
                while (rs.next()) out.add(TratamientoRow.from(rs));
                return out;
            }
        }
    }

    public static class TratamientoRow {
        public final int id, citaId;
        public final String doctorCedula, clienteCedula, doctorNombre, doctorApellido, clienteNombre, clienteApellido;
        public final String receta;
        public final Timestamp fecha;

        private TratamientoRow(int id, int citaId, String dCed, String cCed, String dNom, String dApe, String cNom, String cApe, String receta, Timestamp fecha) {
            this.id = id; this.citaId = citaId; this.doctorCedula = dCed; this.clienteCedula=cCed;
            this.doctorNombre=dNom; this.doctorApellido=dApe; this.clienteNombre=cNom; this.clienteApellido=cApe;
            this.receta=receta; this.fecha=fecha;
        }
        public static TratamientoRow from(ResultSet rs) throws SQLException {
            return new TratamientoRow(
                    rs.getInt("tratamiento_id"),
                    rs.getInt("cita_id"),
                    rs.getString("cedula_doctor"),
                    rs.getString("cedula_cliente"),
                    rs.getString("doctor_nombre"),
                    rs.getString("doctor_apellido"),
                    rs.getString("cliente_nombre"),
                    rs.getString("cliente_apellido"),
                    rs.getString("receta"),
                    rs.getTimestamp("fecha")
            );
        }
    }
}
