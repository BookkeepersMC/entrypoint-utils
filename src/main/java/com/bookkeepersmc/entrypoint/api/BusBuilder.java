package com.bookkeepersmc.entrypoint.api;

import com.bookkeepersmc.entrypoint.BusBuilderImpl;

public interface BusBuilder {
    public static BusBuilder builder() {
        return new BusBuilderImpl();
    }

    BusBuilder setExceptionHandler(EventExceptionHandler handler);
    BusBuilder startShutdown();
    BusBuilder checkTypesOnDispatch();

    default BusBuilder markerType(Class<?> markerInterface) {
        if (!markerInterface.isInterface()) throw new IllegalArgumentException("Cannot specify a class marker type");
        return classChecker(eventType -> {
            if (!markerInterface.isAssignableFrom(eventType)) {
                throw new IllegalArgumentException("This bus only accepts subclasses of " + markerInterface + ", which " + eventType + " is not.");
            }
        });
    }

    BusBuilder classChecker(EventClassChecker classChecker);

    BusBuilder allowPerPhasePost();

    IEventBus build();
}
