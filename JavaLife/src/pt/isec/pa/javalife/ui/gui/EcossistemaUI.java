package pt.isec.pa.javalife.ui.gui;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Window;
import pt.isec.pa.javalife.model.SimuladorManager;
import pt.isec.pa.javalife.model.data.*;
import pt.isec.pa.javalife.model.gameengine.GameEngine;
import pt.isec.pa.javalife.ui.gui.resources.ImageManager;

import java.util.HashSet;
import java.util.Set;

public class EcossistemaUI extends BorderPane {
    SimuladorManager simuladorManager;
    Canvas simuladorCanvas;
    GraphicsContext ctx;
    VBox vBox;
    Set<IElemento> elementos;
    int larguraCanvas, alturaCanvas;
    double scaleX = 1.0, scaleY = 1.0;

    public EcossistemaUI(SimuladorManager simuladorManager, int larguraCanvas, int alturaCanvas) {
        this.simuladorManager = simuladorManager;
        this.larguraCanvas = larguraCanvas;
        this.alturaCanvas = alturaCanvas;
        createViews();
        registerHandlers();
        update();
    }

    private void createViews() {
        this.setTop(vBox = new VBox(new TopBarMenu(simuladorManager)));
        vBox.setBackground(new Background(new BackgroundFill(Color.GRAY, null, null)));

        simuladorCanvas = new Canvas(larguraCanvas, alturaCanvas);
        ctx = simuladorCanvas.getGraphicsContext2D();
        ctx.setFill(Color.BLACK);
        ctx.fillRect(0, 0, simuladorCanvas.getWidth(), simuladorCanvas.getHeight());
        this.setCenter(simuladorCanvas);

        // Add listener to the sceneProperty of the canvas
        simuladorCanvas.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                // Add listener to the windowProperty of the scene
                newScene.windowProperty().addListener((windowObs, oldWindow, newWindow) -> {
                    if (newWindow != null) {
                        newWindow.widthProperty().addListener((obs2, oldVal, newVal) -> resizeCanvas(newWindow));
                        newWindow.heightProperty().addListener((obs2, oldVal, newVal) -> resizeCanvas(newWindow));
                    }
                });
            }
        });
    }

    private void registerHandlers() {
        simuladorManager.addPropertyChangeListener(SimuladorManager.PROP_SIMULATION_STARTED, evt -> Platform.runLater(this::update));
        simuladorManager.addPropertyChangeListener(SimuladorManager.PROP_UPDATE_ECO, evt -> Platform.runLater(this::update));
    }

    private void resizeCanvas(Window window) {
        double newWidth = window.getWidth();
        double newHeight = window.getHeight();
        scaleX = newWidth / larguraCanvas;
        scaleY = newHeight / alturaCanvas;
        simuladorCanvas.setWidth(newWidth);
        simuladorCanvas.setHeight(newHeight);
        update(); // Redraw the canvas with new scale
    }

    private void update() {
        if (!simuladorManager.getEcossistemaStarted()) {
            this.setVisible(false);
        } else {
            this.setVisible(true);
            ctx.clearRect(0, 0, simuladorCanvas.getWidth(), simuladorCanvas.getHeight());
            elementos = simuladorManager.getElementos();
            Set<IElemento> elementosCopia = new HashSet<>(elementos);

            for (IElemento elemento : elementosCopia) {
                if (elemento.getType()== Elemento.INANIMADO) {
                    Area area = elemento.getArea();
                    Image image = ImageManager.getImage("rectangle_i.png");
                    ctx.drawImage(image,scale(area.esquerda(),scaleX),scale(area.cima(),scaleY),
                            scale(area.direita()-area.esquerda(),scaleX),
                            scale(area.baixo() - area.cima(),scaleY));

                } else if (elemento.getType()== Elemento.FAUNA) {
                    Area area = elemento.getArea();
                    Image image = ImageManager.getImage("cabeca-de-tigre.png");
                    // Barra de vida atual
                    ctx.setFill(Color.RED);
                    ctx.fillRect(scale(area.esquerda(), scaleX), scale(area.cima(), scaleY) - 6,
                            scale((area.direita() - area.esquerda()) * (((Fauna) elemento).getForca() / 100), scaleX), 5);

                    ctx.drawImage(image, scale(area.esquerda(), scaleX), scale(area.cima(), scaleY),
                            scale(area.direita() - area.esquerda(), scaleX),
                            scale(area.baixo() - area.cima(), scaleY));

                } else if (elemento.getType()== Elemento.FLORA) {
                    Area area = elemento.getArea();
                    Image image = ImageManager.getImage(((Flora) elemento).getImagem());
                    ctx.setGlobalAlpha(((Flora) elemento).getForca() / 100);
                    ctx.drawImage(image, scale(area.esquerda(), scaleX), scale(area.cima(), scaleY),
                            scale(area.direita() - area.esquerda(), scaleX),
                            scale(area.baixo() - area.cima(), scaleY));
                    ctx.setGlobalAlpha(1);
                }
            }
        }
    }

    private double scale(double value, double scaleFactor) {
        return value * scaleFactor;
    }
}