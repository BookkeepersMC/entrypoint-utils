package com.bookkeepersmc.entrypoint.api;

public abstract sealed class EventListener permits EventLoaderListener, ConsumerEventHandler, GeneratedEventListener {
    public abstract void invoke(Event event);
}
