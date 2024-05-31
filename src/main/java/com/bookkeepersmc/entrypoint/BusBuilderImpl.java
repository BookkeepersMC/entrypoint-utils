package com.bookkeepersmc.entrypoint;

import com.bookkeepersmc.entrypoint.api.EventExceptionHandler;
import com.bookkeepersmc.entrypoint.api.IEventBus;
import com.bookkeepersmc.entrypoint.api.BusBuilder;
import com.bookkeepersmc.entrypoint.api.EventClassChecker;

public class BusBuilderImpl implements BusBuilder {
    public EventExceptionHandler exceptionHandler;
    public boolean startShutdown = false;
    public boolean checkTypesOnDispatch = false;
    public EventClassChecker classChecker = eventClass -> {};
    public boolean allowPerPhasePost = false;

    @Override
    public BusBuilder setExceptionHandler(EventExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
        return this;
    }

    @Override
    public BusBuilder startShutdown() {
        this.startShutdown = true;
        return this;
    }

    @Override
    public BusBuilder checkTypesOnDispatch() {
        this.checkTypesOnDispatch = true;
        return this;
    }

    @Override
    public BusBuilder classChecker(EventClassChecker checker) {
        this.classChecker = checker;
        return this;
    }

    @Override
    public BusBuilder allowPerPhasePost() {
        this.allowPerPhasePost = true;
        return this;
    }

    @Override
    public IEventBus build() {
        return new EventBus(this);
    }
}
