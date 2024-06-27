package pt.isec.pa.javalife.model.data;

import java.io.Serializable;

public final class Inanimado extends ElementoBase implements Serializable {
    private static int idInanimado = 0;
    private final int id;
    private boolean cerca;
    Area area;

    public Inanimado(Area area) {
        this.area = area;
        this.id = idInanimado++;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public Elemento getType() {
        return Elemento.INANIMADO;
    }

    @Override
    public Area getArea() {
        return area;
    }


    public boolean getCerca(){return cerca;}

    @Override
    public void evolve() {
    }
}
