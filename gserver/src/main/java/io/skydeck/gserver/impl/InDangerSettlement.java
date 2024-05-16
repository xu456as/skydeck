package io.skydeck.gserver.impl;

import io.skydeck.gserver.domain.Player;
import io.skydeck.gserver.domain.SettlementBase;
import io.skydeck.gserver.domain.dto.CardUseDTO;
import io.skydeck.gserver.engine.CardFilterFactory;
import io.skydeck.gserver.engine.QueryManager;
import io.skydeck.gserver.engine.GameEngine;
import io.skydeck.gserver.enums.CardSubType;
import io.skydeck.gserver.util.PositionUtil;

import java.util.Collections;
import java.util.List;

public class InDangerSettlement extends SettlementBase {
    private Player player;
    private Player dealer;

    public static InDangerSettlement newOne(Player player, Player dealer) {
        InDangerSettlement settlement = new InDangerSettlement();
        settlement.player = player;
        settlement.dealer = dealer;
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
                    CureCardUseSettlement settlement = CureCardUseSettlement.newOne(cardUseDTO);
                    settlement.resolve(engine);
                } else if (cardUseDTO.getCard().subType() == CardSubType.Liquor) {
                    LiquorCardUseSettlement settlement = LiquorCardUseSettlement.newOne(cardUseDTO);
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
            DyingSettlement dyingSettlement = DyingSettlement.newOne(player, dealer);
            dyingSettlement.resolve(engine);
        }

    }
}
