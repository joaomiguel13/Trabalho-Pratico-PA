package pt.isec.pa.javalife.model.command;

import pt.isec.pa.javalife.model.data.Ecossistema;
import pt.isec.pa.javalife.model.data.Elemento;
import pt.isec.pa.javalife.model.data.Fauna;
import pt.isec.pa.javalife.model.data.IElemento;

public class ApplySolCommand extends AbstractCommand{

    public ApplySolCommand(Ecossistema receiver) {
        super(receiver);
    }


    @Override
    public void execute() {
        receiver.applySolCommand();
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
