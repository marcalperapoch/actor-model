package com.perapoch.concurrency.core;

final class ActorRecipe {

    private final Class klass;
    private final String name;
    private final Object[] args;

    ActorRecipe(Class klass, String name, Object[] args) {
        this.klass = klass;
        this.name = name;
        this.args = args;
    }

    Class getKlass() {
        return klass;
    }

    String getName() {
        return name;
    }

    Object[] getArgs() {
        return args;
    }
}
