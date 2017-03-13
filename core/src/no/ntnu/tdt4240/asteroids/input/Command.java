package no.ntnu.tdt4240.asteroids.input;

import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;

public class Command implements Pool.Poolable {

    public static final int MOVE = 0;
    public static final int FIRE = 1;
    private static Pool<Command> pool = Pools.get(Command.class);
    private int command;

    public Command() {
    }

    public static Command obtain(int command) {
        Command instance = pool.obtain();
        instance.command = command;
        return instance;
    }

    @Override
    public void reset() {

    }

    public void release() {
        pool.free(this);
    }

    public int getCommand() {
        return command;
    }
}
