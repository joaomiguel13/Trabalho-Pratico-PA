package pt.isec.pa.javalife.ui.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import pt.isec.pa.javalife.model.SimuladorManager;
import pt.isec.pa.javalife.ui.gui.resources.FontManager;
import pt.isec.pa.javalife.ui.gui.resources.ImageManager;

public class CreditsUI extends BorderPane {
    SimuladorManager simuladorManager;
    Font titleFont, textFont;

    Button btnGoBack;
    Label lblTitle, lblText;


    public CreditsUI(SimuladorManager simuladorManager) {
        this.simuladorManager = simuladorManager;

        titleFont = FontManager.loadFont("tittleFont.otf", 45);
        textFont = FontManager.loadFont("PressStart2P-Regular.ttf", 15);

        createViews();
        registerHandlers();
        update();
    }


    private void createViews() {
        this.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));

        lblTitle = new Label("creditos");
        lblTitle.setFont(titleFont);
        lblTitle.setId("credits");

        lblText = new Label(
                "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n" +
                        "~                                            ~\n" +
                        "~                                            ~\n" +
                        "~               DEIS-ISEC-IPC                ~\n" +
                        "~                                            ~\n" +
                        "~         LEI - Programação Avançada         ~\n" +
                        "~                                            ~\n" +
                        "~                2023/2024                   ~\n" +
                        "~                                            ~\n" +
                        "~            João Duarte - 2020122715        ~\n" +
                        "~            André Dias - 2021140917         ~\n" +
                        "~            Rúben Agostinho - 2020157100    ~\n" +
                        "~                                            ~\n" +
                        "~             Trabalho Académico             ~\n" +
                        "~                                            ~\n" +
                        "~                                            ~\n" +
                        "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        lblText.setFont(textFont);
        lblText.setId("credits");

        Image isec = ImageManager.getImage("isec.png");
        ImageView imageView = new ImageView(isec);
        imageView.setFitWidth(115);
        imageView.setFitHeight(115);


        VBox vBox = new VBox(lblTitle, lblText, imageView);
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(25);
        VBox.setMargin(lblText, new Insets(50, 0, 0, 0));

        btnGoBack = new Button();
        btnGoBack.setBackground(
                new Background(
                        new BackgroundImage(
                                ImageManager.getImage("backArrow.png"),
                                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                                BackgroundPosition.CENTER,
                                new BackgroundSize(1, 1, true, true, true, false)
                        )
                )
        );

        HBox hBox = new HBox(btnGoBack);
        hBox.setPadding(new Insets(20, 0, 0, 35));

        this.setCenter(vBox);
        this.setTop(hBox);
    }

    private void registerHandlers() {
        simuladorManager.addPropertyChangeListener(SimuladorManager.PROP_CREDITS, evt -> { update(); });

        btnGoBack.setOnAction(event -> {
            simuladorManager.setShowCredits(false);
        });
    }

    private void update() {
        if(simuladorManager.showCredits())
            this.setVisible(true);
        else
            this.setVisible(false);

    }
}
