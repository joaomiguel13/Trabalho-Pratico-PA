package pt.isec.pa.javalife.model.command;

import pt.isec.pa.javalife.model.data.*;

import java.util.Set;

public class ApplyHerbicidaCommand extends AbstractCommand{
    double x, y;

    public ApplyHerbicidaCommand(Ecossistema receiver,double x, double y) {
        super(receiver);
        this.x = x;
        this.y = y;
    }

    @Override
    public void execute() {
        for (IElemento elemento : receiver.getElementos()) {
            if (elemento.getType()== Elemento.FLORA) {
                Area areaFlora = ((Flora) elemento).getArea();

                if (x >= areaFlora.esquerda() && x <= areaFlora.direita() &&
                        y >= areaFlora.cima() && y <= areaFlora.baixo()) {

                    receiver.getElementos().remove(elemento);
                    break;
                }
            }
        }
    }

    @Override
    public boolean undo() {
        return false;
    }

    @Override
    public boolean redo() {
        return false;
    }
}
