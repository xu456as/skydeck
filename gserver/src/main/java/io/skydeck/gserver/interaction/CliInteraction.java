package io.skydeck.gserver.interaction;

import io.skydeck.gserver.domain.card.CardBase;
import io.skydeck.gserver.domain.player.Player;
import jakarta.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

@Component
public class CliInteraction {
    public int takeCardsInput(List<CardBase> cards) {
        if (CollectionUtils.isEmpty(cards)) {
            return -1;
        }
        System.out.println("you have cards:" + presentCards(cards));
        System.out.println("please select your input:");
        Scanner in = new Scanner(System.in);
        return in.nextInt();
    }

    public List<Integer> takeCardInput(List<CardBase> cards) {
        return Collections.emptyList();
    }

    public List<Integer> takePlayerInput(List<Player> players) {
        return Collections.emptyList();
    }

    private String presentCards(List<CardBase> cards) {
        return cards.stream()
                .map(CardBase::nameType)
                .map(Enum::name)
                .collect(Collectors.joining(",", "[", "]"));
    }
}
