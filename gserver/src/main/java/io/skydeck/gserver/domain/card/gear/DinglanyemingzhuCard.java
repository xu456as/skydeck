package io.skydeck.gserver.domain.card.gear;

import io.skydeck.gserver.annotation.CardExecMeta;
import io.skydeck.gserver.domain.card.GearCardBase;
import io.skydeck.gserver.domain.player.Player;
import io.skydeck.gserver.domain.skill.AbilityBase;
import io.skydeck.gserver.domain.skill.ZhihengSkill;
import io.skydeck.gserver.engine.GameEngine;
import io.skydeck.gserver.enums.CardNameType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@CardExecMeta(cardNameType = CardNameType.Dinglanyemingzhu, settlement = "GearCardSettlement")
public class DinglanyemingzhuCard extends GearCardBase {
    private ZhihengSkill zhihengSkill = new ZhihengSkill(null);
    @Override
    public List<AbilityBase> abilities() {
        if (owner == null) {
            return Collections.emptyList();
        }
        List<AbilityBase> playAbilities = new ArrayList<>(owner.getAbilities());
        playAbilities.addAll(owner.getSkills());
        if ((owner.getHeroMask() & Player.HERO_MASK_PRIMARY) != 0) {
            playAbilities.addAll(owner.getPrimaryHero().skills());
        }
        if ((owner.getHeroMask() & Player.HERO_MAST_VICE) != 0) {
            playAbilities.addAll(owner.getViceHero().skills());
        }
        ZhihengSkill zhiheng = (ZhihengSkill) playAbilities.stream().filter(ab -> ab instanceof ZhihengSkill).findFirst().orElse(null);
        if (zhiheng != null) {
            zhiheng.setLimitCheck(false);
            return Collections.emptyList();
        } else {
            return Collections.singletonList(zhihengSkill);
        }
    }

    @Override
    public void onEnteredEquipArea(GameEngine engine, Player player) {
        this.zhihengSkill = new ZhihengSkill(owner);
    }

    @Override
    public void onLeavedEquipArea(GameEngine engine, Player player) {
        this.zhihengSkill = null;
    }
}
