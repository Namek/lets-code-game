package net.engio.mbassy.bus;

import net.engio.mbassy.IPublicationErrorHandler;

import java.util.Collection;


public interface ErrorHandlingSupport {

    /**
     * Publication errors may occur at various points of time during message delivery. A handler may throw an exception,
     * may not be accessible due to security constraints or is not annotated properly.
     * In any of all possible cases a publication error is created and passed to each of the registered error handlers.
     * A call to this method will add the given error handler to the chain
     *
     * @param errorHandler
     */
    void addErrorHandler(IPublicationErrorHandler errorHandler);

    /**
     * Returns an immutable collection containing all the registered error handlers
     *
     * @return
     */
    Collection<IPublicationErrorHandler> getRegisteredErrorHandlers();

}
