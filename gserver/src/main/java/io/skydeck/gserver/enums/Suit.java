package io.skydeck.gserver.enums;

public enum Suit {
    None(0, Color.None),
    Diamond(1, Color.Red),
    Heart(2, Color.Red),
    Club(3, Color.Black),
    Spade(4, Color.Black);

    private Suit(int code, Color color) {
        this.code = code;
        this.color = color;
    }
    private int code;
    private Color color;

    public int code() {
        return code;
    }
    public Color color() {
        return color;
    }
}
