package pt.isec.pa.javalife.model.gameengine;

public interface IGameEngine {
    void registerClient(IGameEngineEvolve listener);
    void unregisterClient(IGameEngineEvolve listener);
    boolean start(long interval); //ms; only works in the READY state
    boolean stop(); // works in the RUNNING or PAUSED states
    boolean pause(); // only works in the RUNNING state
    boolean resume(); // only works in the PAUSED state
    GameEngineState getCurrentState(); // returns the current state
    long getInterval();
    void setInterval(long newInterval); // change the interval
    void waitForTheEnd(); // wait until the engine stops
}

