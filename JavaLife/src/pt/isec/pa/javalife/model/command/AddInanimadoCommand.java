package pt.isec.pa.javalife.model.command;

import pt.isec.pa.javalife.model.data.*;

import java.util.ArrayList;
import java.util.Random;

public class AddInanimadoCommand extends AbstractCommand{
    private int lastID;
    private ArrayList<Inanimado> lastUndo;
    public AddInanimadoCommand(Ecossistema receiver) {
        super(receiver);
        lastUndo = new ArrayList<>();

    }

    @Override
    public void execute() {
        boolean done = false;
        while(!done){
            Random random = new Random();
            int x = random.nextInt(receiver.getLargura() - 25 - 25) + 25;
            int y = random.nextInt(receiver.getAltura() - 25 - 25) + 25;
            int x1 = random.nextInt(45 -10) + 10;
            if(y > 25 && y + 25 < 775 && x > 26 && x + 25 < 550) {
                for(IElemento e : receiver.getElementos()){
                    if(e.getArea().intersects(new Area(x, y, x + x1+5, y + x1))){
                        return;
                    }
                }
                Inanimado inanimado = new Inanimado(new Area(x, y, x + x1+5, y + x1));
                receiver.getElementos().add(inanimado);
                done = true;
            }
        }
    }

    @Override
    public boolean undo() {
        IElemento temp = null;
        for (IElemento e : receiver.getElementos()) {
            if (e.getType()== Elemento.INANIMADO) {
                lastID = e.getId();
                temp = e;
            }
        }
        System.out.println("lastID: " + lastID);
        if(temp != null){
            lastUndo.add((Inanimado) temp);
            receiver.removeInanimadoCommand(lastID);
        }
        return true;
    }

    @Override
    public boolean redo() {
        IElemento temp=null;
        for(Inanimado e : lastUndo){
            temp = e;
        }
        if(temp != null){
            receiver.addInanimadoRedo(temp);
        }
        return true;
    }
}
