package pt.isec.pa.javalife.model;

import pt.isec.pa.javalife.model.command.*;
import pt.isec.pa.javalife.model.data.*;
import pt.isec.pa.javalife.model.gameengine.GameEngine;
import pt.isec.pa.javalife.model.gameengine.GameEngineState;
import pt.isec.pa.javalife.model.gameengine.IGameEngine;
import pt.isec.pa.javalife.model.memento.MyOriginator;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.*;
import java.util.Set;

public class SimuladorManager {
    PropertyChangeSupport pcs;
    CommandManager commandManager;
    MyOriginator originator;

    public static final String PROP_SIMULATION_STARTED = "_simulatior_start_";
    public static final String PROP_UPDATE_ECO = "_update_eco_";
    public static final String PROP_CREDITS = "_credits_";


    private int largura;
    private int altura;
    private int unidTemporal = 1000;
    private Ecossistema ecossistema;
    private boolean ecossistemaStarted;
    private IGameEngine gameEngine;

    private double forcaDefaultFlora;
    private double ganhoForcaFaunaAlimento;
    private double perdaForcaFloraAlimento;
    private double forcaDefaultFauna;
    private double perdaForcaFaunaMovimento;

    public SimuladorManager(int largura, int altura, int unidTemporal) {
        this.largura = largura;
        this.altura = altura;
        this.unidTemporal = unidTemporal;
        this.commandManager = new CommandManager();

        ecossistemaStarted = false;
        pcs = new PropertyChangeSupport(this);

        this.addPropertyChangeListener(PROP_CREDITS, evt -> {});
    }

    public SimuladorManager() {
        pcs = new PropertyChangeSupport(this);
        ecossistemaStarted = false;
        this.commandManager = new CommandManager();
    }

