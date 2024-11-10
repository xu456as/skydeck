package io.skydeck.gserver.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.skydeck.gserver.GserverApplicationTests;
import io.skydeck.gserver.domain.card.CardBase;
import io.skydeck.gserver.domain.card.GearCardBase;
import io.skydeck.gserver.domain.card.basic.JinkCard;
import io.skydeck.gserver.domain.card.basic.SlashCard;
import io.skydeck.gserver.domain.card.gear.DinglanyemingzhuCard;
import io.skydeck.gserver.domain.dto.CardTransferContext;
import io.skydeck.gserver.domain.dto.CardUseDTO;
import io.skydeck.gserver.domain.dto.ProactiveActionDTO;
import io.skydeck.gserver.domain.player.Player;
import io.skydeck.gserver.domain.hero.shu.SHeroGuanyu;
import io.skydeck.gserver.domain.skill.ZhihengSkill;
import io.skydeck.gserver.engine.GameEngine;
import io.skydeck.gserver.impl.settlement.SlashUseSettlement;
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
        GearCardBase dinglanyemingzhu = new DinglanyemingzhuCard();
        List<Player> playerList = new ArrayList<>();
        Player playerA = new Player();
        Player playerB = new Player();
        playerA.init(engine, 0, "A", new SHeroGuanyu(), new SHeroGuanyu());
        playerA.acquireEquip(engine, CardTransferContext.equipOn(), Collections.singletonList(dinglanyemingzhu));
        playerB.init(engine, 1, "B", new SHeroGuanyu(), new SHeroGuanyu());
        playerList.add(playerA);
        playerList.add(playerB);
        engine.initPlayers(playerList);
    }

    @Test
    public void test1() throws Exception {
        Player player = engine.getCurrentPlayer();
        Assert.isTrue(player.getHealth() == 5, "health is not correct");
        Assert.isTrue(player.getMaxHealth() == 5, "health is not correct");
        Assert.isTrue(player.allAbilities().size() == 5, "abilities count is not correct");
        new Thread(() -> engine.mainLoop()).start();
        int times = 0;
        while (true) {
            try {
                Thread.sleep(2000);
                if (times++ >= 4) {
                    break;
                }
                engine.endActivePhase();
                player = engine.getCurrentPlayer();
                if (player.getId() == 1) {
                    Assert.isTrue(player.allAbilities().size() == 4, "abilities count is not correct");
                }
            } catch (Exception ignore) {
            }

        }
    }

    @Test
    public void test2() throws Exception {
        Player player = engine.getCurrentPlayer();
        List<CardBase> cards = new ArrayList<>() {{
            add(CardBase.newSimple(SlashCard.class, 1));
            add(CardBase.newSimple(SlashCard.class, 2));
            add(CardBase.newSimple(SlashCard.class, 3));
        }};
        player.acquireHand(engine, CardTransferContext.draw(), cards);
        new Thread(() -> engine.mainLoop()).start();
        ZhihengSkill zhihengSkill = (ZhihengSkill) player.allAbilities().stream()
                .filter(ab -> ab instanceof ZhihengSkill).findFirst().orElse(null);
        Assert.isTrue(zhihengSkill != null, "skill query error");
        ProactiveActionDTO dto = new ProactiveActionDTO();
        dto.setUser(player);
        dto.setHandSelected(cards);
        zhihengSkill.proactiveAction(engine, player, dto);
        Assert.isTrue(zhihengSkill.getStageState().getUseCount() == 1, "skill use status error");
    }
//    @Test
    public void test3() throws Exception {
        Player player = engine.getCurrentPlayer();
        Player playerB = engine.getPlayers().stream().filter(p -> p != player).findFirst().orElse(null);
        Assert.isTrue(playerB != null, "init players error");
        List<CardBase> cards = new ArrayList<>() {{
            add(CardBase.newSimple(SlashCard.class, 1));
        }};
        player.acquireHand(engine, CardTransferContext.draw(), cards);
        new Thread(() -> engine.mainLoop()).start();
        CardUseDTO dto = new CardUseDTO();
        dto.setPlayer(player);
        dto.setCard(cards.get(0));
        dto.getTargets().put(playerB, 1);
        SlashUseSettlement settlement = SlashUseSettlement.newOne(dto);
        engine.runSettlement(settlement);
        Assert.isTrue(playerB.getHealth() == 4, "slash use error");
    }
//    @Test
    public void test4() throws Exception {
        Player player = engine.getCurrentPlayer();
        Player playerB = engine.getPlayers().stream().filter(p -> p != player).findFirst().orElse(null);
        Assert.isTrue(playerB != null, "init players error");
        List<CardBase> cards = new ArrayList<>() {{
            add(CardBase.newSimple(SlashCard.class, 1));
        }};
        player.acquireHand(engine, CardTransferContext.draw(), cards);
        cards = new ArrayList<>() {{
            add(CardBase.newSimple(JinkCard.class, 1));
        }};
        playerB.acquireHand(engine, CardTransferContext.draw(), cards);
        new Thread(() -> engine.mainLoop()).start();
        CardUseDTO dto = new CardUseDTO();
        dto.setPlayer(player);
        dto.setCard(cards.get(0));
        dto.getTargets().put(playerB, 1);
        SlashUseSettlement settlement = SlashUseSettlement.newOne(dto);
        engine.runSettlement(settlement);
        if (playerB.getStageState().getUseCardCount() > 0) {
            Assert.isTrue(playerB.getHealth() == 5, "slash use error");
        } else {
            Assert.isTrue(playerB.getHealth() == 4, "slash use error");
        }
    }
}

