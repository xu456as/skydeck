package io.skydeck.gserver.domain.skill;

import io.skydeck.gserver.annotation.AbilityName;
import io.skydeck.gserver.annotation.I18n;
import io.skydeck.gserver.domain.card.CardBase;
import io.skydeck.gserver.domain.player.Player;
import io.skydeck.gserver.domain.dto.CardDiscardDTO;
import io.skydeck.gserver.domain.dto.ProactiveActionDTO;
import io.skydeck.gserver.engine.GameEngine;
import io.skydeck.gserver.engine.QueryManager;
import io.skydeck.gserver.enums.AbilityTag;
import io.skydeck.gserver.enums.DamageNature;
import io.skydeck.gserver.i18n.TextDictionary;
import io.skydeck.gserver.impl.settlement.CardDiscardSettlement;
import io.skydeck.gserver.impl.settlement.DamageSettlement;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@AbilityName("HuWei")
@I18n(value = "HuWei")
public class HuWeiSkill extends SkillBase {



    private Player owner;
    public HuWeiSkill(Player owner) {
        this.owner = owner;
    }
    @Override
    public Set<AbilityTag> tags() {
        return Collections.singleton(AbilityTag.OneOff);
    }

    @Override
    public void proactiveAction(GameEngine engine, Player player, ProactiveActionDTO proactiveActionDTO) {
        List<Player> targets = proactiveActionDTO.getTargets();
        if (CollectionUtils.isEmpty(targets) || targets.size() > 1) {
            return;
        }
        Player target = targets.get(0);
        if (target == player || target.getEquips().isEmpty()) {
            return;
        }
        QueryManager queryManager = engine.getQueryManager();
        int index = queryManager.abilityOptionQuery(target, this, TextDictionary.HuWeiOpt0, TextDictionary.HuWeiOpt1);
        switch (index) {
            case 0:
                owner.getStageState().setSlashQuota(owner.getStageState().getSlashQuota() + 1);
                engine.runSettlement(CardDiscardSettlement.newOne(CardDiscardDTO.builder()
                        .offender(target).defender(target).card(target.getEquips().stream().map(e->(CardBase)e).toList()).build())
                );
                break;
            case 1:
                engine.runSettlement(
                        DamageSettlement.newOne(player, target, 1, DamageNature.Normal, null, engine.currentSettlement())
                );
                break;
        }
    }
}
