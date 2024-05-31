package com.bookkeepersmc.entrypoint.api;

public interface EventExceptionHandler {

    void handleException(IEventBus eventBus, Event event, EventListener[] listeners, int index, Throwable throwable);
}
