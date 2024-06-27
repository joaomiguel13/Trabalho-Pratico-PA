package pt.isec.pa.javalife.model.memento;

public interface IMemento {
    default Object getSnapshot() { return null; }
}
