package com.perapoch.concurrency.core;

public abstract class Actor {

    private ActorAddress address;

    public Actor() {
    }

    protected abstract void onReceive(Message msg);


    void setAddress(ActorAddress address) {
        this.address = address;
    }

    protected ActorAddress self() {
        return address;
    }
}
