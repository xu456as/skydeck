package io.skydeck.gserver.engine;

import io.skydeck.gserver.domain.CardBase;
import io.skydeck.gserver.domain.Player;
import io.skydeck.gserver.domain.StaticHeroBase;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VisibilityManager {
    //TODO
    public void showAllHand(Player player) {

    }
    public void showHandCards(Player player, List<CardBase> cards) {

    }

    public void peekCard(Player watcher, List<CardBase> cards) {

    }

    public void peekHero(Player watcher, StaticHeroBase... heroes) {

    }

}
