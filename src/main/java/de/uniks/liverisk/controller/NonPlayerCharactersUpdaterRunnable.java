package de.uniks.liverisk.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class NonPlayerCharactersUpdaterRunnable implements Runnable {

    private ArrayList<NonPlayerCharacter> nonPlayerCharacters;

    public NonPlayerCharactersUpdaterRunnable(final Collection<NonPlayerCharacter> nonPlayerCharacters) {
        Objects.requireNonNull(nonPlayerCharacters);
        this.nonPlayerCharacters = new ArrayList<>(nonPlayerCharacters);
    }

    @Override
    public void run() {
        nonPlayerCharacters.forEach(NonPlayerCharacter::update);
    }
}
