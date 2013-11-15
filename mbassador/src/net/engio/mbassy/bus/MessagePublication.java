package net.engio.mbassy.bus;

import net.engio.mbassy.common.DeadMessage;
import net.engio.mbassy.common.FilteredMessage;
import net.engio.mbassy.subscription.Subscription;

import java.util.Collection;

/**
 * A message publication is created for each asynchronous message dispatch. It reflects the state
 * of the corresponding message publication process, i.e. provides information whether the
 * publication was successfully scheduled, is currently running etc.
 * <p/>
 * A message publication lives within a single thread. It is not designed in a thread-safe manner -> not eligible to
 * be used in multiple threads simultaneously .
 *
 * @author bennidi
 *         Date: 11/16/12
 */
public class MessagePublication {

    private final Collection<Subscription> subscriptions;
    private final Object message;
    // message publications can be referenced by multiple threads to query publication progress
    private volatile State state = State.Initial;
    private volatile boolean delivered = false;
    private final BusRuntime runtime;

    protected MessagePublication(BusRuntime runtime, Collection<Subscription> subscriptions, Object message, State initialState) {
        this.runtime = runtime;
        this.subscriptions = subscriptions;
        this.message = message;
        this.state = initialState;
    }

    public boolean add(Subscription subscription) {
        return subscriptions.add(subscription);
    }

    protected void execute() {
        state = State.Running;
        for (Subscription sub : subscriptions) {
           sub.publish(this, message);
        }
        state = State.Finished;
        // if the message has not been marked delivered by the dispatcher
        if (!delivered) {
            if (!isFilteredEvent() && !isDeadEvent()) {
                runtime.getProvider().publish(new FilteredMessage(message));
            } else if (!isDeadEvent()) {
                runtime.getProvider().publish(new DeadMessage(message));
            }

        }
    }

    public boolean isFinished() {
        return state.equals(State.Finished);
    }

    public boolean isRunning() {
        return state.equals(State.Running);
    }

    public boolean isScheduled() {
        return state.equals(State.Scheduled);
    }

    public void markDelivered() {
        delivered = true;
    }

    public MessagePublication markScheduled() {
        if (state.equals(State.Initial)) {
            state = State.Scheduled;
        }
        return this;
    }


    public boolean isDeadEvent() {
        return DeadMessage.class.isAssignableFrom(message.getClass());
    }

    public boolean isFilteredEvent() {
        return FilteredMessage.class.isAssignableFrom(message.getClass());
    }

    private enum State {
        Initial, Scheduled, Running, Finished, Error
    }

    public static class Factory {

        public MessagePublication createPublication(BusRuntime runtime, Collection<Subscription> subscriptions, Object message) {
            return new MessagePublication(runtime, subscriptions, message, State.Initial);
        }

    }

}
