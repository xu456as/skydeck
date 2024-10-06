package io.skydeck.gserver.engine;

import io.skydeck.gserver.enums.NetworkFeedbackType;

public interface NetworkInterface {

    public Object readInput(NetworkContext nc);

    public void feedback(NetworkContext nc, NetworkFeedbackType feedbackType, String message);

}
