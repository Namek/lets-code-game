package net.engio.mbassy.bus;

import net.engio.mbassy.bus.publication.IPublicationCommand;

/**
 * This interface is meant to be implemented by different bus implementations to offer a consistent way
 * to plugin different flavors of message publication.
 *
 * The parametrization of the IPostCommand influences which publication flavours are available.
 *
 */
public interface GenericMessagePublicationSupport<T, P extends IPublicationCommand> {

    /**
     * Publish a message to the bus using on of its supported message publication mechanisms. The supported
     * mechanisms depend on the available implementation and are exposed as subclasses of IPublicationCommand.
     * The standard mechanism is the synchronous dispatch which will publish the message in the current thread
     * and returns after every matching handler has been invoked. @See IPublicationCommand.
     *
     * @param message
     * @return
     */
    P post(T message);

}
