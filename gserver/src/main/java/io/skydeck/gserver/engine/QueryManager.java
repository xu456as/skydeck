package io.skydeck.gserver.engine;

import io.skydeck.gserver.domain.skill.AbilityBase;
import io.skydeck.gserver.domain.card.CardBase;
import io.skydeck.gserver.domain.card.CardFilterIface;
import io.skydeck.gserver.domain.player.Player;
import io.skydeck.gserver.domain.dto.CardDiscardDTO;
import io.skydeck.gserver.domain.dto.CardSacrificeDTO;
import io.skydeck.gserver.domain.dto.CardUseDTO;
import io.skydeck.gserver.i18n.TextDictionary;
import io.skydeck.gserver.interaction.CliInteraction;
import io.skydeck.gserver.util.JsonUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

@Component
@Slf4j
public class QueryManager {


    private GameEngine engine;

    private LinkedBlockingQueue<String> inputQueue = new LinkedBlockingQueue();

    @Resource
    private CliInteraction cliInteraction;

    public QueryManager(@Lazy GameEngine engine) {
        this.engine = engine;
    }

    public static final int AREA_HAND = 1;
    public static final int AREA_EQUIP = 2;
    public static final int AREA_JUDGE = 4;

    public static final int AREA_OWN = AREA_HAND | AREA_EQUIP;

    public static final int AREA_ALL = AREA_OWN | AREA_JUDGE;

    private <T> T awaitResponse(Class<T> tClass) {
        String data = null;
        while (true) {
            try {
                data = inputQueue.take();
                break;
            } catch (InterruptedException e) {
                log.error("InterruptedException", e);
            }
        }
        try {
            return JsonUtil.INST.readValue(data, tClass);
        } catch (Exception e) {
            throw new RuntimeException("can't parse data, rawData:[%s]".formatted(data), e);
        }
    }

    //TODO
    public CardUseDTO cardUseQuery(Player player, CardFilterIface allow, List<CardFilterIface> deny) {
        int index = cliInteraction.takeCardsInput(player.getHands());
        if (index != -1){
            CardBase card = player.getHands().get(index);
            CardUseDTO dto = new CardUseDTO();
            dto.setCard(card);
            dto.setPlayer(player);
            return dto;
        }
        return null;
    }
    public CardSacrificeDTO cardSacrificeQuery(Player player, CardFilterIface allow, List<CardFilterIface> deny) {return null;}
    public CardDiscardDTO cardDiscardQuery(Player player, int count, CardFilterIface allow, List<CardFilterIface> deny) {return null;}
    public Player playerTargetQuery(Player player, List<Player> options) {return null;}
    public List<Player> playerTargetsQuery(Player player, List<Player> options) {return null;}
    public CardDiscardDTO cardDiscardQuery(Player player, int count, int allowArea) {return null;}
    public CardDiscardDTO handCardDiscardQuery(Player player, int count, CardFilterIface allow, List<CardFilterIface> deny) {return null;}

    public CardBase pickOneCard(Player offender, Player defender, int allowArea) {
        //TODO
        return null;
    }
    public CardBase pickOneCard(Player offender, Player defender, int allowArea, CardFilterIface allow) {
        //TODO
        return null;
    }

    public int cardQuery(Player player, List<CardBase> cards) {
        //TODO
        return -1;
    }

    public AbilityBase abilitiesQuery(Player player, List<AbilityBase> abilityList) {
        //TODO
        return null;
    }
    public int abilityOptionQuery(Player player, AbilityBase ability, TextDictionary... options) {
        //TODO
        return -1;
    }
    public int optionQuery(Player player, String... options) {
        //TODO
        return -1;
    }

    public int optionQuery(Player player, List<String> options) {
        //TODO
        return -1;
    }
    public int optionQuery(Player player, List<String> options, int optMask) {
        //TODO
        return -1;
    }

    public int optionQuery(Player player, Map<Integer, String> options) {
        //TODO
        return -1;
    }

    public int abilityOptionQuery(Player player, AbilityBase ability, List<TextDictionary> options) {
        //TODO
        return -1;
    }

    public CardBase handCardPickQuery(Player player) {
        //TODO
        return null;
    }

}
