/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unam.servicios;

import edu.unam.repositorios.*;

import java.util.ArrayList;
import java.util.List;
import edu.unam.modelo.Departamento;
import edu.unam.modelo.Empleado;

/**
 *
 * @author cbiale
 */
public class Servicio {

    private Repositorio repositorio;

    public Servicio(Repositorio p) {
        this.repositorio = p;
    }

    public List<Empleado> listarEmpleados() {
        return this.repositorio.buscarTodos(Empleado.class);
        // cambiar por buscar todos ordenados por
    }

    public Empleado buscarEmpleado(Long idEmpleado) {
        return this.repositorio.buscar(Empleado.class, idEmpleado);
    }

    public void agregarEmpleado(String nombres, String apellidos, Departamento departamento) {
        if (nombres.trim().length() == 0 || apellidos.trim().length() == 0 || 
            departamento == null) {
            throw new IllegalArgumentException("Faltan datos");
        }
        this.repositorio.iniciarTransaccion();
        Empleado empleado = new Empleado(nombres.toUpperCase().trim(), apellidos.toUpperCase().trim(), departamento);
        this.repositorio.insertar(empleado);
        this.repositorio.confirmarTransaccion();
    }

    // cambiar valor devuelto (por ejemplo: True ok, False problemas)
    public void editarEmpleado(Long idEmpleado, String nombres, String apellidos, Departamento departamento) {
        if (nombres.trim().length() == 0 || apellidos.trim().length() == 0 || 
            departamento == null) {
            throw new IllegalArgumentException("Faltan datos");
        }
        this.repositorio.iniciarTransaccion();
        Empleado empleado = this.repositorio.buscar(Empleado.class, idEmpleado);
        if (empleado != null) {
            empleado.setApellidos(apellidos);
            empleado.setNombres(nombres);
            // implementar comparable o comparator
            // o si el id es unico pueden compararar por id
            if (! empleado.getDepartamento().equals(departamento)) {
                empleado.getDepartamento().getEmpleados().remove(empleado);
                empleado.setDepartamento(departamento);
                departamento.getEmpleados().add(empleado);
            }
            this.repositorio.modificar(empleado);
            this.repositorio.confirmarTransaccion();
        } else {
            this.repositorio.descartarTransaccion();
        }
    }

    public int eliminarEmpleado(Long idEmpleado) {
        this.repositorio.iniciarTransaccion();
        Empleado empleado = this.repositorio.buscar(Empleado.class, idEmpleado);
        // como se soluciona??
        if (empleado != null && empleado.getProyectos().isEmpty() ) {
            this.repositorio.eliminar(empleado);
            this.repositorio.confirmarTransaccion();
            return 0;
        } else {
            this.repositorio.descartarTransaccion();
            return 1;
        }
    }

    // departamentos
    public List<Departamento> listarDepartamentos() {
        var departamentosRepositorio = this.repositorio.buscarTodos(Departamento.class);
        var listado = new ArrayList<Departamento>();
        for (var departamento : departamentosRepositorio) {
            if (departamento.isAlta()) {
                listado.add(departamento);
            }
        }
        return listado;
    }

    public void eliminarDepartamento(Long idDepartamento) {
        this.repositorio.iniciarTransaccion();
        Departamento departamento = this.repositorio.buscar(Departamento.class, idDepartamento);
        departamento.setAlta(false);
        this.repositorio.modificar(departamento);
        this.repositorio.confirmarTransaccion();
        /*
        // SI USAN ESTO VER DE CAMBIAR EL METODO A QUE RETORNE int
        this.repositorio.iniciarTransaccion();
        Departamento departamento = this.repositorio.buscar(Departamento.class, idDepartamento);
        // como se soluciona??
        if (departamento != null && departamento.getEmpleados().isEmpty() ) {
            this.repositorio.eliminar(departamento);
            this.repositorio.confirmarTransaccion();
            return 0;
        } else {
            this.repositorio.descartarTransaccion();
            return 1;
        }
        */
    }

    public void agregarDepartamento(String nombre) {
        // si se repite pasar a método privado
        if (nombre.trim().length() == 0) {
            // manejar una excepción
            throw new IllegalArgumentException("Nombre sin datos");
        }
        this.repositorio.iniciarTransaccion();
        Departamento departamento = new Departamento(nombre.toUpperCase().trim());
        this.repositorio.insertar(departamento);
        this.repositorio.confirmarTransaccion();
    }

    public void editarDepartamento(Long idDepartamento, String nombre) {
        this.repositorio.iniciarTransaccion();
        Departamento departamento = this.repositorio.buscar(Departamento.class, idDepartamento);
        if (departamento != null) {
            departamento.setNombre(nombre.toUpperCase().trim());
            this.repositorio.modificar(departamento);
            this.repositorio.confirmarTransaccion();
        } else {
            this.repositorio.descartarTransaccion();
        }
    }
    
}
