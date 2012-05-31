/* **************************************************************************
 * Copyright (C) 2011
 * Benjamin Duong, ECE @ Boston University
  *
 * All rights reserved.
 * ************************************************************************** */

 package data.generator;

import data.DataLine;
import java.util.Random;

public class DataGen {

    private Random random;
    private int dataLineSize;

    public DataGen(int dataLineSize) {
        random = new Random(System.currentTimeMillis());
        this.dataLineSize = dataLineSize;
    }

    public DataLine getNextLine() {
        int [] colors = new int[dataLineSize];
        for (int ii = 0; ii < dataLineSize; ii++){
            colors[ii] = random.nextInt();
        }

        return new DataLine(colors);
    }
}
