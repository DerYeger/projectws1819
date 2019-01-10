package de.uniks.liverisk.util;

import javafx.event.EventHandler;

//bases on https://www.baeldung.com/java-lambda-exceptions
//accessed on 10.02.2019 10:48
public class ThrowingEventHandlerWrapper {

    public static <T extends javafx.event.Event> EventHandler<T> throwingEventHandlerWrapper(ThrowingEventHandler<T, Exception> throwingEventHandler) {
        return i -> {
            try {
                throwingEventHandler.accept(i);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        };
    }
}
