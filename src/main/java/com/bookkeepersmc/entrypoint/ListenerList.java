package com.bookkeepersmc.entrypoint;

import com.bookkeepersmc.entrypoint.api.Priority;
import com.bookkeepersmc.entrypoint.api.CancellableEvent;
import com.bookkeepersmc.entrypoint.api.EventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicReference;

public class ListenerList {
    private boolean rebuild = true;
    private final AtomicReference<EventListener[]> listeners = new AtomicReference<>();
    private final AtomicReference<EventListener[][]> perPhaseListeners = new AtomicReference<>();
    private final ArrayList<ArrayList<EventListener>> priorities;
    private ListenerList parent;
    private List<ListenerList> children;
    private final Semaphore writeLock = new Semaphore(1, true);
    private final boolean canUnwrapListeners;
    private final boolean buildPerPhaseList;

    ListenerList(Class<?> eventClass, boolean buildPerPhaseList) {
        int count = Priority.values().length;
        priorities = new ArrayList<>(count);

        for (int i = 0; i < count; i++) {
            priorities.add(new ArrayList<>());
        }

        canUnwrapListeners = !CancellableEvent.class.isAssignableFrom(eventClass);
        this.buildPerPhaseList = buildPerPhaseList;
    }

    ListenerList(Class<?> eventClass, ListenerList parent, boolean buildPerPhaseList) {
        this(eventClass, buildPerPhaseList);
        this.parent = parent;
        this.parent.addChild(this);
    }

    private ArrayList<EventListener> getListeners(Priority priority) {
        writeLock.acquireUninterruptibly();
        ArrayList<EventListener> ret = new ArrayList<>(priorities.get(priority.ordinal()));
        writeLock.release();
        if (parent != null) {
            ret.addAll(parent.getListeners(priority));
        }
        return ret;
    }

    public EventListener[] getListeners() {
        if (shouldRebuild()) buildCache();
        return listeners.get();
    }

    public EventListener[] getPhaseListeners(Priority phase) {
        if (!buildPerPhaseList) {
            throw new IllegalStateException("buildPerPhaseList is false!");
        }

        if (shouldRebuild()) buildCache();
        return perPhaseListeners.get()[phase.ordinal()];
    }

    protected boolean shouldRebuild() {
        return rebuild;
    }

    protected void forceRebuild() {
        this.rebuild = true;
        if (this.children != null) {
            synchronized (this.children) {
                for (ListenerList child : this.children)
                    child.forceRebuild();
            }
        }
    }

    private void addChild(ListenerList child) {
        if (this.children == null)
            this.children = Collections.synchronizedList(new ArrayList<>(2));
        this.children.add(child);
    }

    private void buildCache() {
        if (parent != null && parent.shouldRebuild()) {
            parent.buildCache();
        }
        ArrayList<EventListener> ret = new ArrayList<>();
        EventListener[][] perPhaseListeners = buildPerPhaseList ? new EventListener[Priority.values().length][] : null;

        for (Priority phase : Priority.values()) {
            var phaseListeners = getListeners(phase);
            unwrapListeners(phaseListeners);
            ret.addAll(phaseListeners);

            if (perPhaseListeners != null) {
                perPhaseListeners[phase.ordinal()] = phaseListeners.toArray(EventListener[]::new);
            }
        }

        this.listeners.set(ret.toArray(new EventListener[0]));
        this.perPhaseListeners.set(perPhaseListeners);

        rebuild = false;
    }

    private void unwrapListeners(List<EventListener> ret) {
        if (canUnwrapListeners) {
            for (int i = 0; i < ret.size(); ++i) {
                if (ret.get(i) instanceof WrapperListener wrapper) {
                    ret.set(i, wrapper.getWithoutCheck());
                }
            }
        }
    }

    public void register(Priority priority, EventListener listener) {
        writeLock.acquireUninterruptibly();
        priorities.get(priority.ordinal()).add(listener);
        writeLock.release();
        this.forceRebuild();
    }

    public void unregister(EventListener listener) {
        writeLock.acquireUninterruptibly();
        priorities.stream().filter(list -> list.remove(listener)).forEach(list -> this.forceRebuild());
        writeLock.release();
    }
}
