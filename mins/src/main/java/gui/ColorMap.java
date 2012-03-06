package gui

public enum ColorMap {

ALPHA(20),

BETA(40),

GAMMA(80),

DELTA(160),

THETA(320),

SPIKE(640);


public ColorMap(int color) {
	rgb = color;
}

private int rgb;
}
