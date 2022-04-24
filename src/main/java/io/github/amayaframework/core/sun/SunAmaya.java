package io.github.amayaframework.core.sun;

import io.github.amayaframework.core.AbstractAmaya;
import io.github.amayaframework.core.handlers.EventManager;
import io.github.amayaframework.server.interfaces.HttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class SunAmaya extends AbstractAmaya<HttpServer> {
    private static final Logger LOGGER = LoggerFactory.getLogger(SunAmaya.class);
    private final HttpServer server;

    protected SunAmaya(EventManager manager, HttpServer server) {
        super(manager);
        this.server = server;
    }

    private static void printHelloMessage() throws IOException {
        LOGGER.info("Amaya started successfully");
        LOGGER.info("\n" + IOUtil.readLogo());
        LOGGER.info("\n" + IOUtil.readArt());
    }

    @Override
    public String getAddress() {
        return server.getAddress().getHostName();
    }

    @Override
    public int getPort() {
        return server.getAddress().getPort();
    }

    @Override
    public HttpServer getServer() {
        return server;
    }

    @Override
    public void start() throws Throwable {
        server.start();
        printHelloMessage();
        super.start();
    }

    @Override
    public void close() throws Exception {
        server.stop(0);
        LOGGER.info("Amaya server stopped");
        super.close();
    }
}
