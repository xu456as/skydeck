package io.skydeck.gserver.domain.skill;

import io.skydeck.gserver.annotation.AbilityName;
import io.skydeck.gserver.domain.card.CardBase;
import io.skydeck.gserver.domain.dto.CardDiscardDTO;
import io.skydeck.gserver.domain.dto.CardTransferContext;
import io.skydeck.gserver.domain.dto.ProactiveActionDTO;
import io.skydeck.gserver.domain.player.Player;
import io.skydeck.gserver.engine.GameEngine;
import io.skydeck.gserver.engine.PublicCardResManager;
import io.skydeck.gserver.impl.settlement.CardDiscardSettlement;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@AbilityName("Zhiheng")
@Slf4j
public class ZhihengSkill extends SkillBase {
    @Getter
    @Setter
    private boolean limitCheck = true;
    public ZhihengSkill(Player owner) {
        this.owner = owner;
    }

    @Override
    public String name() {
        return "Zhiheng";
    }

    @Override
    public void proactiveAction(GameEngine engine, Player player, ProactiveActionDTO actionDTO) {
        if (player.getHands().isEmpty() && player.getEquips().isEmpty()) {
            return;
        }
        if (stageState.getUseCount() >= 1) {
            return;
        }
        List<CardBase> toDiscardCard = new ArrayList<>(Optional.ofNullable(actionDTO.getHandSelected()).orElse(Collections.emptyList()));
        toDiscardCard.addAll(Optional.ofNullable(actionDTO.getEquipSelected()).orElse(Collections.emptyList()));
        if (limitCheck && toDiscardCard.size() > player.getMaxHealth()) {
            return;
        }
        CardDiscardSettlement discardSettlement = CardDiscardSettlement.newOne(
                CardDiscardDTO.builder()
                .offender(player)
                .defender(player)
                .card(toDiscardCard).build()
        );
        engine.runSettlement(discardSettlement);
        PublicCardResManager pcr = engine.getPcrManager();
        List<CardBase> cards = pcr.pollDeckTop(toDiscardCard.size());
        player.acquireHand(engine, CardTransferContext.draw(), cards);
        try {
            stageState.incCount("useCount");
        } catch (Exception e) {
            log.error("can't incCount for useCount", e);
        }
    }
}
