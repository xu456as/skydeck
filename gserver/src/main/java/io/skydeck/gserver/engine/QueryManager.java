package io.skydeck.gserver.engine;

import io.skydeck.gserver.domain.protocol.request.CardDiscardReq;
import io.skydeck.gserver.domain.protocol.request.CardSacrificeReq;
import io.skydeck.gserver.domain.protocol.request.CardUseReq;
import io.skydeck.gserver.domain.protocol.request.PlayerTargetReq;
import io.skydeck.gserver.domain.skill.AbilityBase;
import io.skydeck.gserver.domain.card.CardBase;
import io.skydeck.gserver.domain.card.CardFilter;
import io.skydeck.gserver.domain.player.Player;
import io.skydeck.gserver.domain.dto.CardDiscardDTO;
import io.skydeck.gserver.domain.dto.CardSacrificeDTO;
import io.skydeck.gserver.domain.dto.CardUseDTO;
import io.skydeck.gserver.enums.NCInputType;
import io.skydeck.gserver.enums.NetworkFeedbackType;
import io.skydeck.gserver.i18n.TextDictionary;
import io.skydeck.gserver.interaction.CliInteraction;
import io.skydeck.gserver.util.JsonUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Slf4j
@Scope("prototype")
public class QueryManager {

    private static final int MAX_RETRY = 3;

    private GameEngine engine;

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

    private NetworkContext newNC(Player player, NCInputType inputType) {
        long currentEId = engine.getEventIncrementer().getAndIncrement();
        return NetworkContext.builder().gameEngine(engine).inputId(currentEId).player(player).inputType(inputType).build();
    }
    private NetworkContext newNC(Player player, NCInputType inputType, Object info) {
        long currentEId = engine.getEventIncrementer().getAndIncrement();
        return NetworkContext.builder().gameEngine(engine).inputId(currentEId).player(player).inputType(inputType).info(info).build();
    }
    private <T> T readRequest(NetworkContext nc, Class<T> tClass) {
        NetworkInterface network = engine.getNetwork();
        String data = (String) network.readInput(nc);
        T req;
        try {
            req = JsonUtil.INST.readValue(data, tClass);
        } catch (Exception e) {
            throw new RuntimeException("can't read input data[%s]".formatted(data));
        }
        return req;
    }
    private void notify(NetworkContext nc, NetworkFeedbackType type, String message) {
        NetworkInterface network = engine.getNetwork();
        network.feedback(nc, type, message);
    }

    private boolean doFilter(CardBase card, CardFilter allow, List<CardFilter> deny) {
        if (card == null) {
            return false;
        }
        boolean ret = true;
        if (allow != null) {
            ret = allow.filter(card);
            if (!ret) {
                return false;
            }
        }
        if (deny != null) {
            for (CardFilter filter : deny) {
                ret = filter.filter(card);
                if (!ret) {
                    return ret;
                }
            }
        }
        return ret;
    }

