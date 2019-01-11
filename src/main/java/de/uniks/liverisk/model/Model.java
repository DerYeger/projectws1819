package de.uniks.liverisk.model;

public class Model {

    private static Model instance;

    private Game game;

    private Model() {
        game = new Game();
    }

    public static Model getInstance() {
        if (instance == null) {
            instance = new Model();
        }
        return instance;
    }

    public static void resetModel() {
        instance = new Model();
    }

    public Game getGame() {
        return game;
    }

}
