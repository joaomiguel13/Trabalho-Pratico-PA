package pt.isec.pa.javalife.model.data;

import java.io.Serializable;

public sealed interface IElemento extends Serializable permits ElementoBase {
    int getId(); // retorna o identificador
    Elemento getType(); // retorna o tipo
    Area getArea(); // retorna a área ocupada

    void evolve();
}

