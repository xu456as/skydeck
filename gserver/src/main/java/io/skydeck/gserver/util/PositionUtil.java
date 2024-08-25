package io.skydeck.gserver.util;

import io.skydeck.gserver.domain.player.Player;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class PositionUtil {

    public static int distance(Player offender, Player defender, List<Player> playerList) {
        if (defender.isDead() || defender.getStageState().getInLimbo()) {
            return Integer.MAX_VALUE;
        }
        int idx = offender.getId();
        int minDist = Integer.MAX_VALUE;
        int distCount = 0;
        for (int i = 1; i < playerList.size(); i++) {
            int nextIdx = (idx + i) % playerList.size();
            Player nextP = playerList.get(nextIdx);
            if (nextP.getStageState().getInLimbo() || nextP.isDead()) {
                continue;
            }
            distCount++;
            if (nextIdx != defender.getId()) {
                continue;
            }
            int val = Math.max(0,  distCount + defender.defensePoint() - offender.offensePoint());
            minDist = Math.min(val, minDist);
            break;
        }

        distCount = 0;
        for (int i = 1; i < playerList.size(); i++) {
            int nextIdx = (playerList.size() + idx - i) % playerList.size();
            Player nextP = playerList.get(nextIdx);
            if (nextP.getStageState().getInLimbo() || nextP.isDead()) {
                continue;
            }
            distCount++;
            if (nextIdx != defender.getId()) {
                continue;
            }
            int val = Math.max(0,  distCount + defender.defensePoint() - offender.offensePoint());
            minDist = Math.min(val, minDist);
            break;
        }
        return minDist;
    }


    public static Player nextAlivePlayer(Player current, List<Player> playerList) {
        int idx = Collections.binarySearch(playerList, current);
        for (int i = 1; i < playerList.size(); i++) {
            int nextIdx = (idx + i) % playerList.size();
            if (!playerList.get(nextIdx).isDead()) {
                return playerList.get(nextIdx);
            }
        }
        return null;
    }

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
        targets = new ArrayList<>(targets);
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
