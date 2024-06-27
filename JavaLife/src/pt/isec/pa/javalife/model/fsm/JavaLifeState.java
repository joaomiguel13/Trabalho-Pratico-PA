package pt.isec.pa.javalife.model.fsm;

import pt.isec.pa.javalife.model.data.Fauna;
import pt.isec.pa.javalife.model.fsm.states.*;

import java.io.Serializable;

public enum JavaLifeState implements Serializable {
    MOVING, SEARCHING_FOOD, REPRODUCING,DEAD;

    public IJavaLifeState getInstance(Fauna data){
        return switch(this){
            case MOVING -> new MovingState(data);
            case SEARCHING_FOOD -> new SearchingFoodState(data);
            case REPRODUCING -> new ReproducingState(data);
            case DEAD -> new DeadState(data);
        };
    }
}