    public CardUseDTO cardUseQuery(Player player, CardFilter allow, List<CardFilter> deny) {
        NetworkContext nc = newNC(player, NCInputType.CardUse);
        int attempt = 0;
        notify(nc, NetworkFeedbackType.Query, "please enter your input");
        while (attempt++ < MAX_RETRY) {
            CardUseReq req = readRequest(nc, CardUseReq.class);
            CardBase card = player.getCardById(req.getCardId());
            if (doFilter(card, allow, deny)) {
                if (attempt <= MAX_RETRY) {
                    notify(nc, NetworkFeedbackType.Retry, "please retry your input");
                    continue;
                } else {
                    notify(nc, NetworkFeedbackType.Retry, "can't receive correct input");
                    break;
                }
            }
            CardUseDTO dto = new CardUseDTO();
            dto.setPlayer(player);
            dto.setCard(card);
            for (Integer tId : req.getTargets()) {
                engine.getPlayers().stream()
                        .filter(p -> Objects.equals(p.getId(), tId))
                        .findFirst()
                        .ifPresent(target -> dto.getTargets().put(target, 1));
            }
            return dto;
        }
        throw new RuntimeException("can't receive correct input");
    }
    public CardSacrificeDTO cardSacrificeQuery(Player player, CardFilter allow, List<CardFilter> deny) {
        NetworkContext nc = newNC(player, NCInputType.CardSacrifice);
        int attempt = 0;
        notify(nc, NetworkFeedbackType.Query, "please enter your input");
        while (attempt++ < MAX_RETRY) {
            CardSacrificeReq req = readRequest(nc, CardSacrificeReq.class);
            CardBase card = player.getCardById(req.getCardId());
            if (doFilter(card, allow, deny)) {
                if (attempt <= MAX_RETRY) {
                    notify(nc, NetworkFeedbackType.Retry, "please retry your input");
                    continue;
                } else {
                    notify(nc, NetworkFeedbackType.Retry, "can't receive correct input");
                    break;
                }
            }
            CardSacrificeDTO dto = new CardSacrificeDTO();
            dto.setPlayer(player);
            dto.setCard(card);
            return dto;
        }
        throw new RuntimeException("can't receive correct input");
    }
    public Player playerTargetQuery(Player player, List<Player> options) {
        if (CollectionUtils.isEmpty(options)) {
            return null;
        }
        List<Integer> playerIds = options.stream().map(Player::getId).toList();
        NetworkContext nc = newNC(player, NCInputType.PlayerSelect, PlayerTargetReq.Info.single(playerIds));
        int attempt = 0;
        notify(nc, NetworkFeedbackType.Query, "please enter your input");
        while (attempt++ < MAX_RETRY) {
            PlayerTargetReq req = readRequest(nc, PlayerTargetReq.class);
            Player offender = engine.getPlayers().stream().filter(p -> Objects.equals(p.getId(), req.getUserId())).findFirst().orElse(null);
            List<Player> defenders = engine.getPlayers().stream().filter(p -> req.getTargetIds().contains(p.getId())).toList();
            if (offender == null || defenders.size() != 1) {
                if (attempt <= MAX_RETRY) {
                    notify(nc, NetworkFeedbackType.Retry, "please retry your input");
                    continue;
                } else {
                    notify(nc, NetworkFeedbackType.Retry, "can't receive correct input");
                    break;
                }
            }
            return defenders.get(0);
        }
        throw new RuntimeException("can't receive correct input");
    }
    public List<Player> playerTargetsQuery(Player player, List<Player> options) {
        if (CollectionUtils.isEmpty(options)) {
            return Collections.emptyList();
        }
        List<Integer> playerIds = options.stream().map(Player::getId).toList();
        NetworkContext nc = newNC(player, NCInputType.PlayerSelect, PlayerTargetReq.Info.multiple(playerIds));
        int attempt = 0;
        notify(nc, NetworkFeedbackType.Query, "please enter your input");
        while (attempt++ < MAX_RETRY) {
            PlayerTargetReq req = readRequest(nc, PlayerTargetReq.class);
            Player offender = engine.getPlayers().stream().filter(p -> Objects.equals(p.getId(), req.getUserId())).findFirst().orElse(null);
            List<Player> defenders = engine.getPlayers().stream().filter(p -> req.getTargetIds().contains(p.getId())).toList();
            if (offender == null || CollectionUtils.isEmpty(defenders)) {
                if (attempt <= MAX_RETRY) {
                    notify(nc, NetworkFeedbackType.Retry, "please retry your input");
                    continue;
                } else {
                    notify(nc, NetworkFeedbackType.Retry, "can't receive correct input");
                    break;
                }
            }
            return defenders;
        }
        throw new RuntimeException("can't receive correct input");
    }

