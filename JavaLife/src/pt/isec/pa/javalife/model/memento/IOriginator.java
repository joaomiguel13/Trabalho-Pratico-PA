package pt.isec.pa.javalife.model.memento;

import pt.isec.pa.javalife.model.data.Ecossistema;
import pt.isec.pa.javalife.model.data.IElemento;

import java.util.Set;

public interface IOriginator {
    IMemento save(Set<IElemento> elementos);
    Set<IElemento> restore();
}
