package com.bookkeepersmc.entrypoint.api;

public interface EventClassChecker {

    void check(Class<? extends Event> eventClass) throws IllegalArgumentException;
}
