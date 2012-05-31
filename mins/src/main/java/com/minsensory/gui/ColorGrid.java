/* **************************************************************************
 * Copyright (C) 2011
 * Benjamin Duong, ECE @ Boston University
  *
 * All rights reserved.
 * ************************************************************************** */

 package com.minsensory.gui;

import javax.swing.*;
import java.awt.*;

/**
 * The <code>ColorGrid</code> object applies tick markers for the <code>ColorMappedImage</code>.
 *
 * It creates two JPanels to serve as the tick axes. An layouts out the image and the two tick panels.
 *
 * The left panel is label 1-16 and the top panel is labeled A-P to represent 16 nodes in either direction.
 */
public class ColorGrid extends JPanel{

    private ColorMappedImage image;
    private JPanel rowTicks;
    private JPanel colTicks;

    public ColorGrid(ColorMappedImage image) {
        this.image = image;
        image.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        rowTicks = new JPanel(new GridLayout(16, 1, 0, 0));
        colTicks = new JPanel(new GridLayout(1, 16, 0, 0));
        init();
    }

    private static char [] nodes ="ABCDEFGHIJKLMNOP".toCharArray();

    /**
     * Initialize and layout the image and tick markers.
     */
    private void init() {
        Dimension imageSize = image.getPreferredSize();
        for (int ii = 0; ii < 16; ii++) {
            rowTicks.add(new JLabel(String.valueOf(ii+1), JLabel.RIGHT));
            JLabel label = new JLabel(String.valueOf(nodes[ii]), JLabel.CENTER);
            label.setVerticalAlignment(JLabel.BOTTOM);
            colTicks.add(label);
        }
        Dimension size = rowTicks.getMaximumSize();
        size.setSize(25, size.getHeight());
        rowTicks.setMaximumSize(size);
        size = colTicks.getMaximumSize();
        size.setSize(size.getWidth(), 20);
        colTicks.setMaximumSize(size);
        size = colTicks.getPreferredSize();
        size.setSize(imageSize.getWidth(), size.getHeight());
        colTicks.setPreferredSize(size);

        GroupLayout layout = new GroupLayout(this);
        setLayout(layout);

        layout.setAutoCreateContainerGaps(false);
        layout.setAutoCreateGaps(false);

        layout.setHorizontalGroup(layout.createSequentialGroup()
          .addComponent(rowTicks)
          .addGap(10)
          .addGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
              .addComponent(colTicks)
              .addComponent(image, 0, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE))
        );

        layout.setVerticalGroup(layout.createSequentialGroup()
          .addComponent(colTicks)
          .addGap(10)
          .addGroup(
            layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
            .addComponent(rowTicks)
            .addComponent(image, 0, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
          )
        );
    }
}
