package de.uniks.liverisk.controller;

import de.uniks.liverisk.model.*;

import org.junit.Assert;
import org.junit.Test;

public class GameControllerTests {

    //Alice moves a unit
    //Start: Alice has 5 units on platform 1. No units are on platform 2, which is connected to platform 1.
    //Action: Alice moves to platform 2.
    //End: Alice has 2 units on platform 1 and 3 units on platform 2.
    @Test
    public void testMove() {
        //setup
        GameController gc = new GameController();
        Game game = new Game();
        Platform platform1 = new Platform().setCapacity(5);
        Platform platform2 = new Platform().setCapacity(3);
        Player alice = new Player();

        game.withPlatforms(platform1, platform2).withPlayers(alice);
        alice.withPlatforms(platform1);
        platform1.withUnits(new Unit(), new Unit(), new Unit(), new Unit(), new Unit()).withNeighbors(platform2);

        //action
        Assert.assertTrue(gc.move(platform1, platform2));

        //asserts
        Assert.assertEquals(2, platform1.getUnits().size());
        Assert.assertEquals(3, platform2.getUnits().size());
        Assert.assertTrue(alice.getPlatforms().contains(platform1));
        Assert.assertTrue(alice.getPlatforms().contains(platform2));
    }

    //Bob attacks Alice
    //Start: Bob has 3 units on one platform, which is connected to a platform containing 1 unit owned by Alice.
    //Action: Bob attacks Alice's platform from his own.
    //End: Bob and Alice lose 1 unit each. Bob has 1 unit on the attacking and 1 unit on the attacked platform.
    @Test
    public void testAttack() {
        //setup
        GameController gc = new GameController();
        Game game = new Game();
        Platform platformA = new Platform();
        Platform platformB = new Platform();
        Player alice = new Player();
        Player bob = new Player();

        game.withPlatforms(platformA, platformB).withPlayers(alice, bob);

        platformA.withUnits(new Unit())
                .setPlayer(alice)
                .setCapacity(3)
                .withNeighbors(platformB);
        platformB.withUnits(new Unit(), new Unit(), new Unit())
                .setPlayer(bob)
                .setCapacity(3);

        //actions
        Assert.assertTrue(gc.attack(platformB, platformA));

        //asserts
        Assert.assertEquals(0, alice.getPlatforms().size());
        Assert.assertEquals(2, bob.getPlatforms().size());
        Assert.assertEquals(1, platformA.getUnits().size());
        Assert.assertEquals(1, platformB.getUnits().size());
    }

    //Alice reenforces a platform
    //Start: Alice has 1 unit on a platform with a capacity of 3 and 1 unit in her reserves.
    //Action: Alice sends 1 unit from her reserves on the platform.
    //End: Alice has 2 units on the platform and no units in her reserves.
    @Test
    public void testReenforce() {
        //setup
        GameController gc = new GameController();
        Game game = new Game();
        Platform platform = new Platform();
        Player alice = new Player();

        game.withPlatforms(platform)
                .withPlayers(alice);
        platform.setCapacity(3)
                .withUnits(new Unit());
        alice.withPlatforms(platform)
                .withUnits(new Unit());

        //action
        Assert.assertTrue(gc.reenforce(platform));

        //asserts
        Assert.assertEquals(2, platform.getUnits().size());
        Assert.assertTrue(alice.getUnits().isEmpty());
    }

    //none of the following tests is required to reach 100% CodeCoverage
    //and thus can/should be disregarded for the assignment

