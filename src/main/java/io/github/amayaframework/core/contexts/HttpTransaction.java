package io.github.amayaframework.core.contexts;

import io.github.amayaframework.server.utils.HeaderMap;

import java.util.List;
import java.util.Map;

/**
 * <p>An interface describing the general case of a http transaction.</p>
 * <p>It is basic for the {@link HttpRequest} and {@link HttpResponse} classes</p>
 */
public interface HttpTransaction {
    /**
     * Returns the transaction body unchanged
     *
     * @return {@link Object}
     */
    Object getBody();

    /**
     * A body setter that allows you to change the transaction body during its processing.
     * It can also be used by the end user of the transaction.
     *
     * @param body body object
     */
    void setBody(Object body);

    /**
     * Returns {@link HeaderMap} containing all transaction headers.
     *
     * @return {@link HeaderMap}
     */
    HeaderMap getHeaders();

    /**
     * Returns values of specified header from internal header map
     *
     * @param key {@link String} header key
     * @return {@link List} header values
     */
    List<String> getHeader(String key);

    /**
     * Returns all stored attachments that could have been changed during the transaction processing transaction.
     *
     * @return {@link Map}
     */
    Map<String, Object> getAttachments();

    /**
     * Returns a specific attachment
     *
     * @param key which is related to attachment
     * @return {@link Object}
     */
    Object getAttachment(String key);

    /**
     * Puts the attachment in the attachment {@link Map}
     *
     * @param key   which is related to attachment
     * @param value which contains attachment
     */
    void setAttachment(String key, Object value);
}
