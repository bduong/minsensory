package gui;

public enum ColorMap {

    NO_BAND(1),

    ALPHA(20),

    BETA(40),

    GAMMA(80),

    DELTA(160),

    THETA(320),

    SPIKE(640);


    ColorMap(int color) {
        rgb1 = color;
    }

    private int rgb1;

    public static int [] translate(int [] points){
    for( int ii = 0; ii < points.length; ii++) {
        int rgb = points[ii] & 0x0000FA00;
            rgb = rgb >> 10;
            RGB rgbValues = getBand(rgb);
            int value = (points[ii] & 0x000003FA)>>2;
            points[ii] = (value * rgbValues.existsRed()) << 16 | (value * rgbValues.existsGreen()) << 8 | (value * rgbValues.existsBlue());
        }
        return points;
    }

    private static RGB getBand(int num) {
        boolean r = false;
        boolean g = false;
        boolean b = false;

        if( num == 0) {
            r = true;
            g = true;
            b = true;
        } else {

            if( (num & 0x1) != 0) { //Alpha exists
                r = true;
            }
            if ( (num & 0x2) != 0) { //Beta exists
                b = true;
            }
            if ( (num & 0x4) != 0 ) {//Gamma exists
                g = true;
            }
            if ( (num & 0x8) != 0) { //Delta exists
                r = true;
                b = true;
            }
            if ( (num & 0x10) != 0 ) { //Theta exists
                r = true;
                g = true;
            }
            if ( (num & 0x20) != 0) { //Spike exists
                g = true;
                b = true;
            }
        }

       return new RGB(r, g, b);
    }

    private static class RGB {
        boolean r;
        boolean g;
        boolean b;

        public RGB(boolean r, boolean g, boolean b) {
            this.r = r;
            this.g = g;
            this.b = b;
        }

        public int existsRed() {
            if(r) return 1;
            return 0;
        }
        public int existsGreen() {
            if(g) return 1;
            return 0;
        }
        public int existsBlue() {
            if(b) return 1;
            return 0;
        }
    }
}
