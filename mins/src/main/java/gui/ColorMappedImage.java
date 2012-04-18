/******************************************************************************/
/*          Copyright MINSensory - Boston University 2011-2012                */
/*                                                                            */
/******************************************************************************/
package gui;

import data.DataLine;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Image to hold color grid
 */
public class ColorMappedImage extends JPanel {

    private BufferedImage image;
    private int width = 16;
    private int height = 16;

    private DataType dataType;

    public ColorMappedImage() {
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    }

    public ColorMappedImage(int width, int height) {
        this.width = width;
        this.height = height;
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        dataType = DataType.PROCESSED;
    }

    public void setDataType(DataType type) {
        dataType = type;
    }

    public void setColors(int [] rgb) {
        if (rgb.length == width*height) {
            image.setRGB(0,0,width,height,rgb,0,width);
        } else {
            throw new IllegalArgumentException("Array must be length" + width*height);
        }
    }

    List<Integer> spikes = new ArrayList<Integer>();
    /**
     * Update
     *
     * @param line
     */
    public void updateImage(DataLine line){
        if (dataType == DataType.PROCESSED) {
            image.setRGB(0, 0, width, height, translateToRGB(line.getLine()), 0, width);
        } else {
            image.setRGB(0, 0, width, height, translateToGrayscale(line.getLine()), 0, width);
        }
        repaint();
    }

    public List<Integer> updateImage(DataLine line, Set<Integer> spikeNodes){
        spikes.clear();
        if (dataType == DataType.PROCESSED) {
            image.setRGB(0, 0, width, height, translateToRGB(line.getLine(), spikeNodes), 0, width);
        } else {
            image.setRGB(0, 0, width, height, translateToGrayscale(line.getLine()), 0, width);
        }
        repaint();
        return spikes;
    }


    private int[] convertDataToColors(int[] data) {


        return data;
    }

    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), this);
    }

    public int [] translateToGrayscale(int[] points){
        int [] newPoints = new int[points.length];
        for( int ii = 0; ii < points.length; ii++) {
            int number = points[ii];
            int value = (number & 0x00000FF0);
            value = value>>4;
            newPoints[ii] = new Color(value, value, value).getRGB();

        }
        return newPoints;
    }

    public int [] translateToRGB(int[] points){
        int [] newPoints = new int[points.length];
        for( int ii = 0; ii < points.length; ii++) {
            int number = points[ii];
            int rgb = number & 0x0000FC00;
            rgb = rgb >> 10;
            int value = (number & 0x000003F0);
            value = value>>6;
            Color color = getBand(rgb, value);

            newPoints[ii] = color.getRGB();
        }
        return newPoints;
    }

    public int [] translateToRGB(int[] points, Set<Integer> spikeNodes){
        int [] newPoints = new int[points.length];
        for( int ii = 0; ii < points.length; ii++) {
            if (spikeNodes.contains(ii)) {
                newPoints[ii] = Color.white.getRGB();
            } else {
                int number = points[ii];
                int rgb = number & 0x0000FC00;
                rgb = rgb >> 10;
                int value = (number & 0x000003F0);
                value = value>>6;
                Color color = getBand(rgb, value);

                newPoints[ii] = color.getRGB();
                if (color == Color.white) {
                    spikes.add(ii);
                }
            }
        }
        return newPoints;
    }


    private Color getBand(int num, int value) {
        int r = 0;
        int b = 0;
        int g = 0;

        if ( (num & 0x20) != 0) { //Spike exists
            return Color.white;
        }

        if( num == 0) {
            r = 1;
            g = 1;
            b = 1;
        } else {

            if( (num & 0x1) != 0) { //Alpha exists
                r += 4;
            }
            if ( (num & 0x2) != 0) { //Beta exists
                b += 4;
            }
            if ( (num & 0x4) != 0 ) {//Gamma exists
                g += 4;
            }
            if ( (num & 0x8) != 0) { //Delta exists
                r += 2;
                b += 1;
            }
            if ( (num & 0x10) != 0 ) { //Theta exists
                r += 1;
                g += 2;
            }

        }
        r *= value;
        g *= value;
        b *= value;

        r = r<<2;
        b = b<<2;
        g = g<<2;

        if (r > 255) r = 255;
        if (b > 255) b = 255;
        if (g > 255) g = 255;

        //System.out.println("R: " + r + " G: " + g + " B: " + b);
        return new Color(r, g, b);

    }
}
