package edu.unam.vistas;

import edu.unam.servicios.Servicio;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import edu.unam.modelo.Departamento;

public class VistaDepartamentos implements Vista {

    // el servicio con el que se va a comunicar la vista
    private final Servicio servicio;
    // objetos de la pantalla
    TableView<Departamento> tabla;
    TableColumn<Departamento, Long> columnaIdDepartamento;
    TableColumn<Departamento, Double> columnaNombre;
    Label etiquetaIdDepartamento;
    TextField entradaNombre;
    Button botonNuevo, botonAgregar, botonEliminar;
    // Departamento
    private Departamento departamentoSeleccionado;

    public VistaDepartamentos(Servicio servicio) {
        this.servicio = servicio;
    }

    @Override
    public Parent obtenerVista() {
        // creamos las cajas de texto
        etiquetaIdDepartamento = new Label("--");
        entradaNombre = new TextField();
        entradaNombre.setPromptText("Nombre del departamento");

        // creamos tres botones y asociamos eventos
        botonNuevo = new Button("Nuevo");
        botonNuevo.setOnAction(e -> clicNuevoDepartamento());
        botonAgregar = new Button("Agregar");
        botonAgregar.setOnAction(e -> clicAgregarDepartamento());
        botonEliminar = new Button("Eliminar");
        botonEliminar.setOnAction(e -> clicEliminarDepartamento());

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
        contenedorCarga.getChildren().addAll(contenedorBotones, etiquetaIdDepartamento, entradaNombre);

        // definimos las columnas y sus propiedades
        columnaIdDepartamento = new TableColumn<>("Id");
        columnaIdDepartamento.setMinWidth(100);
        columnaIdDepartamento.setCellValueFactory(new PropertyValueFactory<>("idDepartamento"));
        columnaNombre = new TableColumn<>("Nombre");
        columnaNombre.setMinWidth(300);
        columnaNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));

        // creamos la tabla
        tabla = new TableView<>();
        // opcional (SELECCIONAR UN ELEMENTO)
        tabla.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        // asociamos los datos
        tabla.getItems().addAll(this.servicio.listarDepartamentos());
        // agregamos las columnas a la tabla
        tabla.getColumns().add(columnaIdDepartamento);
        tabla.getColumns().add(columnaNombre);
        tabla.getSelectionModel().selectedItemProperty().addListener(e -> cargarDatos());
        // creamos un contenedor para la tabla y el contenedor de carga
        VBox contenedor = new VBox();
        // agregamos al contenedor la tabla
        contenedor.getChildren().addAll(tabla, contenedorCarga);

        return contenedor;

    }

    private void clicNuevoDepartamento() {
        // quito seleccionado de tabla
        tabla.getSelectionModel().clearSelection();
        // uso clear para limpiar
        etiquetaIdDepartamento.setText("");
        entradaNombre.clear();
    }

    private void clicEliminarDepartamento() {
        departamentoSeleccionado = tabla.getSelectionModel().getSelectedItem();
        if (departamentoSeleccionado != null) {
            servicio.eliminarDepartamento(departamentoSeleccionado.getIdDepartamento());
            limpiar();
        }
    }

    private void clicAgregarDepartamento() {
        // asumo selección simple
        departamentoSeleccionado = tabla.getSelectionModel().getSelectedItem();
        try {
            if (departamentoSeleccionado == null) {
                // Si no hay elemento seleccionado en la tabla
                servicio.agregarDepartamento(entradaNombre.getText());
            } else {
                // cambiamos el empleado
                servicio.editarDepartamento(Long.parseLong(etiquetaIdDepartamento.getText()), entradaNombre.getText());
            }
            limpiar();
        } catch (IllegalArgumentException e) {
            mostrarAlerta(AlertType.ERROR, "Error", "Error al guardar", e.getMessage());
        }
    }


    private void cargarDatos() {
        departamentoSeleccionado = tabla.getSelectionModel().getSelectedItem();
        if (departamentoSeleccionado != null) {
            etiquetaIdDepartamento.setText(String.valueOf(departamentoSeleccionado.getIdDepartamento()));
            entradaNombre.setText(departamentoSeleccionado.getNombre());
        }
    }

    private void limpiar() {
        // limpiamos
        etiquetaIdDepartamento.setText("");
        entradaNombre.clear();
        tabla.getItems().clear();
        tabla.getItems().addAll(this.servicio.listarDepartamentos());
    }
}
