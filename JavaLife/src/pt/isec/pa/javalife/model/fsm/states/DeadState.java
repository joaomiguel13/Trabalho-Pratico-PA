package pt.isec.pa.javalife.model.fsm.states;

import pt.isec.pa.javalife.model.data.Fauna;
import pt.isec.pa.javalife.model.fsm.JavaLifeState;
import pt.isec.pa.javalife.model.fsm.JavaLifeStateAdapter;

public class DeadState extends JavaLifeStateAdapter {

    public DeadState(Fauna data) {
        super(data);
    }

    @Override
    public void evolve() {
        data.suiced();
    }

    @Override
    public JavaLifeState getState() {
        return JavaLifeState.DEAD;
    }
}
