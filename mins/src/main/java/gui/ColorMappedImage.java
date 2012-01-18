/******************************************************************************/
/*          Copyright MINSensory - Boston University 2011-2012                */
/*                                                                            */
/******************************************************************************/
package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ColorMappedImage extends JPanel {
    
    private BufferedImage image;
    private int width = 16;
    private int height = 16;
    
    public ColorMappedImage() {
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    }

    public ColorMappedImage(int width, int height) {
        this.width = width;
        this.height = height;
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    }
    
    public void setColors(int [] rgb) {
        if (rgb.length == width*height) {
            image.setRGB(0,0,width,height,rgb,0,width);
        } else {
            throw new IllegalArgumentException("Array must be length" + width*height);
        }
    }
    
    @Override
    public void paintComponent(Graphics g) {
       g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), this);
    }

}
