package io.github.amayaframework.core.pipelines;

/**
 * Enum describing the list of default request and response processing stages.
 */
public enum ProcessStage {
    FIND_ROUTE,
    PARSE_REQUEST,
    INVOKE_CONTROLLER,
    CHECK_RESPONSE
}
