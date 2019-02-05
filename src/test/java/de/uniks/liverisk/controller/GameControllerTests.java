package de.uniks.liverisk.controller;

import de.uniks.liverisk.model.Game;
import de.uniks.liverisk.model.Model;
import de.uniks.liverisk.model.Platform;
import de.uniks.liverisk.model.Player;

import org.junit.Assert;
import org.junit.Test;

public class GameControllerTests {

    @Test
    public void testInitGame() throws Exception {
        //setup
        Model.clear();
        GameController.clear();
        GameController.getInstance().initGame(3);

        Game game = Model.getInstance().getGame();

        //asserts
        Assert.assertEquals(3, game.getPlayers().size());

        for (Player player : game.getPlayers()) {
            Assert.assertNotNull(player.getName());
            Assert.assertNotNull(player.getColor());
            Assert.assertEquals(1, player.getPlatforms().size());
            Assert.assertEquals(4, player.getUnits().size());
        }
        for (Platform platform : game.getPlatforms()) {
            Assert.assertFalse(platform.getNeighbors().isEmpty());
            Assert.assertNotEquals(0, platform.getCapacity());
            if (platform.getPlayer() == null) {
                Assert.assertTrue(platform.getUnits().isEmpty());
            } else {
                Assert.assertEquals(1, platform.getUnits().size());
            }
        }
    }

    @Test
    public void testPlayerConfigurationValidation() throws Exception {
        //setup
        Model.clear();
        GameController.clear();
        GameController gc = GameController.getInstance();
        gc.initGame(2);

        Game game = Model.getInstance().getGame();

        Player playerOne = game.getPlayers().get(0);
        Player playerTwo = game.getPlayers().get(1);

        //actions & asserts
        Assert.assertTrue(gc.playerConfigurationIsValid());

        playerOne.setName(playerTwo.getName());
        Assert.assertFalse(gc.playerConfigurationIsValid());

        playerOne.setColor(playerTwo.getColor());
        Assert.assertFalse(gc.playerConfigurationIsValid());

        playerOne.setName("New Name");
        Assert.assertFalse(gc.playerConfigurationIsValid());

        playerOne.setColor("New Color");
        Assert.assertTrue(gc.playerConfigurationIsValid());

        playerOne.setName("");
        Assert.assertFalse(gc.playerConfigurationIsValid());

        playerOne.setName("New Name");
        playerOne.setColor("");
        Assert.assertFalse(gc.playerConfigurationIsValid());
    }
}
