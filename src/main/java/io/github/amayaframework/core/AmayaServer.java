package io.github.amayaframework.core;

import java.net.InetSocketAddress;
import java.util.concurrent.Executor;

/**
 * The interface representing the main entity of the framework - server.
 */
public interface AmayaServer {
    /**
     * Starts server.
     */
    void start();

    /**
     * Stops the server with a delay.
     *
     * @param delay required delay
     */
    void stop(int delay);

    /**
     * Stops the server immediately.
     */
    void stop();

    /**
     * Returns the {@link Executor} used by the server
     *
     * @return {@link Executor}
     */
    Executor getExecutor();

    /**
     * Returns the address to which the server is bound
     *
     * @return {@link InetSocketAddress}
     */
    InetSocketAddress getAddress();
}
