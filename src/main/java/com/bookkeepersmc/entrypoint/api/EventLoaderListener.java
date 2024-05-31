package com.bookkeepersmc.entrypoint.api;

import com.bookkeepersmc.entrypoint.EventListenerFactory;
import com.bookkeepersmc.entrypoint.WrapperListener;

import java.lang.reflect.Method;

public final class EventLoaderListener extends EventListener implements WrapperListener {
    private final EventListener handler;
    private final EventLoader info;
    private String readable;

    public EventLoaderListener(Object target, Method method) {
        handler = EventListenerFactory.create(method, target);

        info = method.getAnnotation(EventLoader.class);
        readable = "@EventLoader: " + target + " " + method.getName() + org.objectweb.asm.Type.getMethodDescriptor(method);
    }

    @Override
    public void invoke(Event event) {
        if (handler != null) {
            if (!((CancellableEvent) event).isCancelled()) {
                handler.invoke(event);
            }
        }
    }

    public Priority getPriority() {
        return info.priority();
    }

    public String toString() {
        return readable;
    }

    @Override
    public EventListener getWithoutCheck() {
        return handler;
    }

}
