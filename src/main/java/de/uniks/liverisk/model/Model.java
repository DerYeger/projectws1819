package de.uniks.liverisk.model;

import org.fulib.yaml.YamlIdMap;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Model {

    private static Model model;

    private static final String SAVEGAME_PATH = "./savegame.yaml";

    private YamlIdMap yamlIdMap = new YamlIdMap(Game.class.getPackage().getName());

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

    public boolean saveGame() {
        boolean savingSuccessful = false;
        String yaml = yamlIdMap.encode(game);

        try {

            File file = new File(SAVEGAME_PATH);

            if (!file.exists()) {
                file.createNewFile();
            }

            Files.write(Paths.get(file.toURI()), yaml.getBytes(StandardCharsets.UTF_8));
            savingSuccessful = true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return savingSuccessful;
    }

    public boolean loadSavedGame() {
        boolean loadingSuccessful = false;
        File file = new File(SAVEGAME_PATH);

        if (!file.exists()) {
           return false;
        }

        try {
            byte[] bytes = Files.readAllBytes(Paths.get(file.toURI()));
            String yaml = new String(bytes, StandardCharsets.UTF_8);
            yamlIdMap.decode(yaml, game);
            loadingSuccessful = true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return loadingSuccessful;
    }
}
