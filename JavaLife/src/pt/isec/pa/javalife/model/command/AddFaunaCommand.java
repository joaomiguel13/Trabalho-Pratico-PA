package pt.isec.pa.javalife.model.command;

import pt.isec.pa.javalife.model.data.*;

import java.util.ArrayList;
import java.util.Random;

public class AddFaunaCommand extends AbstractCommand{
    private int lastID;
    private ArrayList<Fauna> lastUndo;
    private double defaultForca;
    private double perdaForcaMovimento;
    private double ganhaForcaAlimento;
    public AddFaunaCommand(Ecossistema receiver, double defaultForca, double perdaForcaMovimento, double ganhaForcaAlimento) {
        super(receiver);
        lastUndo = new ArrayList<>();
        this.defaultForca = defaultForca;
        this.perdaForcaMovimento = perdaForcaMovimento;
        this.ganhaForcaAlimento = ganhaForcaAlimento;
    }
    @Override
    public void execute() {
        boolean done = false;
        while(!done){
            Random random = new Random();
            int x = random.nextInt(receiver.getLargura() - 25 - 25) + 25;
            int y = random.nextInt(receiver.getAltura()  - 25 - 25) + 25;
            if(y > 25 && y + 25 < 775 && x > 26 && x + 25 < 550) {
                for(IElemento e : receiver.getElementos()){
                    if(e.getArea().intersects(new Area(x, y, x+25, y+25))){
                        return;
                    }
                }
                receiver.addFauna_command(defaultForca, perdaForcaMovimento, ganhaForcaAlimento,x,y);
                //receiver.getElementos().add(new Fauna(new Area(x, y, x+25, y+25), receiver.getElementos(), defaultForca, perdaForcaMovimento, ganhaForcaAlimento));
                done = true;
            }
        }
        for(IElemento e : receiver.getElementos()){
            if(e.getType()== Elemento.FAUNA){
                lastID = e.getId();
            }
        }
    }

    @Override
    public boolean undo() {
        IElemento temp = null;
        for (IElemento e : receiver.getElementos()) {
            if (e.getType()== Elemento.FAUNA) {
                lastID = e.getId();
                temp = e;
            }
        }
        if(temp != null){
            lastUndo.add((Fauna) temp);
            receiver.removeFauna(lastID);
        }
        return true;
    }

    @Override
    public boolean redo() {
        IElemento temp=null;
        for(Fauna e : lastUndo){
                temp = e;
        }
        if(temp != null){
            receiver.addFaunaRedo(temp);
        }
        return true;
    }
}
