package io.skydeck.gserver.impl;

import io.skydeck.gserver.domain.CardFilterIface;
import io.skydeck.gserver.domain.CardSettlement;
import io.skydeck.gserver.domain.Player;
import io.skydeck.gserver.domain.dto.CardUseDTO;
import io.skydeck.gserver.engine.CardFilterFactory;
import io.skydeck.gserver.engine.QueryManager;
import io.skydeck.gserver.engine.SettlementEngine;
import io.skydeck.gserver.util.PositionUtil;
import org.apache.commons.collections4.MapUtils;

import java.util.*;

public class SlashCardUseSettlement extends CardSettlement {
    private CardUseDTO cardUseDTO;
    private Map<Player, Integer> damageCountMap = new HashMap<>();
    private Map<Player, Integer> jinkQueryCountMap = new HashMap<>();

    private Set<Player> jinkSuccessSet = new HashSet<>();
    @Override
    public void resolve(SettlementEngine engine) {
        QueryManager queryManager = engine.getQueryManager();
        CardFilterFactory cff = engine.getCardFilterFactory();
        engine.onCardUsing(cardUseDTO, this);
        engine.onCardTargeting(cardUseDTO, this);
        engine.onCardTargeted(cardUseDTO, this);
        Map<Player, Integer> targetMap = cardUseDTO.getTargets();
        if (!MapUtils.isEmpty(targetMap)) {
            List<Player> sortedList = PositionUtil.positionSort(engine.getCurrentPlayer(), targetMap.keySet());
            for (Player target : sortedList) {
                int effectCnt = targetMap.get(target);
                for (int i = 0; i < effectCnt; i++) {
                    boolean valid = engine.onCardEffecting(cardUseDTO, this, target);
                    if (!valid) {
                        continue;
                    }
                    int jinkCount = jinkQueryCountMap.getOrDefault(target, 1);
                    int validJink = 0;
                    for (int j = 0; j < jinkCount; j++) {
                        CardUseDTO jinkUse = queryManager.cardUseQuery(target,
                                cff.jinkFilter(), cardDisableMap.get(target));
                        if (jinkUse == null) {
                            break;
                        }
                        JinkCardUseSettlement jinkSettlement = JinkCardUseSettlement.newOne(cardUseDTO, jinkUse);
                        jinkSettlement.resolve(engine);
                        validJink++;
                    }
                    if (validJink < jinkCount) {
                        DamageSettlement damageSettlement = DamageSettlement.newOne(cardUseDTO.getPlayer(),
                                target, damageCountMap.getOrDefault(target, 1),
                                cardUseDTO.getCard().nature(), cardUseDTO.getCard());
                        damageSettlement.resolve(engine);
                    } else {
                        jinkSuccessSet.add(target);
                    }
                    engine.onCardEffected(cardUseDTO, this, target);
                }
            }
        }
        engine.onCardEffectFinish(cardUseDTO, this);
        engine.onCardUsed(cardUseDTO, this);
        engine.onCardBurying(cardUseDTO, this);
        engine.onCardUsed(cardUseDTO, this);
    }

    public boolean isJinkSuccess(Player target) {
        return jinkSuccessSet.contains(target);
    }
    public void addJink(Player target, int count) {
        if (jinkQueryCountMap.containsKey(target)) {
            jinkQueryCountMap.put(target, jinkQueryCountMap.get(target) + count);
        } else {
            jinkQueryCountMap.put(target, count);
        }
    }
    public void addDamage(Player target, int count) {
        if (damageCountMap.containsKey(target)) {
            damageCountMap.put(target, damageCountMap.get(target) + count);
        } else {
            damageCountMap.put(target, count);
        }
    }
}
