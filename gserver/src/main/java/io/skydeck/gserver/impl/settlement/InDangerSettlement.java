package io.skydeck.gserver.impl.settlement;

import io.skydeck.gserver.domain.player.Player;
import io.skydeck.gserver.domain.settlement.SettlementBase;
import io.skydeck.gserver.domain.dto.CardUseDTO;
import io.skydeck.gserver.engine.CardFilterFactory;
import io.skydeck.gserver.engine.QueryManager;
import io.skydeck.gserver.engine.GameEngine;
import io.skydeck.gserver.enums.CardSubType;
import io.skydeck.gserver.util.PositionUtil;
import lombok.Getter;

import java.util.Collections;
import java.util.List;

public class InDangerSettlement extends SettlementBase {
    @Getter
    private Player player;
    @Getter
    private Player dealer;
    @Getter
    private DamageSettlement damageSettlement;

    public static InDangerSettlement newOne(Player player, Player dealer, DamageSettlement damageSettlement) {
        InDangerSettlement settlement = new InDangerSettlement();
        settlement.player = player;
        settlement.dealer = dealer;
        settlement.damageSettlement = damageSettlement;
        return settlement;
    }

    @Override
    public void resolve(GameEngine engine) {
        if (player.isInDanger()) {
            return;
        }
        player.setInDanger(true);
        QueryManager queryManager = engine.getQueryManager();
        CardFilterFactory cff = engine.getCardFilterFactory();
        engine.onDanger(this);
        List<Player> sortedPlayers = PositionUtil.positionSort(engine.getCurrentPlayer(), engine.getPlayers())
                .stream()
                .filter(person -> !person.isDead() && !person.getStageState().getInLimbo())
                .toList();
        for (Player person : sortedPlayers) {
            CardUseDTO cardUseDTO = null;
            if (person.getId() == player.getId()) {
                cardUseDTO = queryManager.cardUseQuery(person, cff.or(cff.cureFilter(), cff.liquorFilter()), Collections.emptyList());
            } else {
                cardUseDTO = queryManager.cardUseQuery(person, cff.cureFilter(), Collections.emptyList());
            }
            if (cardUseDTO != null) {
                if (cardUseDTO.getCard().subType() == CardSubType.Cure) {
                    CureUseSettlement settlement = CureUseSettlement.newOne(cardUseDTO);
                    settlement.resolve(engine);
                } else if (cardUseDTO.getCard().subType() == CardSubType.Liquor) {
                    LiquorUseSettlement settlement = LiquorUseSettlement.newOne(cardUseDTO);
                    settlement.resolve(engine);
                }
            }
            if (player.getHealth() >= 1) {
                break;
            }
        }
        if (player.getHealth() >= 1) {
            player.setInDanger(false);
            engine.onRecover(this);
        } else {
            DeceaseSettlement dyingSettlement = DeceaseSettlement.newOne(player, dealer);
            dyingSettlement.resolve(engine);
        }

    }
}
