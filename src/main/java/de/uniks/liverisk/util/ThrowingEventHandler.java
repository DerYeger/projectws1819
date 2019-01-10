package de.uniks.liverisk.util;

//bases on https://www.baeldung.com/java-lambda-exceptions
//accessed on 10.02.2019 10:48
public interface ThrowingEventHandler<T extends javafx.event.Event, E extends Exception> {
    void accept(T t) throws E;
}
