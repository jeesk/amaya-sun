package io.github.amayaframework.core.controllers;

import io.github.amayaframework.core.routers.Router;

public interface Controller {
    Router router();

    String getPath();

    void setPath(String route);
}
