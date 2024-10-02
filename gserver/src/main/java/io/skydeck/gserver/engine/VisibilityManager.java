package io.skydeck.gserver.engine;

import io.skydeck.gserver.domain.card.CardBase;
import io.skydeck.gserver.domain.player.Player;
import io.skydeck.gserver.domain.hero.StaticHeroBase;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Scope("prototype")
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
