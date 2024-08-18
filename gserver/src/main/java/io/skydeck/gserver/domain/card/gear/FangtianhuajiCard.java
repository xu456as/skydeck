package io.skydeck.gserver.domain.card.gear;

import io.skydeck.gserver.annotation.CardExecMeta;
import io.skydeck.gserver.domain.card.CardBase;
import io.skydeck.gserver.domain.card.GearCardBase;
import io.skydeck.gserver.domain.dto.ActiveCheckDTO;
import io.skydeck.gserver.domain.player.Player;
import io.skydeck.gserver.domain.settlement.CardSettlement;
import io.skydeck.gserver.domain.settlement.SettlementBase;
import io.skydeck.gserver.domain.skill.AbilityBase;
import io.skydeck.gserver.engine.GameEngine;
import io.skydeck.gserver.engine.QueryManager;
import io.skydeck.gserver.enums.CardNameType;
import io.skydeck.gserver.enums.CardUseEvent;
import io.skydeck.gserver.enums.Kingdom;
import io.skydeck.gserver.impl.settlement.SlashUseSettlement;
import io.skydeck.gserver.util.PositionUtil;

import java.util.*;
import java.util.stream.Collectors;

@CardExecMeta(cardNameType = CardNameType.Fangtianhuaji, settlement = "GearCardSettlement")
public class FangtianhuajiCard extends GearCardBase {
    @Override
    public int attackRange() {
        return 4;
    }

    @Override
    public List<AbilityBase> abilities() {
        return Collections.singletonList(ability);
    }

    private final FangtianhuajiAbility ability = new FangtianhuajiAbility();

    private class FangtianhuajiAbility extends AbilityBase {
        @Override
        public String name() {
            return FangtianhuajiAbility.this.name();
        }
        @Override
        public Set<Enum> events() {
            return Collections.singleton(CardUseEvent.Using);
        }
        @Override
        public boolean canActive(GameEngine engine, Enum event, ActiveCheckDTO activeCheck) {
            return activeOnUsing(engine, event, activeCheck.getSubject());
        }
        private boolean activeOnUsing(GameEngine engine, Enum event, Player player) {
            SettlementBase currentSettlement = engine.currentSettlement();
            if (!(currentSettlement instanceof SlashUseSettlement slashUseSettlement)) {
                return false;
            }
            if (slashUseSettlement.getUseDTO().getPlayer() == player) {
                return false;
            }
            return event == CardUseEvent.Using && player.getEquips().contains(FangtianhuajiCard.this);
        }

        @Override
        public void onCardUsing(GameEngine e, Player offender, CardSettlement settlement) {
            if (!(settlement instanceof SlashUseSettlement slashSettlement)) {
                return;
            }
            CardBase card = slashSettlement.getUseDTO().getCard();
            QueryManager qm = e.getQueryManager();
            List<Player> options = new ArrayList<>(e.getPlayers().size());
            List<Player> players = PositionUtil.positionSort(e.getCurrentPlayer(), e.getPlayers());
            for (Player player : players) {
                if (player.isDead()) {
                    continue;
                }
                if (slashSettlement.getUseDTO().getTargets().containsKey(player)) {
                    continue;
                }
                if (!e.canSelectAsCardTarget(settlement.getUseDTO().getPlayer(), player, card)) {
                    continue;
                }
                options.add(player);
            }
            List<Player> extraTargets = qm.playerTargetsQuery(settlement.getUseDTO().getPlayer(), options);
            Set<Kingdom> kingdomSet = slashSettlement.getUseDTO().getTargets().keySet().stream()
                    .map(Player::getKingdom)
                    .collect(Collectors.toCollection(HashSet::new));
            for (Player target : extraTargets) {
                if (target.getKingdom() == Kingdom.Unknown || target.getKingdom() == Kingdom.Am) {
                    slashSettlement.getUseDTO().getTargets().put(target, 1);
                    continue;
                }
                if (kingdomSet.contains(target.getKingdom())) {
                    continue;
                }
                slashSettlement.getUseDTO().getTargets().put(target, 1);
                kingdomSet.add(target.getKingdom());
            }
        }
    }
}
