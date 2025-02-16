/**
 * <p>
 * El archivo {@code module-info.java} es parte del sistema de modulos de Java (Java Platform Module System o JPMS), introducido
 * en Java 9. En el contexto de un proyecto JavaFX creado en IntelliJ, este archivo juega un papel importante en la definicion y
 * configuracion del modulo de la aplicacion.
 * <p>
 * El uso de {@code module-info.java} en proyectos JavaFX ayuda a:
 * <ul>
 * <li>Mejorar la modularidad y encapsulacion del codigo.
 * <li>Facilitar la gestion de dependencias.
 * <li>Permitir una mejor organizacion de aplicaciones grandes.
 * <li>Preparar la aplicacion para su distribucion como un modulo Java.
 * </ul>
 * Links:
 * <a href="https://edencoding.com/javafx-maven/">How To Effortlessly Setup JavaFX With Maven (+ debugging tips!) in IntelliJ</a>
 */
module com.punkipunk {

    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.sql;
    requires java.compiler;
    requires com.fasterxml.jackson.databind;
    requires org.lwjgl;
    requires org.lwjgl.openal;
    requires org.lwjgl.stb;

    opens com.punkipunk.controllers to javafx.fxml;
    opens com.punkipunk.core.api to javafx.fxml;
    opens com.punkipunk.scene to javafx.fxml;
    opens com.punkipunk.core to javafx.fxml;
    opens com.punkipunk.json to com.fasterxml.jackson.databind;

    exports com.punkipunk;
    exports com.punkipunk.world;
    exports com.punkipunk.world.system;
    exports com.punkipunk.input.keyboard;
    exports com.punkipunk.io;
    exports com.punkipunk.physics;
    exports com.punkipunk.ai;
    exports com.punkipunk.states;
    exports com.punkipunk.controllers;
    exports com.punkipunk.core.api;
    exports com.punkipunk.audio;
    exports com.punkipunk.scene;
    exports com.punkipunk.gui.container.equipment;
    exports com.punkipunk.gui.container;
    exports com.punkipunk.gui.container.inventory;
    exports com.punkipunk.core;
    exports com.punkipunk.json;
    exports com.punkipunk.json.model;
    exports com.punkipunk.entity;
    exports com.punkipunk.entity.combat;
    exports com.punkipunk.entity.components;
    exports com.punkipunk.entity.interactive;
    exports com.punkipunk.entity.item;
    exports com.punkipunk.entity.mob;
    exports com.punkipunk.entity.player;
    exports com.punkipunk.entity.spells;
    exports com.punkipunk.json.model.event;
    exports com.punkipunk.physics.event;
    exports com.punkipunk.json.model.map;

}