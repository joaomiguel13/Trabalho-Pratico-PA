package pt.isec.pa.javalife.model.command;

public interface ICommand {
    void execute();

    boolean undo();

    boolean redo();
}
