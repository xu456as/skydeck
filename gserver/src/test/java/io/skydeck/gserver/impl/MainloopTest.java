package io.skydeck.gserver.impl;

import io.skydeck.gserver.GserverApplicationTests;
import io.skydeck.gserver.domain.player.Player;
import io.skydeck.gserver.domain.hero.shu.SHeroGuanyu;
import io.skydeck.gserver.engine.GameEngine;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import java.util.*;

public class MainloopTest extends GserverApplicationTests {

    @Resource
    private GameEngine engine;

    @BeforeEach
    public void init() {
        List<Player> playerList = new ArrayList<>();
        Player playerA = new Player();
        Player playerB = new Player();
        playerA.init(engine, 0, "A", new SHeroGuanyu(), new SHeroGuanyu());
        playerB.init(engine, 1, "B", new SHeroGuanyu(), new SHeroGuanyu());
        playerList.add(playerA);
        playerList.add(playerB);
        engine.initPlayers(playerList);
    }

    @Test
    public void test() throws Exception {
        Player player = engine.getCurrentPlayer();
        Assert.isTrue(player.getHealth() == 5, "health is not correct");
        Assert.isTrue(player.getMaxHealth() == 5, "health is not correct");
        Assert.isTrue(player.allAbilities().size() == 4, "abilities count is not correct");
        new Thread(() -> engine.mainLoop()).start();
        int times = 0;
        while (true) {
            try {
                Thread.sleep(2000);
                if  (times++ >= 4) {
                    break;
                }
            }catch (Exception ignore) {}
            engine.endActivePhase();
        }
    }
}
