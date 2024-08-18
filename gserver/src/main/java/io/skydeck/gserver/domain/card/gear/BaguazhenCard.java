package io.skydeck.gserver.domain.card.gear;

import io.skydeck.gserver.annotation.CardExecMeta;
import io.skydeck.gserver.domain.card.CardBase;
import io.skydeck.gserver.domain.card.GearCardBase;
import io.skydeck.gserver.domain.dto.ActiveCheckDTO;
import io.skydeck.gserver.domain.dto.CardSacrificeDTO;
import io.skydeck.gserver.domain.dto.CardUseDTO;
import io.skydeck.gserver.domain.dto.ProactiveActionDTO;
import io.skydeck.gserver.domain.player.Player;
import io.skydeck.gserver.domain.skill.AbilityBase;
import io.skydeck.gserver.engine.DynamicCardManager;
import io.skydeck.gserver.engine.GameEngine;
import io.skydeck.gserver.enums.*;
import io.skydeck.gserver.impl.settlement.CardJudgeSettlement;
import io.skydeck.gserver.impl.settlement.CardSacrificeSettlement;
import io.skydeck.gserver.impl.settlement.JinkUseSettlement;
import io.skydeck.gserver.impl.settlement.SlashUseSettlement;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

@CardExecMeta(cardNameType = CardNameType.Baguazhen, settlement = "GearCardSettlement")
public class BaguazhenCard extends GearCardBase {
    @Override
    public List<AbilityBase> abilities() {
        return Collections.singletonList(ability);
    }

    private final BaguazhenAbility ability = new BaguazhenAbility();

    private class BaguazhenAbility extends AbilityBase {
        @Override
        public String name() {
            return BaguazhenCard.this.name();
        }
        @Override
        public Set<Enum> events() {
            return Collections.singleton(CardQueryEvent.Querying);
        }
        @Override
        public boolean canActive(GameEngine engine, Enum event, ActiveCheckDTO activeCheck) {
            if (events().contains(event) && activeCheck.getSubject() == owner && activeCheck.getCardQuery() == CardNameType.Jink &&
                    (activeCheck.getCardDisposeType() == CardDisposeType.Use || activeCheck.getCardDisposeType() == CardDisposeType.Sacrifice)) {
                    if ((engine.currentSettlement() instanceof SlashUseSettlement slashSet)) {
                        return !slashSet.getIgnoreArmorSet().contains(owner);
                    } else {
                        return true;
                    }
            }
            return false;
        }


        @Override
        public void proactiveAction(GameEngine engine, Player player, ProactiveActionDTO proactiveActionDTO) {
            CardJudgeSettlement cjSettle = CardJudgeSettlement.newOne(player, engine.currentSettlement(), new Action(engine, owner, proactiveActionDTO));
            engine.runSettlement(cjSettle);

        }

        private record Action(GameEngine e, Player owner, ProactiveActionDTO proactiveActionDTO) implements Consumer<CardBase> {

            @Override
            public void accept(CardBase card) {
                Suit suit = owner.cardSuitMod(card);
                if (suit.color() == Color.Red) {
                    DynamicCardManager dcm = e.getDynamicCardManager();
                    if (proactiveActionDTO.getActiveCheck().getCardDisposeType() == CardDisposeType.Use) {
                        CardUseDTO dto = new CardUseDTO();
                        dto.setCard(dcm.imitate(CardNameType.Jink));
                        dto.setPlayer(owner);
                        dto.setCounterSettlement(e.currentSettlement());
                        proactiveActionDTO.setOutput(JinkUseSettlement.newOne(dto));
                    } else if (proactiveActionDTO.getActiveCheck().getCardDisposeType() == CardDisposeType.Sacrifice){
                        proactiveActionDTO.setOutput(CardSacrificeSettlement.newOne(CardSacrificeDTO.builder()
                                .player(owner)
                                .card(dcm.imitate(CardNameType.Jink))
                                .build()
                        ));
                    }
                }
            }
        }
    }
}
