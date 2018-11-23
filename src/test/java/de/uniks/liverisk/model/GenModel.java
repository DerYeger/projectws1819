package de.uniks.liverisk.model;

import org.fulib.Fulib;
import org.fulib.builder.ClassBuilder;
import org.fulib.builder.ClassModelBuilder;
import org.fulib.classmodel.ClassModel;

public class GenModel {

    public static void main(String... args) {

        ClassModelBuilder mb = Fulib.classModelBuilder("de.uniks.liverisk.model", "src/main/java");

        // Classes
        ClassBuilder game = mb.buildClass("Game");
        ClassBuilder platform = mb.buildClass("Platform");
        ClassBuilder player = mb.buildClass("Player");
        ClassBuilder unit = mb.buildClass("Unit");

        // Attributes
        game.buildAttribute("name", ClassModelBuilder.STRING);

        platform.buildAttribute("capacity", ClassModelBuilder.INT);
        platform.buildAttribute("xPos", ClassModelBuilder.DOUBLE);
        platform.buildAttribute("yPos", ClassModelBuilder.DOUBLE);

        player.buildAttribute("name", ClassModelBuilder.STRING);
        player.buildAttribute("color", ClassModelBuilder.STRING);

        // Assoc
        game.buildAssociation(player, "players", ClassModelBuilder.MANY, "game", ClassModelBuilder.ONE);
        game.buildAssociation(player, "currentPlayer", ClassModelBuilder.ONE, "currentGame", ClassModelBuilder.ONE);
        game.buildAssociation(player, "winner", ClassModelBuilder.ONE, "gameWon", ClassModelBuilder.ONE);
        game.buildAssociation(platform, "platforms", ClassModelBuilder.MANY, "game", ClassModelBuilder.ONE);
        game.buildAssociation(platform, "selectedPlatform", ClassModelBuilder.ONE, "selectedBy", ClassModelBuilder.ONE);

        platform.buildAssociation(platform, "neighbors", ClassModelBuilder.MANY, "neighbors", ClassModelBuilder.MANY);
        platform.buildAssociation(unit, "units", ClassModelBuilder.MANY, "platform", ClassModelBuilder.ONE);

        player.buildAssociation(unit, "units", ClassModelBuilder.MANY, "player", ClassModelBuilder.ONE);
        player.buildAssociation(platform, "platforms", ClassModelBuilder.MANY, "player", ClassModelBuilder.ONE);

        ClassModel model = mb.getClassModel();

        Fulib.generator().generate(model);
    }
}

