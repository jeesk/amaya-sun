package io.github.amayaframework.core.sun;

import io.github.amayaframework.core.Amaya;
import io.github.amayaframework.core.util.IOUtil;
import io.github.amayaframework.server.interfaces.HttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

public class SunAmaya implements Amaya<HttpServer> {
    private static final Logger logger = LoggerFactory.getLogger(SunAmaya.class);
    private final HttpServer server;

    protected SunAmaya(HttpServer server) {
        this.server = server;
    }

    private static void printHelloMessage() throws IOException {
        logger.info("Amaya started successfully");
        logger.info("\n" + IOUtil.readLogo());
        logger.info("We are glad to welcome you, senpai!");
        logger.info("\n" + IOUtil.readArt());
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
    }

    @Override
    public void close() {
        server.stop(0);
        Executor executor = server.getExecutor();
        if (executor instanceof ExecutorService) {
            ((ExecutorService) executor).shutdown();
        }
        logger.info("Amaya server stopped");
    }
}