    @Test
    public void testAdditionalMoveCases() {
        //setup
        GameController gc = new GameController();
        Game game = new Game();
        Platform platform1 = new Platform().setCapacity(5).withUnits(new Unit(), new Unit(), new Unit(), new Unit(), new Unit());
        Platform platform2 = new Platform().setCapacity(3);
        Platform platform3 = new Platform().setCapacity(3).withUnits(new Unit(), new Unit(), new Unit());
        Platform platform4 = new Platform().setCapacity(5);
        Platform platform5 = new Platform().setCapacity(5).withUnits(new Unit(), new Unit(), new Unit(), new Unit());
        Platform platform6 = new Platform().setCapacity(5).withUnits(new Unit(), new Unit(), new Unit());
        Player alice = new Player().withPlatforms(platform1);
        Player bob = new Player().withPlatforms(platform3, platform5, platform6);

        platform1.withNeighbors(platform2);
        platform3.withNeighbors(platform4);
        platform5.withNeighbors(platform6);

        game.withPlatforms(platform1, platform2, platform3, platform4, platform5, platform6).withPlayers(alice, bob);

        //actions and asserts
        Assert.assertTrue(gc.move(platform1, platform2));   //destination cap reached
        Assert.assertTrue(gc.move(platform3, platform4));   //only 1 unit left on source
        Assert.assertTrue(gc.move(platform5, platform6));   //destination cap reached, units already present

        Assert.assertEquals(2, platform1.getUnits().size());
        Assert.assertEquals(3, platform2.getUnits().size());

        Assert.assertEquals(1, platform3.getUnits().size());
        Assert.assertEquals(2, platform4.getUnits().size());

        Assert.assertEquals(2, platform5.getUnits().size());
        Assert.assertEquals(5, platform6.getUnits().size());
    }

    @Test
    public void testBadMoveCases() {
        //setup
        GameController gc = new GameController();
        Game game = new Game();
        Platform platform1 = new Platform().setCapacity(5);
        Platform platform2 = new Platform().setCapacity(3);
        Platform platform3 = new Platform().setCapacity(3);
        Platform platform4 = new Platform().setCapacity(5);
        Player alice = new Player();
        Player bob = new Player();

        game.withPlatforms(platform1, platform2, platform3).withPlayers(alice, bob);
        alice.withPlatforms(platform1, platform2);
        bob.withPlatforms(platform4);
        platform1.withUnits(new Unit(), new Unit(), new Unit(), new Unit(), new Unit()).withNeighbors(platform2, platform4);
        platform2.withUnits(new Unit(), new Unit(), new Unit());
        platform4.withNeighbors(platform2, platform3);

        //actions and assert
        //null parameter
        try {
            gc.move(null, null);
            Assert.fail();
        } catch (Exception e) {}

        //source has now owner
        Assert.assertFalse(gc.move(platform3, platform2));
        Assert.assertNull(platform3.getPlayer());

        //invalid move to hostile owned platform
        Assert.assertFalse(gc.move(platform1, platform4));
        Assert.assertNotSame(platform1.getPlayer(), platform4.getPlayer());

        //invalid move between unconnected platforms
        Assert.assertFalse(gc.move(platform1, platform3));
        Assert.assertFalse(platform1.getNeighbors().contains(platform3));

        //target platform at max capacity
        Assert.assertFalse(gc.move(platform1, platform2));
        Assert.assertEquals(platform2.getCapacity(), platform2.getUnits().size());

        //source platform has less than two units
        Assert.assertFalse(gc.move(platform4, platform3));
        Assert.assertTrue(platform4.getUnits().size() < 2);
    }

