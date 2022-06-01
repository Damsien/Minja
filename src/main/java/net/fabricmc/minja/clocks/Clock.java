package net.fabricmc.minja.clocks;

import net.minecraft.world.World;

public abstract class Clock implements Runnable {

    private long timer;
    private Thread thread;


    public Clock(long timer) {
        this.timer = timer;
    }


    @Override
    final public void run() {

        try {
            Thread.sleep(timer);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        execute();
    }

    final public void start() {
        if(thread == null || thread.isAlive()) {
            this.thread = new Thread(this);
            this.thread.start();
        }
        else this.run();
    }

    public abstract void execute();
}
