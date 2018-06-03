package com.perapoch.concurrency;

import com.perapoch.concurrency.core.Message;

import java.nio.file.Path;

public interface ActorContext extends ActorFactory {

    void tell(ActorAddress to, Message msg, ActorAddress from);

    Path getPath();
    
}
