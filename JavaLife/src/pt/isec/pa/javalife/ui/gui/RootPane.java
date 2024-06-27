package pt.isec.pa.javalife.ui.gui;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import pt.isec.pa.javalife.model.SimuladorManager;

public class RootPane extends BorderPane {
    SimuladorManager simuladorManager;

    EcossistemaUI ecossistemaUI;

    Pane areaPane;
    int larguraCanvas, alturaCanvas;

    public RootPane(SimuladorManager simuladorManager, int larguraCanvas, int alturaCanvas) {
        this.simuladorManager = simuladorManager;
        this.larguraCanvas = larguraCanvas;
        this.alturaCanvas = alturaCanvas;
        createViews();
        registerHandlers();
        update();
    }

    private void createViews() {
        StackPane stackPane = new StackPane(
                new MainMenuUI(simuladorManager),
                new EcossistemaUI(simuladorManager, larguraCanvas, alturaCanvas),
                new CreditsUI(simuladorManager)
        );


        this.setCenter(stackPane);
    }

    private void registerHandlers() {


    }

    private void update() {

    }
}
