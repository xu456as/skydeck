package io.skydeck.gserver.domain.skill;

import io.skydeck.gserver.domain.SkillBase;
import io.skydeck.gserver.engine.GameEngine;
import io.skydeck.gserver.engine.QueryManager;
import io.skydeck.gserver.enums.DamageEvent;
import io.skydeck.gserver.impl.DamageSettlement;

import java.util.Collections;
import java.util.Set;

public class JianXiongSkill extends SkillBase {
    @Override
    public Set<Enum> events() {
        return Collections.singleton(DamageEvent.DDamaged);
    }
    @Override
    public String queryName() {
        return "JianXiong";
    }

    @Override
    public void onDDamaged(GameEngine engine, DamageSettlement settlement) {
        QueryManager queryManager = engine.getQueryManager();
        //todo
        int index = queryManager.abilityOptionQuery(settlement.getSufferer(), this, "1", "2");
        switch (index) {
            case 1:
                //draw
            case 2:
                //gain
        }

    }
}
