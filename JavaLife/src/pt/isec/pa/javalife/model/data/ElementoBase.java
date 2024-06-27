package pt.isec.pa.javalife.model.data;

import java.io.Serializable;

public abstract sealed class ElementoBase implements Serializable, IElemento permits Inanimado, Flora, Fauna {
    //TODO
}

