package com.bookkeepersmc.entrypoint.api;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.MustBeInvokedByOverriders;

public interface CancellableEvent {

    @MustBeInvokedByOverriders
    default void setCancelled(boolean cancelled) {
        ((Event) this).isCancelled = cancelled;
    }

    @ApiStatus.NonExtendable
    default boolean isCancelled() {
        return ((Event) this).isCancelled;
    }
}
