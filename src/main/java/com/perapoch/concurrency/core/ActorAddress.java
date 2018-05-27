package com.perapoch.concurrency.core;

import java.util.Objects;

public final class ActorAddress {

    private static final ActorAddress NO_SENDER = new ActorAddress("no address");

    private final ActorRegistry registry;
    private final String address;

    private ActorAddress(String address) {
        this(null, address);
    }

    ActorAddress(ActorRegistry registry, String address) {
        this.registry = registry;
        this.address = address;
    }

    public void tell(Message msg, ActorAddress from) {
        checkRegistry();
        registry.tell(this, msg, from);
    }

    public void tell(Message msg) {
        checkRegistry();
        registry.tell(this, msg, NO_SENDER);
    }

    public <T extends Actor> void restart() {
        checkRegistry();
        registry.restart(address);
    }

    private void checkRegistry() {
        if (registry == null) {
            throw new UnsupportedOperationException("You can not send msg to a no sender address");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActorAddress that = (ActorAddress) o;
        return Objects.equals(address, that.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address);
    }

    @Override
    public String toString() {
        return "ActorAddress{" +
                "address='" + address + '\'' +
                '}';
    }
}
