package io.github.amayaframework.core;

import io.github.amayaframework.core.configurators.BaseServletConfigurator;

/**
 * A builder that helps to instantiate a properly configured collection of Servlets
 */
public class AmayaServletBuilder extends AbstractBuilder {

    public AmayaServletBuilder() {
        super(new BaseServletConfigurator());
        resetValues();
    }
}
