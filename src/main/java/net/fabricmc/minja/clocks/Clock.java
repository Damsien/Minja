package net.fabricmc.minja.clocks;

import net.minecraft.world.World;

public abstract class Clock implements Runnable {

    World world;
    long timer;

    public Clock(long timer, World world) {
        this.timer = timer;
        this.world = world;
    }

    @Override
    final public void run() {

        long start = world.getTime();
        long end = start + timer;
        long delta = start;

        System.out.println("Delta : " + delta);
        System.out.println("Ending : " + end);

        while(delta < end) {
            delta = world.getTime();
            System.out.println("Delta : " + delta);
            System.out.println("Ending : " + end);
        }

        System.out.println("Executing : " + end);
        execute();
    }

    final public void start() {
        System.out.println("Starting the clock ! ");
        Thread thread = new Thread(this);
        thread.start();
    }

    public abstract void execute();
}