    @Test
    public void testAdditionalAttackCases() {
        //setup
        GameController gc = new GameController();
        Game game = new Game();
        Platform platformA1 = new Platform();
        Platform platformA2 = new Platform();
        Platform platformA3 = new Platform();
        Platform platformA4 = new Platform();
        Platform platformA5 = new Platform();
        Platform platformB1 = new Platform();
        Platform platformB2 = new Platform();
        Platform platformB3 = new Platform();
        Platform platformB4 = new Platform();
        Platform platformB5 = new Platform();
        Player alice = new Player().setName("Alice");
        Player bob = new Player().setName("Bob");

        game.withPlatforms(platformA1, platformA2, platformA3, platformA4, platformA5, platformB1, platformB2, platformB3, platformB4, platformB5)
                .withPlayers(alice, bob);

        platformA1.withUnits(new Unit(), new Unit(), new Unit(), new Unit(), new Unit())
                .setPlayer(alice)
                .withNeighbors(platformB1)
                .setCapacity(5);
        platformA2.withUnits(new Unit(), new Unit(), new Unit())
                .setPlayer(alice)
                .withNeighbors(platformB2)
                .setCapacity(3);
        platformA3.withUnits(new Unit(), new Unit(), new Unit())
                .setPlayer(alice)
                .withNeighbors(platformB3)
                .setCapacity(3);
        platformA4.withUnits(new Unit(), new Unit(), new Unit())
                .setPlayer(alice)
                .withNeighbors(platformB4)
                .setCapacity(3);
        platformA5.withUnits(new Unit(), new Unit(), new Unit(), new Unit(), new Unit(), new Unit(), new Unit(), new Unit(), new Unit(), new Unit())
                .setPlayer(alice)
                .withNeighbors(platformB5)
                .setCapacity(10);
        platformB1.withUnits(new Unit(), new Unit(), new Unit())
                .setPlayer(bob)
                .setCapacity(3);
        platformB2.withUnits(new Unit(), new Unit(), new Unit())
                .setPlayer(bob)
                .setCapacity(3);
        platformB3.withUnits(new Unit(), new Unit(), new Unit(), new Unit(), new Unit())
                .setPlayer(bob)
                .setCapacity(5);
        platformB4.withUnits(new Unit(), new Unit())
                .setPlayer(bob)
                .setCapacity(3);
        platformB5.withUnits(new Unit(), new Unit(), new Unit())
                .setPlayer(bob)
                .setCapacity(3);

        //actions
        Assert.assertTrue(gc.attack(platformA1, platformB1));   //successful attack
        Assert.assertTrue(gc.attack(platformA2, platformB2));   //unsuccessful attack
        Assert.assertTrue(gc.attack(platformA3, platformB3));   //unsuccessful attack
        Assert.assertTrue(gc.attack(platformA4, platformB4));   //successful attack without unit to take over platform
        Assert.assertTrue(gc.attack(platformA5, platformB5));   //successful attack with surplus unit

        //asserts
        int aliceUnitCount = 0;
        for (Platform platform : alice.getPlatforms()) {
            aliceUnitCount += platform.getUnits().size();
        }

        int bobUnitCount = 0;
        for (Platform platform : bob.getPlatforms()) {
            bobUnitCount += platform.getUnits().size();
        }

        Assert.assertEquals(12, aliceUnitCount);
        Assert.assertEquals(4, bobUnitCount);
        Assert.assertEquals(7, alice.getPlatforms().size());
        Assert.assertEquals(2, bob.getPlatforms().size());

        Assert.assertEquals(alice, platformA1.getPlayer());
        Assert.assertEquals(alice, platformA2.getPlayer());
        Assert.assertEquals(alice, platformA3.getPlayer());
        Assert.assertEquals(alice, platformA4.getPlayer());
        Assert.assertEquals(alice, platformA5.getPlayer());
        Assert.assertEquals(alice, platformB1.getPlayer());
        Assert.assertEquals(bob, platformB2.getPlayer());
        Assert.assertEquals(bob, platformB3.getPlayer());
        Assert.assertNull(platformB4.getPlayer());
        Assert.assertEquals(alice, platformB5.getPlayer());
        Assert.assertEquals(1, platformA1.getUnits().size());
        Assert.assertEquals(1, platformA2.getUnits().size());
        Assert.assertEquals(1, platformA3.getUnits().size());
        Assert.assertEquals(1, platformA4.getUnits().size());
        Assert.assertEquals(4, platformA5.getUnits().size());
        Assert.assertEquals(1, platformB1.getUnits().size());
        Assert.assertEquals(1, platformB2.getUnits().size());
        Assert.assertEquals(3, platformB3.getUnits().size());
        Assert.assertEquals(0, platformB4.getUnits().size());
        Assert.assertEquals(3, platformB5.getUnits().size());
    }

