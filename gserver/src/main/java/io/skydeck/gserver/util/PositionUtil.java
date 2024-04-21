package io.skydeck.gserver.util;

import io.skydeck.gserver.domain.Player;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class PositionUtil {
    public static List<Player> positionSort(Player current, Set<Player> targets) {
        if (CollectionUtils.isEmpty(targets)) {
            return Collections.emptyList();
        }
        return positionSort(current, targets.stream().toList());
    }
    public static List<Player> positionSort(Player current, List<Player> targets) {
        if (CollectionUtils.isEmpty(targets)) {
            return Collections.emptyList();
        }
        Collections.sort(targets);
        List<Player> newList = new ArrayList<>(targets.size());
        int stIdx = 0;
        for (int i = 0; i < targets.size(); i++) {
            if (targets.get(i).getId() >= current.getId()) {
                stIdx = i;
                break;
            }
        }
        for (int i = 0; i < targets.size(); i++) {
            newList.add(targets.get((stIdx + i) % targets.size()));
        }
        return newList;
    }
}