    public CardDiscardDTO cardDiscardQuery(Player player, int count, CardFilter allow, List<CardFilter> deny) {
        NetworkContext nc = newNC(player, NCInputType.CardDiscard, CardDiscardReq.Info.builder().discardCount(count).build());
        int attempt = 0;
        notify(nc, NetworkFeedbackType.Query, "please enter your input");
        while (attempt++ < MAX_RETRY) {
            CardDiscardReq req = readRequest(nc, CardDiscardReq.class);
            Player offender = engine.getPlayers().stream().filter(p -> Objects.equals(p.getId(), req.getUserId())).findFirst().orElse(null);
            Player defender = engine.getPlayers().stream().filter(p -> Objects.equals(p.getId(), req.getTargetId())).findFirst().orElse(null);
            if (offender == null || defender == null) {
                if (attempt <= MAX_RETRY) {
                    notify(nc, NetworkFeedbackType.Retry, "please retry your input");
                    continue;
                } else {
                    notify(nc, NetworkFeedbackType.Retry, "can't receive correct input");
                    break;
                }
            }
            List<CardBase> cardDiscardList = new ArrayList<>();
            boolean filterFail = false;
            for (int i = 0; i < req.getCardIdList().size() && !filterFail; i++) {
                Integer cardId = req.getCardIdList().get(i);
                CardBase card = player.getCardById(cardId);
                if (doFilter(card, allow, deny)) {
                    filterFail = true;
                    continue;
                }
                cardDiscardList.add(card);
            }
            if (filterFail) {
                if (attempt <= MAX_RETRY) {
                    notify(nc, NetworkFeedbackType.Retry, "please retry your input");
                    continue;
                } else {
                    notify(nc, NetworkFeedbackType.Retry, "can't receive correct input");
                    break;
                }
            }
            CardDiscardDTO dto = CardDiscardDTO.builder()
                    .offender(offender)
                    .defender(defender)
                    .card(cardDiscardList)
                    .build();
            return dto;
        }
        throw new RuntimeException("can't receive correct input");
    }
    public CardDiscardDTO cardDiscardQuery(Player player, int count, int allowArea) {
        NetworkContext nc = newNC(player, NCInputType.CardDiscard, CardDiscardReq.Info.builder().discardCount(count).build());
        int attempt = 0;
        notify(nc, NetworkFeedbackType.Query, "please enter your input");
        while (attempt++ < MAX_RETRY) {
            CardDiscardReq req = readRequest(nc, CardDiscardReq.class);
            Player offender = engine.getPlayers().stream().filter(p -> Objects.equals(p.getId(), req.getUserId())).findFirst().orElse(null);
            Player defender = engine.getPlayers().stream().filter(p -> Objects.equals(p.getId(), req.getTargetId())).findFirst().orElse(null);
            if (offender == null || defender == null) {
                if (attempt <= MAX_RETRY) {
                    notify(nc, NetworkFeedbackType.Retry, "please retry your input");
                    continue;
                } else {
                    notify(nc, NetworkFeedbackType.Retry, "can't receive correct input");
                    break;
                }
            }
            List<CardBase> cardDiscardList = new ArrayList<>();
            boolean filterFail = false;
            for (int i = 0; i < req.getCardIdList().size() && !filterFail; i++) {
                Integer cardId = req.getCardIdList().get(i);
                CardBase card = player.getCardById(cardId, allowArea);
                if(card == null) {
                    filterFail = true;
                    continue;
                }
                cardDiscardList.add(card);
            }
            if (filterFail) {
                if (attempt <= MAX_RETRY) {
                    notify(nc, NetworkFeedbackType.Retry, "please retry your input");
                    continue;
                } else {
                    notify(nc, NetworkFeedbackType.Retry, "can't receive correct input");
                    break;
                }
            }
            CardDiscardDTO dto = CardDiscardDTO.builder()
                    .offender(offender)
                    .defender(defender)
                    .card(cardDiscardList)
                    .build();
            return dto;
        }
        throw new RuntimeException("can't receive correct input");
    }
    public CardDiscardDTO handCardDiscardQuery(Player player, int count, CardFilter allow, List<CardFilter> deny) {
        NetworkContext nc = newNC(player, NCInputType.CardDiscard, CardDiscardReq.Info.builder().discardCount(count).build());
        int attempt = 0;
        notify(nc, NetworkFeedbackType.Query, "please enter your input");
        while (attempt++ < MAX_RETRY) {
            CardDiscardReq req = readRequest(nc, CardDiscardReq.class);
            Player offender = engine.getPlayers().stream().filter(p -> Objects.equals(p.getId(), req.getUserId())).findFirst().orElse(null);
            Player defender = engine.getPlayers().stream().filter(p -> Objects.equals(p.getId(), req.getTargetId())).findFirst().orElse(null);
            if (offender == null || defender == null) {
                if (attempt <= MAX_RETRY) {
                    notify(nc, NetworkFeedbackType.Retry, "please retry your input");
                    continue;
                } else {
                    notify(nc, NetworkFeedbackType.Retry, "can't receive correct input");
                    break;
                }
            }
            List<CardBase> cardDiscardList = new ArrayList<>();
            boolean filterFail = false;
            for (int i = 0; i < req.getCardIdList().size() && !filterFail; i++) {
                Integer cardId = req.getCardIdList().get(i);
                CardBase card = player.getCardById(cardId, AREA_HAND);
                if(card == null) {
                    filterFail = true;
                    continue;
                }
                if (doFilter(card, allow, deny)) {
                    filterFail = true;
                    continue;
                }
                cardDiscardList.add(card);
            }
            if (filterFail) {
                if (attempt <= MAX_RETRY) {
                    notify(nc, NetworkFeedbackType.Retry, "please retry your input");
                    continue;
                } else {
                    notify(nc, NetworkFeedbackType.Retry, "can't receive correct input");
                    break;
                }
            }
            CardDiscardDTO dto = CardDiscardDTO.builder()
                    .offender(offender)
                    .defender(defender)
                    .card(cardDiscardList)
                    .build();
            return dto;
        }
        throw new RuntimeException("can't receive correct input");
    }

