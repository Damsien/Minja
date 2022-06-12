package net.fabricmc.minja.mouse.callflow;

import net.fabricmc.minja.mouse.context.Context;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 *  Simulates the generic callflow of a mouse click. <br>
 *  During a click, the mouse goes through 3 distinct states: <br>
 *
 *  <ul>
 *      <li> Pressing the button </li>
 *      <li> Holding the button </li>
 *      <li> The release of the button </li>
 *  </ul>
 *
 *  <br>
 *  For all buttons, the behavior is identical: only the check and the callback associated to each action change. <br><br>
 *
 * For these reasons, this class is abstract and must be implemented concretely.
 *
 *
 * @param <EventCallback> Type of return of callback functions
 *
 */
public abstract class GenericCallFlow<EventCallback> {

    private Context context;

    public GenericCallFlow() {
        this.context = new Context(null,null,null,null);
    }

    protected boolean isFirstTimeClicked = true;

    /**
     * Identify the current action of the mouse and trigger the associated callback.
     *
     * @return the success of the event
     */
    public EventCallback triggerCurrentAction() {

        head();

        if (isFirstTimeClicked) {
            afterPressed();
            isFirstTimeClicked = false;
            return triggerPressed();
        }

        else {
            afterMaintained();
            return triggerMaintained();
        }

    }


    /**
     *  Periodically check if the click has been released. <br><br>
     *
     * The release of the click is a <i>non-event</i>: <br>
     * Unlike {@link net.fabricmc.minja.objects.MinjaItem#use(World, PlayerEntity, Hand)} pressing} and {@link net.fabricmc.minja.mixin.LivingEntityMixin#swingHand(Hand, boolean, CallbackInfo) maintaining},
     * which are simulated via method calls on the object or player, there is no event to identify its release. <br><br>
     *
     * To detect it, this function is called every tick when the calling object is in the player's hands.
     */
    public void tick() {

        if (!isFirstTimeClicked && isReleased())  {
            triggerReleased();
            afterReleased();
            isFirstTimeClicked = true;
        }
    }

    /**
     * Inject code at the head of the method
     */
    public void head() {}

    /**
     * Inject additional treatments after the release event has been triggered
     */
    public void afterReleased() {}

    /**
     * Inject additional treatments after the press event has been triggered
     */
    public void afterPressed() {}

    /**
     * Inject additional treatments after the maintain event has been triggered
     */
    public void afterMaintained() {}

    /**
     *  Execute the code inside this method once the <strong>released</strong> event has been triggered.
     *
     * @return the success of the event
     */
    public abstract EventCallback triggerReleased();

    /**
     *  Execute the code inside this method once the <strong>pressed</strong> event has been triggered.
     *
     * @return the success of the event
     */
    public abstract EventCallback triggerPressed();

    /**
     *  Execute the code inside this method once the <strong>maintained</strong> event has been triggered.
     *
     * @return the success of the event
     */
    public abstract EventCallback triggerMaintained();


    /**
     *  Condition to verify to check the click has been released
     *
     * @return the success of the event
     */
    public abstract boolean isReleased();

    /**
     *  Change associated context to maintain variable up to date
     */
    public void changeContext(Context context) {
        this.context = context;
    }

    /**
     *  Allow child class to get their current context
     */
    protected Context getContext() {
        return this.context;
    }


}
