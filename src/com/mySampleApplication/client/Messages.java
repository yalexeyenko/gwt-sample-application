package com.mySampleApplication.client;

public interface Messages {

    String getMessage(String messageKey);

    String getParametrizedMessage(String messageName, Object... params);

    String format(final String format, final Object... args);

}