    @Test
    public void testBadAttackCases() {
        //setup
        GameController gc = new GameController();
        Game game = new Game();
        Platform platformA = new Platform();
        Platform platformB = new Platform();
        Platform platformC = new Platform();
        Platform platformD = new Platform();
        Player alice = new Player();
        Player bob = new Player();

        game.withPlatforms(platformA, platformB, platformC).withPlayers(alice, bob);

        platformA.withUnits(new Unit())
                .setPlayer(alice)
                .withNeighbors(platformB, platformC)
                .setCapacity(3);
        platformB.withUnits(new Unit(), new Unit(), new Unit())
                .withNeighbors(platformD)
                .setPlayer(bob)
                .setCapacity(3);
        platformC.setCapacity(3);
        platformD.withUnits(new Unit())
                .setPlayer(bob)
                .setCapacity(3);

        //actions & asserts
        //null parameters
        try {
            gc.attack(null, null);
            Assert.fail();
        } catch (Exception e) {}

        //source has now owner
        Assert.assertFalse(gc.attack(platformC, platformA));
        Assert.assertNull(platformC.getPlayer());

        //attacking unowned platform
        Assert.assertFalse(gc.attack(platformA, platformC));
        Assert.assertNull(platformC.getPlayer());

        //invalid attack between unconnected platforms
        Assert.assertFalse(gc.attack(platformA, platformD));
        Assert.assertFalse(platformA.getNeighbors().contains(platformD));

        //source platform has less than two units
        Assert.assertFalse(gc.move(platformA, platformB));
        Assert.assertTrue(platformA.getUnits().size() < 2);

        //player attacks own platform
        Assert.assertFalse(gc.attack(platformB, platformD));
        Assert.assertEquals(platformB.getPlayer(), platformD.getPlayer());
    }

    @Test
    public void testAdditionalReenforceCases() {
        //setup
        GameController gc = new GameController();
        Game game = new Game();
        Platform platformA = new Platform().setCapacity(3).withUnits(new Unit());
        Platform platformB = new Platform().setCapacity(3).withUnits(new Unit());
        Platform platformC = new Platform().setCapacity(3).withUnits(new Unit());
        Player alice = new Player();
        Player bob = new Player();
        Player clemens = new Player();

        game.withPlatforms(platformA, platformB, platformC).withPlayers(alice, bob, clemens);
        alice.withPlatforms(platformA).withUnits(new Unit(), new Unit());
        bob.withPlatforms(platformB).withUnits(new Unit());
        clemens.withPlatforms(platformC).withUnits(new Unit(), new Unit(), new Unit(), new Unit(), new Unit(), new Unit());

        //actions & asserts
        Assert.assertTrue(gc.reenforce(platformA));    //spare unit count fits exactly
        Assert.assertTrue(gc.reenforce(platformB));    //not enough spare units to fill
        Assert.assertTrue(gc.reenforce(platformC));    //more than enough spare units

        //asserts
        Assert.assertTrue(alice.getUnits().isEmpty());
        Assert.assertTrue(bob.getUnits().isEmpty());
        Assert.assertEquals(4, clemens.getUnits().size());
        Assert.assertEquals(3, platformA.getUnits().size());
        Assert.assertEquals(2, platformB.getUnits().size());
        Assert.assertEquals(3, platformC.getUnits().size());
    }

    @Test
    public void testBadReenforceCases() {
        //setup
        GameController gc = new GameController();
        Game game = new Game();
        Platform platformA = new Platform().setCapacity(3).withUnits(new Unit(), new Unit(), new Unit());
        Platform platformB = new Platform().setCapacity(3).withUnits(new Unit());
        Platform platformC = new Platform().setCapacity(3).withUnits(new Unit());
        Platform platformD = new Platform().setCapacity(3);
        Player alice = new Player();
        Player bob = new Player();

        game.withPlatforms(platformA, platformB, platformC, platformD).withPlayers(alice, bob);
        alice.withPlatforms(platformA).withUnits(new Unit());
        bob.withPlatforms(platformB);

        //actions & asserts
        Assert.assertFalse(gc.reenforce(platformA));    //reenforcing full platform
        Assert.assertFalse(gc.reenforce(platformB));    //no spare units
        Assert.assertFalse(gc.reenforce(platformC));    //reenforcing unowned platform with units
        Assert.assertFalse(gc.reenforce(platformD));    //reenforcing unowned platform without units
    }

    @Test
    public void testInitGame() {
        //setup
        Model.resetModel();
        new GameController().initGame(3);

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
    public void testPlayerConfigurationValidation() {
        //setup
        Model.resetModel();
        GameController gc = new GameController();
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
