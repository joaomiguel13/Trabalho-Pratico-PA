package pt.isec.pa.javalife.model.command;

import pt.isec.pa.javalife.model.data.*;

import java.util.ArrayList;
import java.util.Random;

public class AddFloraCommand extends AbstractCommand{
    private int lastID;
    private ArrayList<Flora> lastUndo;
    double forcaDefault;
    double perdaForca;
    public AddFloraCommand(Ecossistema receiver, double forcaDefault, double perdaForca) {
        super(receiver);
        lastUndo = new ArrayList<>();
        this.forcaDefault = forcaDefault;
        this.perdaForca = perdaForca;
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
                receiver.addFlora_command(forcaDefault, perdaForca,x,y,x1);
                //Flora flora = new Flora(new Area(x, y, x + x1+5, y + x1), forcaDefault, perdaForca);
                //flora.setImagem("rectangle.png");
                //receiver.getElementos().add(flora);
                done = true;
            }
        }
        for(IElemento e : receiver.getElementos()){
            if(e.getType()== Elemento.FLORA){
                lastID = e.getId();
            }
        }
    }

    @Override
    public boolean undo() {
        IElemento temp = null;
        for (IElemento e : receiver.getElementos()) {
            if (e.getType()== Elemento.FLORA) {
                lastID = e.getId();
                temp = e;
            }
        }
        if(temp != null){
            lastUndo.add((Flora) temp);
            receiver.removeFloraCommand(lastID);
        }
        return true;
    }

    @Override
    public boolean redo() {
        IElemento temp=null;
        for(Flora e : lastUndo){
            temp = e;
        }
        if(temp != null){
            receiver.addFloraRedo(temp);
        }
        return true;
    }
}
