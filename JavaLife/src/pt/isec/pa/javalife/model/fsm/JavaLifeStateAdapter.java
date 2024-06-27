package pt.isec.pa.javalife.model.fsm;

import pt.isec.pa.javalife.model.data.Fauna;
import pt.isec.pa.javalife.model.fsm.states.IJavaLifeState;

public abstract class JavaLifeStateAdapter implements IJavaLifeState {
    protected Fauna data;
    protected JavaLifeStateAdapter(Fauna data) {
        this.data = data;
    }

    protected void changeState(JavaLifeState newState) {
        data.changeState(newState.getInstance(data));
    }

    @Override
    public void evolve(){}

}
