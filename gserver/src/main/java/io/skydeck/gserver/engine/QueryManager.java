package io.skydeck.gserver.engine;

import io.skydeck.gserver.domain.AbilityBase;
import io.skydeck.gserver.domain.CardFilterIface;
import io.skydeck.gserver.domain.Player;
import io.skydeck.gserver.domain.dto.CardDiscardDTO;
import io.skydeck.gserver.domain.dto.CardSacrificeDTO;
import io.skydeck.gserver.domain.dto.CardUseDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class QueryManager {
    //TODO
    public CardUseDTO cardUseQuery(Player player, CardFilterIface allow, List<CardFilterIface> deny) {return null;}
    public CardSacrificeDTO cardSacrificeQuery(Player player, CardFilterIface allow, List<CardFilterIface> deny) {return null;}
    public CardDiscardDTO cardDiscardQuery(Player player, int count, CardFilterIface allow, List<CardFilterIface> deny) {return null;}

    public AbilityBase abilitiesQuery(Player player, List<AbilityBase> abilityList) {
        //TODO
        return null;
    }
    public int abilityOptionQuery(Player player, AbilityBase ability, String... options) {
        //TODO
        return -1;
    }

    public int abilityOptionQuery(Player player, AbilityBase ability, List<String> options) {
        //TODO
        return -1;
    }

}
