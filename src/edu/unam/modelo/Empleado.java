package edu.unam.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Empleado {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idEmpleado;
    private String nombres;
    private String apellidos;
    @ManyToMany
    private Set<Proyecto> proyectos = new HashSet<>();
    @ManyToOne
    private Departamento departamento;

    public Empleado() {
    }

    public Empleado(String nombres, String apellidos, Departamento departamento) {
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.departamento = departamento;
        departamento.agregarEmpleado(this);
    }

    public Long getIdEmpleado() {
        return idEmpleado;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
        // REVISAR
    }

    public Set<Proyecto> getProyectos() {
        return proyectos;
    }

    public void setProyectos(Set<Proyecto> proyectos) {
        this.proyectos = proyectos;
        // REVISAR
    }

    public void agregarProyecto(Proyecto proyecto) {
        this.proyectos.add(proyecto);
    }

    @Override
    public String toString() {
        return idEmpleado + " " + nombres + " " + apellidos;
    }
}
