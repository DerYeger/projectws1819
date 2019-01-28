package de.uniks.liverisk.model;

public class Model {

    private static Model model;

    private Game game;

    private Model() {
        game = new Game();
    }

    public static Model getInstance() {
        if (model == null) model = new Model();
        return model;
    }

    public static void clear() {
        model = null;
    }

    public Game getGame() {
        return game;
    }
}
