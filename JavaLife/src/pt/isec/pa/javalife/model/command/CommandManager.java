package pt.isec.pa.javalife.model.command;

import java.util.ArrayDeque;
import java.util.Deque;

public class CommandManager {

    private Deque<ICommand> history;
    private Deque<ICommand> redoCmds;

    public CommandManager(){
        history = new ArrayDeque<>();
        redoCmds = new ArrayDeque<>();
    }

    public void invokeCommand(ICommand cmd){
        cmd.execute();
        history.push(cmd);
    }

    public void undo() {
        if (history.isEmpty())
            return;
        ICommand cmd = history.pop();
        cmd.undo();
        redoCmds.push(cmd);
    }


    public void redo() {
        if (redoCmds.isEmpty())
            return;
        ICommand cmd = redoCmds.pop();
        cmd.redo();
        history.push(cmd);
    }
    public boolean hasUndo() {
        return history.size() > 0;
    }

    public boolean hasRedo() {
        return redoCmds.size() > 0;
    }
}
