package com.bookkeepersmc.entrypoint.api;

import java.util.function.Consumer;

/**
 * Like Neo/Forge's Event bus, but for notebook.
 * <p>
 * Registers events
 */
public interface IEventBus {

    void register(Object target);

    <T extends Event> void addListener(Consumer<T> consumer);

    <T extends Event> void addListener(Class<T> eventType, Consumer<T> consumer);

    <T extends Event> void addListener(Priority priority, Consumer<T> consumer);

    <T extends Event> void addListener(Priority priority, Class<T> eventType, Consumer<T> consumer);

    <T extends Event> void addListener(Priority priority, boolean receiveCancelled, Consumer<T> consumer);

    <T extends Event> void addListener(Priority priority, boolean receiveCancelled, Class<T> eventType, Consumer<T> consumer);

    <T extends Event> void addListener(boolean receiveCanceled, Consumer<T> consumer);

    <T extends Event> void addListener(boolean receiveCanceled, Class<T> eventType, Consumer<T> consumer);

    void unregister(Object target);

    <T extends Event> T post(T event);

    <T extends Event> T post(Priority phase, T event);

    void start();
}
