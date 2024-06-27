package pt.isec.pa.javalife.model.gameengine;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class GameEngine implements IGameEngine {
    private GameEngineState state;
    private GameEngineThread controlThread;
    private final Set<IGameEngineEvolve> clients;
    System.Logger logger;
    private void setState(GameEngineState state) {
        this.state = state;
        logger.log(System.Logger.Level.INFO,state.toString());
    }
    public GameEngine() {
        logger = System.getLogger("GameEngine");
        clients = new HashSet<>();
        setState(GameEngineState.READY);

    }
    @Override
    public void registerClient(IGameEngineEvolve listener) {
        clients.add(listener);
    }

    @Override
    public void unregisterClient(IGameEngineEvolve listener) {
        clients.remove(listener);
    }
    @Override
    public boolean start(long interval) {
        if (state != GameEngineState.READY)
            return false;
        controlThread = new GameEngineThread(interval);
        setState(GameEngineState.RUNNING);
        controlThread.start();
        return true;
    }
    @Override
    public boolean stop() {
        if (state == GameEngineState.READY)
            return false;
        setState(GameEngineState.READY);
        return true;
    }
    @Override
    public boolean pause() {
        if (state != GameEngineState.RUNNING)
            return false;
        setState(GameEngineState.PAUSED);
        return true;
    }
    @Override
    public boolean resume() {
        if (state != GameEngineState.PAUSED)
            return false;
        setState(GameEngineState.RUNNING);
        return true;
    }
    @Override
    public GameEngineState getCurrentState() {
        return state;
    }
    @Override
    public long getInterval() {
        return controlThread.interval;
    }
    @Override
    public void setInterval(long newInterval) {
        if (controlThread != null)
            controlThread.interval = newInterval;
    }
    @Override
    public void waitForTheEnd() {
        try {
            controlThread.join();
        } catch (InterruptedException ignored) {}
    }

    private class GameEngineThread extends Thread {
        long interval;
        GameEngineThread(long interval) {
            this.interval = interval;
            this.setDaemon(true);
        }
        @Override
        public void run() {
            int errCounter = 0;
            while (true) {
                if (state == GameEngineState.READY) break;
                if (state == GameEngineState.RUNNING) {
                    new Thread(() -> {
                        long time = System.nanoTime();
                        List.copyOf(clients).forEach(
                                client -> client.evolve(GameEngine.this, time)
                        );
                    }).start();
                }
                try {
                    //noinspection BusyWait
                    sleep(interval);
                    errCounter = 0;
                } catch (InterruptedException e) {
                    if (state == GameEngineState.READY || errCounter++ > 10)
                        break;
                }
            }
        }
    }
}
