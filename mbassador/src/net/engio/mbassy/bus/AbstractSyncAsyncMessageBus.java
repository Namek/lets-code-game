package net.engio.mbassy.bus;

import net.engio.mbassy.PublicationError;
import net.engio.mbassy.bus.config.IBusConfiguration;
import net.engio.mbassy.bus.publication.ISyncAsyncPublicationCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * The base class for all async message bus implementations.
 *
 * @param <T>
 * @param <P>
 */
public abstract class AbstractSyncAsyncMessageBus<T, P extends ISyncAsyncPublicationCommand> extends AbstractSyncMessageBus<T, P> implements IMessageBus<T, P> {

    // executor for asynchronous message handlers
    private final ExecutorService executor;

    // all threads that are available for asynchronous message dispatching
    private final List<Thread> dispatchers;

    // all pending messages scheduled for asynchronous dispatch are queued here
    private final BlockingQueue<MessagePublication> pendingMessages;

    protected AbstractSyncAsyncMessageBus(IBusConfiguration configuration) {
        super(configuration);
        this.executor = configuration.getExecutorForAsynchronousHandlers();
        getRuntime().add("handler.async-service", executor);
        pendingMessages = configuration.getPendingMessagesQueue();
        dispatchers = new ArrayList<Thread>(configuration.getNumberOfMessageDispatchers());
        initDispatcherThreads(configuration);
    }


    // initialize the dispatch workers
    private void initDispatcherThreads(IBusConfiguration configuration) {
        for (int i = 0; i < configuration.getNumberOfMessageDispatchers(); i++) {
            // each thread will run forever and process incoming
            // message publication requests

            Thread dispatcher = configuration.getThreadFactoryForAsynchronousMessageDispatch().newThread(new Runnable() {
                public void run() {
                    while (true) {
                        try {
                            pendingMessages.take().execute();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return;
                        } catch(Throwable t){
                            handlePublicationError(new PublicationError(t, "Error in asynchronous dispatch", null, null, null));
                        }
                    }
                }
            });
            dispatchers.add(dispatcher);
            dispatcher.start();
        }
    }


    // this method enqueues a message delivery request
    protected MessagePublication addAsynchronousDeliveryRequest(MessagePublication request) {
        try {
            pendingMessages.put(request);
            return request.markScheduled();
        } catch (InterruptedException e) {
            // TODO: publication error
            return request;
        }
    }

    // this method queues a message delivery request
    protected MessagePublication addAsynchronousDeliveryRequest(MessagePublication request, long timeout, TimeUnit unit) {
        try {
            return pendingMessages.offer(request, timeout, unit)
                    ? request.markScheduled()
                    : request;
        } catch (InterruptedException e) {
            // TODO: publication error
            return request;
        }
    }

    @Override
    protected void finalize() throws Throwable {
        shutdown();
        super.finalize();
    }

    @Override
    public void shutdown() {
        for (Thread dispatcher : dispatchers) {
            dispatcher.interrupt();
        }
        if(executor != null) executor.shutdown();
    }

    @Override
    public boolean hasPendingMessages() {
        return pendingMessages.size() > 0;
    }

    @Override
    public Executor getExecutor() {
        return executor;
    }

}
