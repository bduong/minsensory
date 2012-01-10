/******************************************************************************/
/*          Copyright MINSensory - Boston University 2011-2012                */
/*                                                                            */
/******************************************************************************/
package gui;

import javax.swing.*;
import java.awt.image.BufferedImage;

public class ColorMappedImage extends JPanel {
    
    private BufferedImage image;
    private int width = 16;
    private int height = 16;
    
    public ColorMappedImage() {
        image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
    }

    public ColorMappedImage(int width, int height) {
        this.width = width;
        this.height = height;
        image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
    }
    
    public void setColors(int [] rgb) {
        if (rgb.length == width*height) {

        } else {
            throw new IllegalArgumentException("Array must be length" + width*height);
        }
    }
}
