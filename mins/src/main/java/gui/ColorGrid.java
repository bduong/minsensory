package gui;

import javax.swing.*;
import java.awt.*;

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
        //setBorder(BorderFactory.createLineBorder(Color.RED));
    }

    private static char [] nodes ="ABCDEFGHIJKLMNOP".toCharArray();

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
        //rowTicks.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        //colTicks.setBorder(BorderFactory.createLineBorder(Color.BLACK));

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

        //layout.linkSize(SwingConstants.HORIZONTAL, image, colTicks);

        layout.setVerticalGroup(layout.createSequentialGroup()
          .addComponent(colTicks)
          .addGap(10)
          .addGroup(
            layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
            .addComponent(rowTicks)
            .addComponent(image, 0, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
          )
        );

        //layout.linkSize(SwingConstants.VERTICAL, rowTicks, image);

    }
}
