package io.skydeck.gserver.domain.skill;

import io.skydeck.gserver.annotation.AbilityName;
import io.skydeck.gserver.domain.CardBase;
import io.skydeck.gserver.domain.DynamicCard;
import io.skydeck.gserver.domain.Player;
import io.skydeck.gserver.domain.SkillBase;
import io.skydeck.gserver.domain.dto.CardSacrificeDTO;
import io.skydeck.gserver.domain.dto.CardUseDTO;
import io.skydeck.gserver.domain.dto.ProactiveActionDTO;
import io.skydeck.gserver.engine.DynamicCardManager;
import io.skydeck.gserver.engine.GameEngine;
import io.skydeck.gserver.enums.CardNameType;
import io.skydeck.gserver.enums.CardSubType;
import io.skydeck.gserver.enums.Color;
import io.skydeck.gserver.enums.Suit;
import io.skydeck.gserver.impl.CardSacrificeSettlement;
import io.skydeck.gserver.impl.SlashCardUseSettlement;
import org.apache.commons.collections4.CollectionUtils;

import java.util.*;

@AbilityName("WuSheng")
public class WuShengSkill extends SkillBase {
    private Player owner;
    public WuShengSkill(Player owner) {
        this.owner = owner;
    }

    @Override
    public String name() {
        return "WuSheng";
    }


    @Override
    public void proactiveAction(GameEngine engine, Player player, ProactiveActionDTO actionDTO) {
        if (CollectionUtils.isNotEmpty(actionDTO.getJudgeSelected())) {
            return;
        }
        DynamicCardManager dynamicCardManager = engine.getDynamicCardManager();
        List<CardBase> cards = new ArrayList<>();
        cards.addAll(actionDTO.getHandSelected());
        cards.addAll(actionDTO.getEquipSelected());
        if (CollectionUtils.isEmpty(cards) || cards.size() > 1 || cards.get(0).color() != Color.Red) {
            return;
        }
        CardBase card = cards.get(0);
        DynamicCard dCard = dynamicCardManager.convert(Collections.singletonList(card), CardNameType.Slash);
        List<Player> targets = actionDTO.getTargets();
        if (CollectionUtils.isEmpty(targets)) {
            CardSacrificeDTO dto = new CardSacrificeDTO();
            dto.setPlayer(player);
            dto.setCard(dCard);
            actionDTO.setOutput(CardSacrificeSettlement.newOne(dto));
        } else if (targets.size() == 1) {
            CardUseDTO cardUseDTO = new CardUseDTO();
            Player target = targets.get(0);
            if (engine.canSelectAsCardTarget(player, target, dCard)) {
                cardUseDTO.setPlayer(player);
                cardUseDTO.setCard(dCard);
                cardUseDTO.addTarget(target);
                actionDTO.setOutput(SlashCardUseSettlement.newOne(cardUseDTO));
            }
        }
    }

    @Override
    public boolean canSelectAsCardTarget(Player player, Player target, CardBase card) {
        return card.subType() == CardSubType.Slash && card.suit() == Suit.Diamond && target != player;
    }
}
