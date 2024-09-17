package io.skydeck.gserver.engine;

public interface NetworkInterface {
    public void notifyInput(NetworkContext nc);

    public Object readInput(NetworkContext nc);

}
