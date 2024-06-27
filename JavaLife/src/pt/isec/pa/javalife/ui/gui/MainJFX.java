package pt.isec.pa.javalife.ui.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pt.isec.pa.javalife.Main;
import pt.isec.pa.javalife.model.SimuladorManager;

public class MainJFX extends Application {
    SimuladorManager simuladorManager;

    @Override
    public void init() throws Exception {
        super.init();
        simuladorManager = new SimuladorManager(800,600, 1000);
    }
    @Override
    public void start(Stage stage) {
        //initGUI(new Stage(), "JavaLife");
        createOneStage(stage);
        //createOneStage(new Stage());
    }

    private void createOneStage(Stage stage){
        RootPane root = new RootPane(simuladorManager,800,600);
        Scene scene = new Scene(root, 800 , 600);
        stage.setScene(scene);
        //stage.setFullScreen(true);
        stage.setTitle("JavaLife");
        stage.show();
    }

    /*private void initGUI(Stage stage, String title) {
        RootPane rootPane = new RootPane(simuladorManager, 800, 600);
        Scene scene = new Scene(rootPane, 800, 600);
        stage.setFullScreen(true);
        stage.setScene(scene);
        stage.setTitle(title);
        stage.show();
    }*/

    @Override
    public void stop() throws Exception{
        super.stop();
    }
}
