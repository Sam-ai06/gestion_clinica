package com.espol.proyectodb3;

import com.espol.proyectodb3.SQL.Cita;
import com.espol.proyectodb3.SQL.Tratamiento;
import javafx.collections.FXCollections;
import javafx.scene.control.*;

public class DoctorController {

    public TableView<Cita.CitaRow> tblCitas;
    public TableView<Tratamiento.TratamientoRow> tblRecetas;
    public Label lblInfo;

    private String cedulaDoctor;

    public void setCedulaDoctor(String cedula) { this.cedulaDoctor = cedula; }

    public void cargarCitasDelDoctor() {
        try {
            var lista = Cita.obtenerCitasPorDoctor(cedulaDoctor);
            tblCitas.setItems(FXCollections.observableArrayList(lista));
            lblInfo.setText("Citas: " + lista.size());
        } catch (Exception e) {
            lblInfo.setText("Error citas: " + e.getMessage());
        }
    }

    public void cargarRecetasDelDoctor() {
        try {
            var lista = Tratamiento.obtenerTratamientosPorDoctor(cedulaDoctor);
            tblRecetas.setItems(FXCollections.observableArrayList(lista));
        } catch (Exception e) {
            lblInfo.setText("Error recetas: " + e.getMessage());
        }
    }
}
