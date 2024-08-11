package io.skydeck.gserver.impl.settlement;

import io.skydeck.gserver.domain.card.CardBase;
import io.skydeck.gserver.domain.card.GearCardBase;
import io.skydeck.gserver.domain.player.Player;
import io.skydeck.gserver.domain.settlement.PloyCardSettlement;
import io.skydeck.gserver.domain.dto.CardTransferContext;
import io.skydeck.gserver.domain.dto.CardUseDTO;
import io.skydeck.gserver.domain.dto.StealWeaponExtraInfo;
import io.skydeck.gserver.engine.CardFilterFactory;
import io.skydeck.gserver.engine.GameEngine;
import io.skydeck.gserver.engine.QueryManager;
import io.skydeck.gserver.enums.CardAcquireWay;
import io.skydeck.gserver.enums.CardLostType;
import io.skydeck.gserver.enums.CardSubType;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class StealWeaponUseSettlement extends PloyCardSettlement {
    private StealWeaponExtraInfo info;
    public static StealWeaponUseSettlement newOne(CardUseDTO useDTO) {
        StealWeaponUseSettlement settlement = new StealWeaponUseSettlement();
        settlement.useDTO = useDTO;
        settlement.info = (StealWeaponExtraInfo) useDTO.getExtraInfo();
        return settlement;
    }
    @Override
    public void resolve(GameEngine e) {
        commonResolve(e, this::bizResolve);
    }
    private void bizResolve(GameEngine e, Player target) {
        if (target.isDead()) {
            return;
        }
        GearCardBase weapon = target.getEquips().stream()
                .filter(gear -> gear.subType() == CardSubType.Weapon)
                .findFirst()
                .orElse(null);
        if (weapon == null) {
            return;
        }
        Map<Player, Player> map = info.getSlashTargetMap();
        Player slashTarget = map.getOrDefault(target, null);
        if (slashTarget == null || slashTarget.isDead()) {
            return;
        }
        QueryManager queryManager = e.getQueryManager();
        CardFilterFactory cff = e.getCardFilterFactory();
        CardUseDTO cardUseDTO = queryManager.cardUseQuery(target, cff.slashFilter(), null);
        if (cardUseDTO != null) {
            SlashUseSettlement settlement = SlashUseSettlement.newOne(cardUseDTO);
            e.runSettlement(settlement);
        } else {
            List<CardBase> cards = Collections.singletonList(weapon);
            target.removeCard(e, cards, CardLostType.Stolen);
            useDTO.getPlayer().acquireHand(e,
                    CardTransferContext.builder().acquireWay(CardAcquireWay.Steal).source(target).build(),
                    cards);
        }

    }
}
