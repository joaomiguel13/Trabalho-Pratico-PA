package pt.isec.pa.javalife.model.fsm.states;

import pt.isec.pa.javalife.model.fsm.JavaLifeState;

import java.io.Serializable;

public interface IJavaLifeState extends Serializable {
    JavaLifeState getState();

    void evolve();
}