    public void addPropertyChangeListener(String property, PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(property, listener);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public int getLargura() {
        return largura;
    }

    public int getAltura() {
        return altura;
    }

    public int getUnidTemporal() {
        return unidTemporal;
    }

    public Set<IElemento> getElementos() {
        if (ecossistema.getElementos() != null) {
            return ecossistema.getElementos();
        } else {
            return null;
        }
    }

    public void startEcossistema() {
        System.out.println("Starting ecossistema...");
        ecossistema = new Ecossistema(altura, largura, unidTemporal);
        gameEngine = new GameEngine();
        gameEngine.registerClient(ecossistema);
        gameEngine.start(unidTemporal);

        ecossistema.addPropertyChangeListener("ola", evt -> {
            updateMap();
        });
    }

    public void updateMap() {
        pcs.firePropertyChange(PROP_UPDATE_ECO, null, null);
    }

    public boolean getEcossistemaStarted() {
        return ecossistemaStarted;
    }

    public void setEcossistemaStarted(boolean ecossistemaStarted) {
        this.ecossistemaStarted = ecossistemaStarted;
        pcs.firePropertyChange(PROP_SIMULATION_STARTED, null, null);
    }

    public void addFauna() {
        commandManager.invokeCommand(new AddFaunaCommand(ecossistema, forcaDefaultFauna, perdaForcaFaunaMovimento, ganhoForcaFaunaAlimento));
        updateMap();
    }

    public void addFlora() {
        commandManager.invokeCommand(new AddFloraCommand(ecossistema, forcaDefaultFlora, perdaForcaFloraAlimento));
        updateMap();
    }

    public void addInanimados() {
        commandManager.invokeCommand(new AddInanimadoCommand(ecossistema));
        updateMap();
    }

    public boolean pause() {
        if (ecossistemaStarted && gameEngine != null) {
            if (gameEngine.getCurrentState() == GameEngineState.PAUSED) {
                gameEngine.resume();
                return false;
            } else if (gameEngine.getCurrentState() == GameEngineState.RUNNING) {
                gameEngine.pause();
                return true;
            }

        }
        return false;
    }

    public void herbicida(double x, double y) {
        commandManager.invokeCommand(new ApplyHerbicidaCommand(ecossistema, x, y));
        updateMap();
    }






    public void strengh(double x, double y) {
        commandManager.invokeCommand(new ApplyForcaCommand(ecossistema, x, y));
        updateMap();
    }

    public void sun() {
        commandManager.invokeCommand(new ApplySolCommand(ecossistema));
        updateMap();
    }

    public void undo() {
        if (hasUndo()) {
            commandManager.undo();
            updateMap();
        }
    }

    public void redo() {
        if (hasRedo()) {
            commandManager.redo();
            updateMap();
        }
    }

    public boolean hasUndo() {
        return commandManager.hasUndo();
    }

    public boolean hasRedo() {
        return commandManager.hasRedo();
    }


    public void saveSnap() {
        originator = new MyOriginator();
        originator.save(ecossistema.getElementos());
    }

    public void restoreSnap() {
        if (originator != null) {
            ecossistema.replaceElementos(originator.restore());
            updateMap();
        }
    }

    public void saveEcossistema(File file) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(file)
        )) {
            oos.writeObject(ecossistema.getElementos());
            ecossistema.setSaved(true);
        } catch (Exception e) {
            System.err.println("Error saving the file");
            System.out.println("" + e.getMessage());
        }
    }

    public void load(File file) {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(file)
        )) {
            ecossistema.replaceElementos((Set<IElemento>) ois.readObject());
        } catch (Exception e) {
            System.err.println("Error loading the file");
            System.out.println("" + e.getMessage());
        }
    }

    public void CreateSimulador() {
        gameEngine.unregisterClient(ecossistema);
        ecossistema = new Ecossistema(altura, largura, unidTemporal);
        gameEngine = new GameEngine();
        gameEngine.registerClient(ecossistema);
        gameEngine.start(unidTemporal);
        ecossistema.addPropertyChangeListener("ola", evt -> {
            updateMap();
        });
        this.addPropertyChangeListener(PROP_CREDITS, evt -> {});
    }

    public void export(File file) {
        try (PrintWriter writer = new PrintWriter(file)) {
            for (IElemento elemento : ecossistema.getElementos()) {
                StringBuilder sb = new StringBuilder();
                if (elemento.getType() == Elemento.FAUNA) {
                    sb.append("Fauna").append(",");
                    sb.append("Forca: ");
                    sb.append(((Fauna) elemento).getForca()).append(",");
                } else if (elemento.getType() == Elemento.FLORA) {
                    sb.append("Flora").append(",");
                    sb.append("Forca: ");
                    sb.append(((Flora) elemento).getForca()).append(",");
                } else if (elemento.getType() == Elemento.INANIMADO) {
                    sb.append("Inanimado").append(",");
                    sb.append("-").append(",");
                }
                sb.append("X: ");
                sb.append(elemento.getArea().esquerda()).append(",");
                sb.append("Y: ");
                sb.append(elemento.getArea().cima()).append(",");
                sb.append("X2: ");
                sb.append(elemento.getArea().direita()).append(",");
                sb.append("Y2: ");
                sb.append(elemento.getArea().baixo());
                writer.println(sb);
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error exporting to CSV file: " + e.getMessage());
        }
    }

    public boolean isSaved() {
        return ecossistema.isSaved();
    }

    public void importEcossistema(File hFile) {
        try (BufferedReader reader = new BufferedReader(new FileReader(hFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals("Fauna")) {
                    Fauna fauna = new Fauna(new Area(Double.parseDouble(parts[3].split(" ")[1]), Double.parseDouble(parts[2].split(" ")[1]), Double.parseDouble(parts[5].split(" ")[1]), Double.parseDouble(parts[4].split(" ")[1])), ecossistema.getElementos(), Double.parseDouble(parts[1].split(" ")[1]), perdaForcaFaunaMovimento, ganhoForcaFaunaAlimento);
                    //fauna.setForca(Double.parseDouble(parts[1].split(" ")[1]));
                    ecossistema.getElementos().add(fauna);
                } else if (parts[0].equals("Flora")) {
                    Flora flora = new Flora(new Area(Double.parseDouble(parts[3].split(" ")[1]), Double.parseDouble(parts[2].split(" ")[1]), Double.parseDouble(parts[5].split(" ")[1]), Double.parseDouble(parts[4].split(" ")[1])), Double.parseDouble(parts[1].split(" ")[1]), perdaForcaFloraAlimento);
                    //flora.setForca(Double.parseDouble(parts[1].split(" ")[1]));
                    ecossistema.getElementos().add(flora);
                } else if (parts[0].equals("Inanimado")) {
                    Inanimado inanimado = new Inanimado(new Area(Double.parseDouble(parts[3].split(" ")[1]), Double.parseDouble(parts[2].split(" ")[1]), Double.parseDouble(parts[5].split(" ")[1]), Double.parseDouble(parts[4].split(" ")[1])));

                    ecossistema.getElementos().add(inanimado);
                }
            }
        } catch (IOException e) {
            System.err.println("Error importing from CSV file: " + e.getMessage());
        }
        updateMap();
    }

    public void delete(double x, double y) {
        for (IElemento elemento : ecossistema.getElementos()) {
            Area areaElemento = elemento.getArea();

            if (x >= areaElemento.esquerda() && x <= areaElemento.direita() &&
                    y >= areaElemento.cima() && y <= areaElemento.baixo()) {

                ecossistema.getElementos().remove(elemento);
                break;
            }
        }
    }


    boolean showCredits;
    public boolean showCredits(){

        return showCredits;
    }
    public void setShowCredits(boolean b){
        showCredits = b;
        pcs.firePropertyChange(PROP_CREDITS, null, null);
    }

    public void config(String forcaDefaultFlora, String ganhoForcaFaunaAlimento, String perdaForcaFloraAlimento, String forcaDefaultFauna, String perdaForcaFaunaMovimento) {
        if(forcaDefaultFlora.equals("") || forcaDefaultFlora.equals("0")){
            forcaDefaultFlora = "50";
        }
        if(ganhoForcaFaunaAlimento.equals("") || ganhoForcaFaunaAlimento.equals("0")){
            ganhoForcaFaunaAlimento = "1";
        }
        if(perdaForcaFloraAlimento.equals("") || perdaForcaFloraAlimento.equals("0")){
            perdaForcaFloraAlimento = "1";
        }
        if(forcaDefaultFauna.equals("") || forcaDefaultFauna.equals("0")){
            forcaDefaultFauna = "50";
        }
        if(perdaForcaFaunaMovimento.equals("") || perdaForcaFaunaMovimento.equals("0")){
            perdaForcaFaunaMovimento = "0.5";
        }

        try{
            this.forcaDefaultFlora = Double.parseDouble(forcaDefaultFlora);
            this.ganhoForcaFaunaAlimento = Double.parseDouble(ganhoForcaFaunaAlimento);
            this.perdaForcaFloraAlimento = Double.parseDouble(perdaForcaFloraAlimento);
            this.forcaDefaultFauna = Double.parseDouble(forcaDefaultFauna);
            this.perdaForcaFaunaMovimento = Double.parseDouble(perdaForcaFaunaMovimento);
        } catch (Exception e){

        }

    }

    public void editFlora(double x, double y, String text, String text1) {
        for (IElemento elemento : ecossistema.getElementos()) {
            if (elemento.getType() == Elemento.FLORA) {
                Area areaFlora = ((Flora) elemento).getArea();

                if (x >= areaFlora.esquerda() && x <= areaFlora.direita() &&
                        y >= areaFlora.cima() && y <= areaFlora.baixo()) {

                    ((Flora) elemento).setForca(Double.parseDouble(text));
                    ((Flora) elemento).setPerdaForca(Double.parseDouble(text1));
                    break;
                }
            }
        }
    }
    public void editFauna(double x, double y, String text, String text1, String text2) {
        for (IElemento elemento : ecossistema.getElementos()) {
            if (elemento.getType() == Elemento.FAUNA) {
                Area areaFlora = ((Fauna) elemento).getArea();

                if (x >= areaFlora.esquerda() && x <= areaFlora.direita() &&
                        y >= areaFlora.cima() && y <= areaFlora.baixo()) {

                    ((Fauna) elemento).setGanhoForca(Double.parseDouble(text));
                    ((Fauna) elemento).setForca(Double.parseDouble(text1));
                    ((Fauna) elemento).setPerdaForcaMovimento(Double.parseDouble(text2));

                    break;
                }
            }
        }
    }

    public String getElementoXY(double x, double y){
        for (IElemento elemento : ecossistema.getElementos()) {
            if (elemento.getType()== Elemento.FLORA) {
                Area areaFlora = ((Flora) elemento).getArea();

                if (x >= areaFlora.esquerda() && x <= areaFlora.direita() &&
                        y >= areaFlora.cima() && y <= areaFlora.baixo()) {

                    return "flora";
                }
            }
            else if (elemento.getType()== Elemento.FAUNA) {
                Area areaFlora = ((Fauna) elemento).getArea();

                if (x >= areaFlora.esquerda() && x <= areaFlora.direita() &&
                        y >= areaFlora.cima() && y <= areaFlora.baixo()) {


                    return "fauna";
                }
            }
        }
        return "";
    }

}
