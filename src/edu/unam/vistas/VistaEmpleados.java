package edu.unam.vistas;

import edu.unam.servicios.Servicio;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import edu.unam.modelo.Departamento;
import edu.unam.modelo.Empleado;

public class VistaEmpleados implements Vista {

    // el servicio con el que se va a comunicar la vista
    private final Servicio servicio;
    // objetos de la pantalla
    TableView<Empleado> tabla;
    TableColumn<Empleado, Long> columnaIdEmpleado;
    TableColumn<Empleado, String> columnaNombres;
    TableColumn<Empleado, String> columnaApellidos;
    TableColumn<Empleado, Departamento> columnaDepartamento;
    Label etiquetaIdEmpleado;
    TextField entradaNombres, entradaApellidos;
    ComboBox<Departamento> departamentos;
    Button botonNuevo, botonAgregar, botonEliminar;
    // Departamento
    private Empleado empleadoSeleccionado;

    public VistaEmpleados(Servicio servicio) {
        this.servicio = servicio;
    }

    @Override
    public Parent obtenerVista() {
        // creamos las cajas de texto
        etiquetaIdEmpleado = new Label("--");
        entradaNombres = new TextField();
        entradaNombres.setPromptText("Nombres del empleado");
        entradaApellidos = new TextField();
        entradaApellidos.setPromptText("Apellidos del empleado");
        departamentos = new ComboBox<>();
        departamentos.getItems().addAll(servicio.listarDepartamentos());

        // creamos tres botones y asociamos eventos
        botonNuevo = new Button("Nuevo");
        botonNuevo.setOnAction(e -> clicNuevoEmpleado());
        botonAgregar = new Button("Agregar");
        botonAgregar.setOnAction(e -> clicAgregarEmpleado());
        botonEliminar = new Button("Eliminar");
        botonEliminar.setOnAction(e -> clicEliminarEmpleado());

        // creamos un contenedor para los botones
        HBox contenedorBotones = new HBox();
        contenedorBotones.setPadding(new Insets(10, 10, 10, 10));
        contenedorBotones.setSpacing(10);

        // agregamos al contenedor las entradas y botones
        contenedorBotones.getChildren().addAll(botonNuevo, botonAgregar, botonEliminar);

        // creamos un contenedor para la carga
        VBox contenedorCarga = new VBox();
        contenedorCarga.setPadding(new Insets(10, 10, 10, 10));
        contenedorCarga.setSpacing(10);
        contenedorCarga.getChildren().addAll(contenedorBotones, etiquetaIdEmpleado, entradaNombres, entradaApellidos, departamentos);

        // definimos las columnas y sus propiedades
        columnaIdEmpleado = new TableColumn<>("Id");
        columnaIdEmpleado.setMinWidth(100);
        columnaIdEmpleado.setCellValueFactory(new PropertyValueFactory<>("idEmpleado"));
        columnaNombres = new TableColumn<>("Nombres");
        columnaNombres.setMinWidth(300);
        columnaNombres.setCellValueFactory(new PropertyValueFactory<>("nombres"));
        columnaApellidos = new TableColumn<>("Apellidos");
        columnaApellidos.setMinWidth(300);
        columnaApellidos.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
        columnaDepartamento = new TableColumn<>("Departamento");
        columnaDepartamento.setMinWidth(300);
        columnaDepartamento.setCellValueFactory(new PropertyValueFactory<>("departamento"));

        // creamos la tabla
        tabla = new TableView<>();
        // opcional (SELECCIONAR UN ELEMENTO)
        tabla.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        // asociamos los datos
        tabla.getItems().addAll(this.servicio.listarEmpleados());
        // agregamos las columnas a la tabla
        tabla.getColumns().add(columnaIdEmpleado);
        tabla.getColumns().add(columnaNombres);
        tabla.getColumns().add(columnaApellidos);
        tabla.getColumns().add(columnaDepartamento);
        tabla.getSelectionModel().selectedItemProperty().addListener(e -> cargarDatos());
        // creamos un contenedor para la tabla y el contenedor de carga
        VBox contenedor = new VBox();
        // agregamos al contenedor la tabla
        contenedor.getChildren().addAll(tabla, contenedorCarga);

        return contenedor;

    }

    private void clicNuevoEmpleado() {
        // quito seleccionado de tabla
        tabla.getSelectionModel().clearSelection();
        // uso clear para limpiar
        etiquetaIdEmpleado.setText("");
        entradaNombres.clear();
        entradaApellidos.clear();
        // falta departamento
    }

    private void clicEliminarEmpleado() {
        empleadoSeleccionado = tabla.getSelectionModel().getSelectedItem();
        if (empleadoSeleccionado != null) {
            servicio.eliminarEmpleado(empleadoSeleccionado.getIdEmpleado());
            limpiar();
        }
    }

    private void clicAgregarEmpleado() {
        // asumo selección simple
        empleadoSeleccionado = tabla.getSelectionModel().getSelectedItem();
        try {
            if (empleadoSeleccionado == null) {
                // Si no hay elemento seleccionado en la tabla
                servicio.agregarEmpleado(entradaNombres.getText(), entradaApellidos.getText(), departamentos.getSelectionModel().getSelectedItem());
            } else {
                // cambiamos el empleado
                servicio.editarEmpleado(Long.parseLong(etiquetaIdEmpleado.getText()), entradaNombres.getText(), entradaApellidos.getText(), departamentos.getSelectionModel().getSelectedItem());
            }
            limpiar();
        } catch (IllegalArgumentException e) {
            mostrarAlerta(AlertType.ERROR, "Error", "Error al guardar", e.getMessage());
        }
    }


    private void cargarDatos() {
        empleadoSeleccionado = tabla.getSelectionModel().getSelectedItem();
        if (empleadoSeleccionado != null) {
            etiquetaIdEmpleado.setText(String.valueOf(empleadoSeleccionado.getIdEmpleado()));
            entradaNombres.setText(empleadoSeleccionado.getNombres());
            entradaApellidos.setText(empleadoSeleccionado.getApellidos());
            departamentos.getSelectionModel().select(empleadoSeleccionado.getDepartamento());
        }
    }

    private void limpiar() {
        // limpiamos
        etiquetaIdEmpleado.setText("");
        entradaNombres.clear();
        entradaApellidos.clear();
        departamentos.getItems().clear();
        departamentos.getItems().addAll(servicio.listarDepartamentos());
        tabla.getItems().clear();
        tabla.getItems().addAll(this.servicio.listarEmpleados());
    }
}
