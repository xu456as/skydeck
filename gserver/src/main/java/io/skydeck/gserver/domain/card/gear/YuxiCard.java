package io.skydeck.gserver.domain.card.gear;

import io.skydeck.gserver.annotation.CardExecMeta;
import io.skydeck.gserver.domain.card.GearCardBase;
import io.skydeck.gserver.domain.dto.ActiveCheckDTO;
import io.skydeck.gserver.domain.dto.CardUseDTO;
import io.skydeck.gserver.domain.player.Player;
import io.skydeck.gserver.domain.skill.AbilityBase;
import io.skydeck.gserver.engine.DynamicCardManager;
import io.skydeck.gserver.engine.GameEngine;
import io.skydeck.gserver.engine.QueryManager;
import io.skydeck.gserver.enums.CardNameType;
import io.skydeck.gserver.enums.Kingdom;
import io.skydeck.gserver.enums.PhaseEvent;
import io.skydeck.gserver.impl.settlement.GlanceUseSettlement;
import io.skydeck.gserver.util.PositionUtil;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@CardExecMeta(cardNameType = CardNameType.Yuxi, settlement = "GearCardSettlement")
public class YuxiCard extends GearCardBase {
    @Override
    public boolean unique() {
        return true;
    }

    private final YuxiAbility ability = new YuxiAbility();

    private class YuxiAbility extends AbilityBase {
        @Override
        public String name() {
            return YuxiCard.this.name();
        }

        @Override
        public boolean mandatory() {
            return true;
        }

        @Override
        public Set<Enum> events() {
            return Collections.singleton(PhaseEvent.EnteringActivePhase);
        }

        @Override
        public boolean canActive(GameEngine e, Enum event, ActiveCheckDTO activeCheck) {
            return events().contains(event) && owner.getKingdom() != Kingdom.Unknown;
        }

        @Override
        public void onEnteringActivePhase(GameEngine e, Player subject) {
            if (owner.getKingdom() == Kingdom.Unknown) {
                return;
            }
            QueryManager qm = e.getQueryManager();
            DynamicCardManager dcm = e.getDynamicCardManager();
            List<Player> playerList = e.getPlayers();
            playerList = playerList.stream()
                    .filter(p -> !p.isDead())
                    .filter(p -> !p.getStageState().getInLimbo())
                    .filter(p -> p != owner)
                    .toList();
            playerList = PositionUtil.positionSort(e.getCurrentPlayer(), playerList);
            Player target = qm.playerTargetQuery(owner, playerList);
            if (target == null) {
                return;
            }
            CardUseDTO dto = new CardUseDTO();
            dto.setCard(dcm.imitate(CardNameType.GlancePloy));
            dto.setPlayer(owner);
            dto.getTargets().put(target, 1);
            GlanceUseSettlement settlement = GlanceUseSettlement.newOne(dto);
            e.runSettlement(settlement);
        }

        @Override
        public int drawQuotaMod(GameEngine e, int original) {
            if (owner.getKingdom() != Kingdom.Unknown) {
                return original + 1;
            }
            return original;
        }
    }
}
