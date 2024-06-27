package pt.isec.pa.javalife.model.command;

import pt.isec.pa.javalife.model.data.Ecossistema;

abstract class AbstractCommand implements ICommand {
    protected Ecossistema receiver;
    protected AbstractCommand(Ecossistema receiver){
        this.receiver = receiver;
    }
}
