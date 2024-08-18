package io.skydeck.gserver.domain.card.gear;

import io.skydeck.gserver.annotation.CardExecMeta;
import io.skydeck.gserver.domain.card.GearCardBase;
import io.skydeck.gserver.domain.dto.ActiveCheckDTO;
import io.skydeck.gserver.domain.dto.CardDiscardDTO;
import io.skydeck.gserver.domain.player.Player;
import io.skydeck.gserver.domain.settlement.SettlementBase;
import io.skydeck.gserver.domain.skill.AbilityBase;
import io.skydeck.gserver.engine.GameEngine;
import io.skydeck.gserver.engine.QueryManager;
import io.skydeck.gserver.enums.CardNameType;
import io.skydeck.gserver.enums.CardSubType;
import io.skydeck.gserver.enums.DamageEvent;
import io.skydeck.gserver.enums.DamageNature;
import io.skydeck.gserver.impl.settlement.CardDiscardSettlement;
import io.skydeck.gserver.impl.settlement.DamageSettlement;
import io.skydeck.gserver.util.PositionUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;


@CardExecMeta(cardNameType = CardNameType.Sanjianliangrendao, settlement = "GearCardSettlement")
public class SanjianliangrendaoCard extends GearCardBase {
    @Override
    public int attackRange() {
        return 3;
    }
    @Override
    public List<AbilityBase> abilities() {
        return Collections.singletonList(ability);
    }

    private final SanjianliangrendaoAbility ability = new SanjianliangrendaoAbility();

    private class SanjianliangrendaoAbility extends AbilityBase {
        @Override
        public String name() {
            return SanjianliangrendaoCard.this.name();
        }
        @Override
        public Set<Enum> events() {
            return Collections.singleton(DamageEvent.ODamaged);
        }

        @Override
        public boolean canActive(GameEngine engine, Enum event, ActiveCheckDTO activeCheck) {
            if (events().contains(event)) {
                return false;
            }
            if (activeCheck.getSubject() != owner) {
                return false;
            }
            SettlementBase se = engine.currentSettlement();
            if (!(se instanceof DamageSettlement dSe)) {
                return false;
            }
            if (dSe.getCard() == null || dSe.getCard().subType() != CardSubType.Slash) {
                return false;
            }
            return !dSe.getSufferer().isDead();
        }

        @Override
        public void onODamaged(GameEngine engine, DamageSettlement settlement) {
            Player suffer = settlement.getSufferer();
            if (suffer.isDead()) {
                return;
            }
            if (settlement.getDealer().getHands().isEmpty()) {
                return;
            }
            List<Player> damageOptionList = new ArrayList<>(engine.getPlayers().size());
            List<Player> players = PositionUtil.positionSort(engine.getCurrentPlayer(), engine.getPlayers());
            for (Player player : players) {
                if (player.isDead()) {
                    continue;
                }
                if (player == suffer) {
                    continue;
                }
                if (engine.distance(suffer, player) > 1) {
                    continue;
                }
                damageOptionList.add(player);
            }
            if (damageOptionList.isEmpty()) {
                return;
            }
            QueryManager qm = engine.getQueryManager();
            Player target = qm.playerTargetQuery(settlement.getDealer(), damageOptionList);
            if (target == null) {
                return;
            }
            CardDiscardDTO dto = qm.cardDiscardQuery(settlement.getDealer(), 1, QueryManager.AREA_HAND);
            if (dto == null) {
                return;
            }
            engine.runSettlement(CardDiscardSettlement.newOne(dto));
            engine.runSettlement(DamageSettlement.newOne(settlement.getDealer(), target, 1, DamageNature.Normal, null));
        }
    }
}
