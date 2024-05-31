package com.bookkeepersmc.entrypoint;

import com.bookkeepersmc.entrypoint.api.EventListener;

public interface WrapperListener {
    EventListener getWithoutCheck();
}
