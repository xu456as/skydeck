package io.skydeck.gserver.engine;

import io.skydeck.gserver.domain.CardFilterIface;
import io.skydeck.gserver.domain.Player;
import io.skydeck.gserver.domain.dto.CardDiscardDTO;
import io.skydeck.gserver.domain.dto.CardSacrificeDTO;
import io.skydeck.gserver.domain.dto.CardUseDTO;

import java.util.List;

public class QueryManager {
    //TODO
    public CardUseDTO cardUseQuery(Player player, CardFilterIface allow, List<CardFilterIface> deny) {return null;}
    public CardSacrificeDTO cardSacrificeQuery(Player player, CardFilterIface allow, List<CardFilterIface> deny) {return null;}
    public CardDiscardDTO cardDiscardQuery(Player player, CardFilterIface allow, List<CardFilterIface> deny) {return null;}
}
