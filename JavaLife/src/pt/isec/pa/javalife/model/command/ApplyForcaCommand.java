package pt.isec.pa.javalife.model.command;

import pt.isec.pa.javalife.model.data.*;

public class ApplyForcaCommand extends AbstractCommand{
    double x, y;
    public ApplyForcaCommand(Ecossistema receiver, double x, double y) {
        super(receiver);
        this.x = x;
        this.y = y;
    }

    @Override
    public void execute() {
        for (IElemento elemento : receiver.getElementos()) {
            if (elemento.getType()== Elemento.FAUNA) {
                Area areaFauna = elemento.getArea();

                if (x >= areaFauna.esquerda() && x <= areaFauna.direita() &&
                        y >= areaFauna.cima() && y <= areaFauna.baixo()) {
                    ((Fauna) elemento).setForca(((Fauna) elemento).getForca() + 50);
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
