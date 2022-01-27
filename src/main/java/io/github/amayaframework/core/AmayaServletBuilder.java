package io.github.amayaframework.core;

import io.github.amayaframework.core.configurators.BaseServletConfigurator;
import io.github.amayaframework.core.configurators.Configurator;
import io.github.amayaframework.core.controllers.Controller;
import io.github.amayaframework.core.handlers.ServletHandler;

import javax.servlet.Servlet;
import java.lang.annotation.Annotation;
import java.util.*;
import java.util.function.Consumer;

/**
 * A builder that helps to instantiate a properly configured collection of Servlets
 */
public class AmayaServletBuilder extends AbstractBuilder {
    private String urlPattern;
    private boolean useUrlPattern;

    public AmayaServletBuilder() {
        super(new BaseServletConfigurator());
        resetValues();
    }

    /**
     * Sets a list of configurators that will be used when adding each controller.
     *
     * @param configurators {@link List} configurators to be set. Must be not null.
     * @return {@link AmayaServletBuilder} instance
     */
    @Override
    public AmayaServletBuilder pipelineConfigurators(Collection<Configurator> configurators) {
        return (AmayaServletBuilder) super.pipelineConfigurators(configurators);
    }

    /**
     * Adds the configurator to the end of the current list of configurators.
     *
     * @param configurator {@link Consumer} configurator to be added. Must be not null.
     * @return {@link AmayaServletBuilder} instance
     */
    @Override
    public AmayaServletBuilder addConfigurator(Configurator configurator) {
        return (AmayaServletBuilder) super.addConfigurator(configurator);
    }

    /**
     * Adds the controller to the list of processed
     *
     * @param controller {@link Controller} controller to be added. Must be not null.
     * @return {@link AmayaServletBuilder} builder instance
     */
    @Override
    public AmayaServletBuilder addController(Controller controller) {
        return (AmayaServletBuilder) super.addController(controller);
    }

    /**
     * Removes the controller from the list of processed
     *
     * @param path controller path
     * @return {@link AmayaServletBuilder} instance
     */
    @Override
    public AmayaServletBuilder removeController(String path) {
        return (AmayaServletBuilder) super.removeController(path);
    }

    /**
     * Sets the annotation by which the controllers will be scanned.
     * If value will be null, the scan will not be performed.
     *
     * @param annotation {@link Class} of annotation
     * @return {@link AmayaServletBuilder} instance
     */
    @Override
    public AmayaServletBuilder controllerAnnotation(Class<? extends Annotation> annotation) {
        return (AmayaServletBuilder) super.controllerAnnotation(annotation);
    }

    /**
     * Sets a flag that will activate the automatic addition of the required pattern to the routes.
     *
     * @param urlPattern pattern value
     * @return {@link AmayaServletBuilder} instance
     */
    public AmayaServletBuilder useUrlPattern(String urlPattern) {
        this.useUrlPattern = true;
        this.urlPattern = Objects.requireNonNull(urlPattern);
        return this;
    }

    /**
     * Sets a flag that will activate the automatic addition of the required pattern to the routes.
     *
     * @param useUrlPattern flag value
     * @return {@link AmayaServletBuilder} instance
     */
    public AmayaServletBuilder useUrlPattern(boolean useUrlPattern) {
        this.useUrlPattern = useUrlPattern;
        return this;
    }

    /**
     * Creates list of Servlets corresponding to the specified parameters
     * and resets the builder to the initial parameters.
     *
     * @return {@link AmayaServer} instance
     */
    public Map<String, Servlet> build() {
        Map<String, Servlet> ret = new HashMap<>();
        findControllers();
        controllers.forEach((path, controller) -> {
            ServletHandler handler = new ServletHandler(controller);
            handler.getHandler().configure(configurators);
            String pathToAdd = useUrlPattern ? path + urlPattern : path;
            ret.put(pathToAdd, handler);
        });
        resetValues();
        return ret;
    }
}
