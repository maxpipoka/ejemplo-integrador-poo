package edu.unam.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "departamentos")
public class Departamento {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idDepartamento;
    @Column(name = "nombre_departamento", nullable = false, length = 50 )
    private String nombre;
    @Column(nullable = false)
    private boolean alta = true;
    @OneToMany (mappedBy = "departamento")
    private List<Empleado> empleados = new ArrayList<>();

    public Departamento() {
    }

    public Departamento(String nombre) {
        this.nombre = nombre;
    }
    
    public Long getIdDepartamento() {
        return idDepartamento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    
    public boolean isAlta() {
        return alta;
    }

    public void setAlta(boolean alta) {
        this.alta = alta;
    }

    public List<Empleado> getEmpleados() {
        return empleados;
    }

    public void setEmpleados(List<Empleado> empleados) {
        this.empleados = empleados;
    }

    @Override
    public String toString() {
        return idDepartamento + " " + nombre;
    }

    public void agregarEmpleado(Empleado empleado) {
        this.empleados.add(empleado);
    }
}
