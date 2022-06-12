package net.fabricmc.minja.clocks;

/**
 * A clock executing a callback after a certain delay. (similar to the setTimeout method of javascript) <br><br>
 *
 * <strong>WARNING</strong>: As this clock pauses the current thread, it creates its own context in order not to prevent the game from running properly. <br>
 * <strong>As this clock breaks the concurrent thread rule, use it only as a last resort!</strong> <br>
 *
 * <pre>
 *
 * <div id="clock-example"></div>
 * {@code
 * public void howToUseAlarm() {
 *
 * 	Clock clock = new Clock(2000) {
 *
 *            @Override public void execute() {
 *
 * 				 // Do your stuff here
 * 				 ...
 *
 * 				 // Clock can be re-alarm at the end in case you need
 * 				 this.start();
 *            }
 *      };
 *
 *      // Start it for the first time
 *      clock.start();
 * 	}
 *}
 * </pre>
 *
 *
 */
public abstract class Clock implements Runnable {

    /** **/
    final private long timer;
    private Thread thread;

    private boolean isStopped = false;


    /**
     * Initialize a clock.
     *
     * @param timer delay before execution of the callback (in milliseconds)
     */
    public Clock(long timer) {
        this.timer = timer;
    }

    /**
     * Start the timeout. <br>
     * <strong>This method should not be called since it would interrupt the caller thread.</strong> <br>
     * Use start() if you need to run the clock.
     *
     * @see #start()
     */
    @Override
    final public void run() {

        try {
            Thread.sleep(timer);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        if(!isStopped)
            execute();
    }

    /**
     * Stop the clock.
     * Doesn't stop the callback function if callback has already been reached.
     * Useful to break an alarm.
     */
    public void stop() {
        isStopped = true;
    }

    /**
     * Start the clock.
     * If a thread hasn't been initialized, the context will be created too.
     *
     */
    final public void start() {
        isStopped = false;
        if(thread == null || thread.isAlive()) {
            this.thread = new Thread(this);
            this.thread.start();
        }
        else this.run();
    }

    /**
     *  The code that will be executed after the timeout.
     *  Because the content of the code depends on the user's problem,
     *  this method must be redefined in a concrete clock or an anonymous class.
     *
     *  @see <a href="#clock-example"> Example of usage </a>
     */
    public abstract void execute();
}
