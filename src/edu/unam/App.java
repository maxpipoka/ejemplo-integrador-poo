package edu.unam;

import edu.unam.repositorios.Repositorio;
import edu.unam.servicios.Servicio;
import edu.unam.vistas.VistaDepartamentos;
import edu.unam.vistas.VistaEmpleados;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class App  extends Application {

    // cambiante
    Group cambiante = new Group();
    // creamos vistas
    VistaDepartamentos vistaDepartamentos;
    VistaEmpleados vistaEmpleados;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        // defino persistencia (Esto queda muy dependiente de JPA)
        // si quiero hacer menos dependiente hay que cambiar el formato del repositorio
        // hacer una interfaz y ahi crear los distintos tipos de repositorios
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("EmpresaPU");
        Servicio servicio = new Servicio(new Repositorio(emf));

        // vistas
        vistaDepartamentos = new VistaDepartamentos(servicio);
        vistaEmpleados = new VistaEmpleados(servicio);

        // botones
        Button botonDepartamentos = new Button("Departamentos");
        Button botonEmpleados = new Button("Empleados");
        Button botonProyectos = new Button("Proyectos");
        
        botonDepartamentos.setOnAction(e -> clicMostrarVistaDepartamento(stage));
        botonEmpleados.setOnAction(e -> clicMostrarVistaEmpleados(stage));
        botonProyectos.setOnAction(e -> clicMostrarVistaProyectos(stage));

        // separadores
        Separator separador1 = new Separator(Orientation.HORIZONTAL);
        Separator separador2 = new Separator(Orientation.HORIZONTAL);
        
        // contenedor de botones y separadores
        VBox contenedorBotones = new VBox();
        contenedorBotones.setAlignment(Pos.CENTER);
        contenedorBotones.setPadding(new Insets(10, 10, 10, 10));
        contenedorBotones.setSpacing(10);
        contenedorBotones.getChildren().addAll(botonDepartamentos, separador1, botonEmpleados, separador2, botonProyectos);
        // contenedor de botones y separadores con cambiante
        HBox contenedorPrincipal = new HBox();
        Separator separador3 = new Separator(Orientation.VERTICAL);
        // que muestro inicialmente
        cambiante.getChildren().add(vistaDepartamentos.obtenerVista());
        contenedorPrincipal.getChildren().addAll(contenedorBotones, separador3, cambiante);
                
        // escena  principal
        Scene escena = new Scene(contenedorPrincipal, 1200, 600);
        stage.setScene(escena);
        stage.setTitle("Empresa - Departamentos");
        stage.setResizable(false);
        stage.show();        
    }

    private void clicMostrarVistaProyectos(Stage stage) {
        cambiante.getChildren().clear();
        cambiante.getChildren().add(vistaDepartamentos.obtenerVista());
//        stage.sizeToScene();
//        stage.centerOnScreen();
        stage.setTitle("Empresa - Proyectos");
    }

    private void clicMostrarVistaEmpleados(Stage stage) {
        cambiante.getChildren().clear();
        cambiante.getChildren().add(vistaEmpleados.obtenerVista());
//        stage.sizeToScene();
//        stage.centerOnScreen();
        stage.setTitle("Empresa - Empleados");
    }

    private void clicMostrarVistaDepartamento(Stage stage) {
        cambiante.getChildren().clear();
        cambiante.getChildren().add(vistaDepartamentos.obtenerVista());
//        stage.sizeToScene();
//        stage.centerOnScreen();
        stage.setTitle("Empresa - Departamentos");
    }

}
