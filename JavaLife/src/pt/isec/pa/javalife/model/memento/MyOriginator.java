package pt.isec.pa.javalife.model.memento;

import pt.isec.pa.javalife.model.data.Ecossistema;
import pt.isec.pa.javalife.model.data.Elemento;
import pt.isec.pa.javalife.model.data.IElemento;

import java.io.Serializable;
import java.util.Set;

public class MyOriginator implements IOriginator,Serializable{
    private Set<IElemento> elementos;
    Memento memento;

    @Override
    public IMemento save(Set<IElemento> elementos) {
        this.elementos = elementos;
        memento = new Memento(this);
        System.out.println("Snapshot saved");
        return memento;
    }

    @Override
    public Set<IElemento> restore() {
        Object object = memento.getSnapshot();
        if(object instanceof MyOriginator m){
            return m.elementos;
            //System.out.println("Snapshot restored");
        }
        else{
            System.out.println("Error restoring snapshot");
            return null;
        }
    }


}
