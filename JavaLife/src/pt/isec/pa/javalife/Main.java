package pt.isec.pa.javalife;

import javafx.application.Application;
import pt.isec.pa.javalife.model.SimuladorManager;
import pt.isec.pa.javalife.ui.gui.MainJFX;

public class Main {
    /*public static SimuladorManager simuladorManager;
    static{
        simuladorManager = new SimuladorManager();
    }*/
    public static void main(String[] args) {
        Application.launch(MainJFX.class,args);
    }
}
