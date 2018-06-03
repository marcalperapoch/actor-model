package com.perapoch.concurrency.core;

import com.perapoch.concurrency.ActorAddress;
import com.perapoch.concurrency.ActorContext;

import java.nio.file.Path;
import java.util.Objects;

public final class ActorAddressImpl implements ActorAddress {

    private final ActorContext context;
    private final String name;

    ActorAddressImpl(ActorContext context) {
        this.context = context;
        this.name = context.getPath().getName(context.getPath().getNameCount() - 1).toString();
    }

    @Override
    public void tell(Message msg, ActorAddress from) {
        checkContext();
        context.tell(this, msg, from);
    }

    @Override
    public void tell(Message msg) {
        checkContext();
        context.tell(this, msg, NO_SENDER);
    }

    /*public <T extends Actor> void restart() {
        checkContext();
        context.restart(address);
    }*/

    private void checkContext() {
        if (context == null) {
            throw new UnsupportedOperationException("You can not send msg to a no sender address");
        }
    }

    @Override
    public ActorContext getContext() {
        return context;
    }

    @Override
    public Path getPath() {
        checkContext();
        return context.getPath();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActorAddressImpl that = (ActorAddressImpl) o;
        return Objects.equals(context, that.context) &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(context, name);
    }

    @Override
    public String toString() {
        return "ActorAddressImpl{" +
                "context=" + context +
                ", name='" + name + '\'' +
                '}';
    }
}
