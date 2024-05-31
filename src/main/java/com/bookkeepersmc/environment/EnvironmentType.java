package com.bookkeepersmc.environment;

public enum EnvironmentType {
    CLIENT,

    SERVER;

    public boolean isServer() {
        return !isClient();
    }

    public boolean isClient() {
        return this == CLIENT;
    }
}
