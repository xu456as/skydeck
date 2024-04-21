package io.skydeck.gserver.enums;

public enum Color {
    None(0),
    Red(1),
    Black(2);

    private Color(int code) {
        this.code = code;
    }
    private int code;

    public int code() {
        return code;
    }
}
