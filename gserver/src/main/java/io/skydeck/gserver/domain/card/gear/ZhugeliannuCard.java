package io.skydeck.gserver.domain.card.gear;

import io.skydeck.gserver.annotation.CardExecMeta;
import io.skydeck.gserver.domain.card.GearCardBase;
import io.skydeck.gserver.domain.skill.AbilityBase;
import io.skydeck.gserver.engine.GameEngine;
import io.skydeck.gserver.enums.CardNameType;

import java.util.Collections;
import java.util.List;

@CardExecMeta(cardNameType = CardNameType.Zhugeliannu, settlement = "GearCardSettlement")
public class ZhugeliannuCard extends GearCardBase {
    @Override
    public int attackRange() {
        return 1;
    }
    @Override
    public List<AbilityBase> abilities() {
        return Collections.singletonList(ability);
    }

    private final ZhugeliannuAbility ability = new ZhugeliannuAbility();

    private class ZhugeliannuAbility extends AbilityBase {


        @Override
        public String name() {
            return ZhugeliannuCard.this.name();
        }

        @Override
        public int slashQuotaMod(GameEngine e, int original) {
            return original + 999;
        }
    }
}
