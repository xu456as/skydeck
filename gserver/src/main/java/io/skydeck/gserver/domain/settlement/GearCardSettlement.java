package io.skydeck.gserver.domain.settlement;

import io.skydeck.gserver.domain.card.GearCardBase;
import io.skydeck.gserver.domain.dto.CardTransferContext;
import io.skydeck.gserver.domain.player.Player;
import io.skydeck.gserver.engine.GameEngine;
import io.skydeck.gserver.enums.CardLostType;
import io.skydeck.gserver.enums.GearEquipType;

import java.util.Collections;
import java.util.List;

public class GearCardSettlement extends CardSettlement {
    @Override
    public void resolve(GameEngine e) {
        Player user = useDTO.getPlayer();
        GearCardBase card = (GearCardBase) useDTO.getCard();
        user.removeCard(e, Collections.singletonList(card), CardLostType.Use);
        e.onCardLost(user, CardLostType.Use, Collections.singletonList(card));
        e.onCardUsing(useDTO, this);
        e.onOCardTargeting(useDTO, this);
        e.onDCardTargeting(useDTO, this);
        e.onOCardTargeted(useDTO, this);
        e.onDCardTargeted(useDTO, this);
        List<GearCardBase> equips = user.getEquips();
        GearCardBase toReplace = equips.stream()
                .filter(item -> item.subType() == card.subType())
                .findFirst()
                .orElse(null);
        if (toReplace != null) {
            user.removeCard(e, Collections.singletonList(toReplace), CardLostType.EquipReplaced);
            e.onCardLost(user, CardLostType.EquipReplaced, Collections.singletonList(toReplace));
            e.getPcrManager().addToGrave(e, Collections.singletonList(toReplace));
        }
        e.onCardEquipping(user, GearEquipType.Use, Collections.singletonList(card));
        user.acquireEquip(e, CardTransferContext.equipOn(), Collections.singletonList(card));
        e.onCardEquipped(user, GearEquipType.Use, Collections.singletonList(card));
        e.onCardUsed(useDTO, this);
    }
}
