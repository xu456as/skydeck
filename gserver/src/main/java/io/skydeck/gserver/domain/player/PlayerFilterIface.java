package io.skydeck.gserver.domain.player;

public interface PlayerFilterIface {
    public boolean filter(Player player, Enum event);
}
