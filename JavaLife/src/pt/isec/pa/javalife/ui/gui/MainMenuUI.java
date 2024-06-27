package pt.isec.pa.javalife.ui.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import pt.isec.pa.javalife.model.SimuladorManager;
import pt.isec.pa.javalife.ui.gui.resources.FontManager;

public class MainMenuUI extends BorderPane {
    SimuladorManager simuladorManager;


    Font titleFont, buttonsFont;
    Label lblTitle;

    Button btnStart, btnCredits, btnExit;
    public MainMenuUI(SimuladorManager simuladorManager) {
        this.simuladorManager = simuladorManager;

        titleFont = FontManager.loadFont("tittleFont.otf", 69);
        buttonsFont = FontManager.loadFont("PressStart2P-Regular.ttf", 30);

        createViews();
        registerHandlers();
        update();
    }

    private void createViews() {
        this.setBackground(new Background(new BackgroundFill(Color.BLACK, null,null)));
        this.setStyle("-fx-background-color: #000000");

        lblTitle = new Label("JavaLife");
        lblTitle.setFont(titleFont);
        lblTitle.setId("mainMenuTitle");
        lblTitle.setTextFill(Color.GREEN);

        btnStart = new Button("INICAR SIMULADOR");
        btnStart.setFont(buttonsFont);
        btnStart.setId("mainMenuButton");
        btnStart.setStyle("-fx-background-color: #A8B833");

        btnCredits = new Button("CRÉDITOS");
        btnCredits.setFont(buttonsFont);
        btnCredits.setId("mainMenuButton");
        btnCredits.setStyle("-fx-background-color: #425641");

        btnExit = new Button("SAIR");
        btnExit.setFont(buttonsFont);
        btnExit.setId("mainMenuButton");
        btnExit.setStyle("-fx-background-color: #b6d5a8");

        VBox vBox = new VBox(lblTitle, btnStart, btnCredits, btnExit);
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(15);
        vBox.setMargin(btnStart, new Insets(75,0,0,0));

        this.setCenter(vBox);


    }

    private void registerHandlers() {
        simuladorManager.addPropertyChangeListener(SimuladorManager.PROP_SIMULATION_STARTED, evt -> {update();});
        simuladorManager.addPropertyChangeListener(SimuladorManager.PROP_CREDITS, evt -> { update(); });

        btnStart.setOnAction(e -> {
            simuladorManager.startEcossistema();
            simuladorManager.setEcossistemaStarted(true);
        });

        btnCredits.setOnAction(e -> {
            simuladorManager.setShowCredits(true);
        });

        btnExit.setOnAction(e -> {
            System.exit(0);
        });
    }

    private void update() {
        if(simuladorManager.getEcossistemaStarted() || simuladorManager.showCredits())
            this.setVisible(false);
        else
            this.setVisible(true);
    }
}
