package io.skydeck.gserver.domain.card.gear;

import io.skydeck.gserver.annotation.CardExecMeta;
import io.skydeck.gserver.domain.card.CardBase;
import io.skydeck.gserver.domain.card.DynamicCard;
import io.skydeck.gserver.domain.card.GearCardBase;
import io.skydeck.gserver.domain.dto.CardSacrificeDTO;
import io.skydeck.gserver.domain.dto.CardUseDTO;
import io.skydeck.gserver.domain.dto.ProactiveActionDTO;
import io.skydeck.gserver.domain.player.Player;
import io.skydeck.gserver.domain.skill.AbilityBase;
import io.skydeck.gserver.engine.GameEngine;
import io.skydeck.gserver.enums.CardNameType;
import io.skydeck.gserver.impl.settlement.CardSacrificeSettlement;
import io.skydeck.gserver.impl.settlement.SlashUseSettlement;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Collections;
import java.util.List;


@CardExecMeta(cardNameType = CardNameType.Zhangbashemao, settlement = "GearCardSettlement")
public class ZhangbashemaoCard extends GearCardBase {
    @Override
    public int attackRange() {
        return 3;
    }
    @Override
    public List<AbilityBase> abilities() {
        return Collections.singletonList(ability);
    }

    private final ZhangbashemaoAbility ability = new ZhangbashemaoAbility();

    private class ZhangbashemaoAbility extends AbilityBase {
        @Override
        public String name() {
            return ZhangbashemaoCard.this.name();
        }

        @Override
        public void proactiveAction(GameEngine engine, Player player, ProactiveActionDTO proactiveActionDTO) {
            List<CardBase> handCards = proactiveActionDTO.getHandSelected();
            if (CollectionUtils.isEmpty(handCards) || handCards.size() != 2) {
                return;
            }
            DynamicCard dCard = engine.getDynamicCardManager().convert(handCards, CardNameType.Slash);
            List<Player> targets = proactiveActionDTO.getTargets();
            if (CollectionUtils.isEmpty(targets)) {
                CardSacrificeDTO dto = new CardSacrificeDTO();
                dto.setPlayer(player);
                dto.setCard(dCard);
                proactiveActionDTO.setOutput(CardSacrificeSettlement.newOne(dto));
            } else if (targets.size() == 1) {
                CardUseDTO cardUseDTO = new CardUseDTO();
                Player target = targets.get(0);
                if (engine.canSelectAsCardTarget(player, target, dCard)) {
                    cardUseDTO.setPlayer(player);
                    cardUseDTO.setCard(dCard);
                    cardUseDTO.addTarget(target);
                    proactiveActionDTO.setOutput(SlashUseSettlement.newOne(cardUseDTO));
                }
            }
        }
    }
}
