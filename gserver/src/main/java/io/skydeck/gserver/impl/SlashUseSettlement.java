package io.skydeck.gserver.impl;

import io.skydeck.gserver.domain.CardSettlement;
import io.skydeck.gserver.domain.Player;
import io.skydeck.gserver.domain.dto.CardUseDTO;
import io.skydeck.gserver.engine.CardFilterFactory;
import io.skydeck.gserver.engine.GameEngine;
import io.skydeck.gserver.engine.QueryManager;
import io.skydeck.gserver.enums.CardLostType;
import io.skydeck.gserver.util.PositionUtil;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.MapUtils;

import java.util.*;

public class SlashUseSettlement extends CardSettlement {
    @Getter
    private CardUseDTO cardUseDTO;
    private Map<Player, Integer> damageCountMap = new HashMap<>();
    private Map<Player, Integer> jinkQueryCountMap = new HashMap<>();

    private Set<Player> jinkSuccessSet = new HashSet<>();

    public static SlashUseSettlement newOne(CardUseDTO useDTO) {
        SlashUseSettlement settlement = new SlashUseSettlement();
        settlement.cardUseDTO =  useDTO;
        return settlement;
    }

    @Setter
    private boolean jinkInvalid = false;

    @Override
    public void resolve(GameEngine engine) {
        QueryManager queryManager = engine.getQueryManager();
        CardFilterFactory cff = engine.getCardFilterFactory();

        Player user = cardUseDTO.getPlayer();
        user.removeCard(engine, Collections.singletonList(cardUseDTO.getCard()), CardLostType.Use);
        engine.onCardLost(user, CardLostType.Use, Collections.singletonList(cardUseDTO.getCard()));
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
                    if (!valid || target.isDead()) {
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
                        JinkUseSettlement jinkSettlement = JinkUseSettlement.newOne(cardUseDTO, jinkUse);
                        jinkSettlement.resolve(engine);
                        validJink++;
                    }
                    if (validJink < jinkCount) {
                        causeDamage(engine, target);
                    } else {
                        jinkSuccessSet.add(target);
                        if (jinkInvalid)  {
                            causeDamage(engine, target);
                        }
                    }
                    engine.onCardEffected(cardUseDTO, this, target);
                }
            }
        }
        cardUseDTO.getPlayer().getStageState().setDrunk(false);
        engine.onCardEffectFinish(cardUseDTO, this);
        engine.onCardUsed(cardUseDTO, this);
    }

    private void causeDamage(GameEngine engine, Player target) {
        int drunkDamage = (cardUseDTO.getPlayer().getStageState().getDrunk()) ? 1 : 0;
        DamageSettlement damageSettlement = DamageSettlement.newOne(cardUseDTO.getPlayer(),
                target, damageCountMap.getOrDefault(target, 1) + drunkDamage,
                cardUseDTO.getCard().nature(), cardUseDTO.getCard());
        damageSettlement.resolve(engine);
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
