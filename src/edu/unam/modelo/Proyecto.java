package edu.unam.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Proyecto {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idProyecto;
    private String descripcion;
    @ManyToMany (mappedBy = "proyectos")
    private Set<Empleado> empleados = new HashSet<>();

    public Proyecto() {
    }

    public Proyecto(String descripcion) {
        this.descripcion = descripcion;
    }

    public Long getIdProyecto() {
        return idProyecto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Set<Empleado> getEmpleados() {
        return empleados;
    }

    public void setEmpleados(Set<Empleado> empleados) {
        this.empleados = empleados;
    }

    public void agregarEmpleado(Empleado e) {
        this.empleados.add(e);
        e.agregarProyecto(this);
    }

    // QUE SUCEDE SI QUITO A UN EMPLEADO DEL PROYECTO

    @Override
    public String toString() {
        return idProyecto + " " + descripcion;
    }
}
