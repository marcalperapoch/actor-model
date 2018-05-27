package com.perapoch.concurrency.core;

public final class ActorRecipe {

    private final Class klass;
    private final String name;
    private final Object[] args;

    public ActorRecipe(Class klass, String name, Object[] args) {
        this.klass = klass;
        this.name = name;
        this.args = args;
    }

    public Class getKlass() {
        return klass;
    }

    public String getName() {
        return name;
    }

    public Object[] getArgs() {
        return args;
    }
}