    //TODO fix me
    public CardBase pickOneCard(Player offender, Player defender, int allowArea) {
        NetworkContext nc = newNC(player, NCInputType.CardSelect, CardDiscardReq.Info.builder().discardCount(count).build());
        int attempt = 0;
        notify(nc, NetworkFeedbackType.Query, "please enter your input");
        while (attempt++ < MAX_RETRY) {
            CardDiscardReq req = readRequest(nc, CardDiscardReq.class);
            Player offender = engine.getPlayers().stream().filter(p -> Objects.equals(p.getId(), req.getUserId())).findFirst().orElse(null);
            Player defender = engine.getPlayers().stream().filter(p -> Objects.equals(p.getId(), req.getTargetId())).findFirst().orElse(null);
            if (offender == null || defender == null) {
                if (attempt <= MAX_RETRY) {
                    notify(nc, NetworkFeedbackType.Retry, "please retry your input");
                    continue;
                } else {
                    notify(nc, NetworkFeedbackType.Retry, "can't receive correct input");
                    break;
                }
            }
            List<CardBase> cardDiscardList = new ArrayList<>();
            boolean filterFail = false;
            for (int i = 0; i < req.getCardIdList().size() && !filterFail; i++) {
                Integer cardId = req.getCardIdList().get(i);
                CardBase card = player.getCardById(cardId, AREA_HAND);
                if(card == null) {
                    filterFail = true;
                    continue;
                }
                if (doFilter(card, allow, deny)) {
                    filterFail = true;
                    continue;
                }
                cardDiscardList.add(card);
            }
            if (filterFail) {
                if (attempt <= MAX_RETRY) {
                    notify(nc, NetworkFeedbackType.Retry, "please retry your input");
                    continue;
                } else {
                    notify(nc, NetworkFeedbackType.Retry, "can't receive correct input");
                    break;
                }
            }
            CardDiscardDTO dto = CardDiscardDTO.builder()
                    .offender(offender)
                    .defender(defender)
                    .card(cardDiscardList)
                    .build();
            return dto;
        }
        throw new RuntimeException("can't receive correct input");
    }
    public CardBase pickOneCard(Player offender, Player defender, int allowArea, CardFilter allow) {
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
