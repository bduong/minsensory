/* **************************************************************************
 * Copyright (C) 2011
 * Benjamin Duong, ECE @ Boston University
  *
 * All rights reserved.
 * ************************************************************************** */

 package com.minsensory.data;

/**
 * The <code>DataLine</code> object represents a set of 256 data points. Each
 * point maps to one node in the 16x16 node array.
 */
public class DataLine {
    private int [] line;

    /**
     * Constructs a blank line with a given size.
     *
     * @param size size of the data line
     */
    public DataLine(int size) {
        line = new int[size];
    }

    /**
     * Constructs a line with the given data.
     *
     * @param data the data to fill the line with
     */
    public DataLine(int [] data) {
        line = data;
    }

    /**
     * Constructs a line with the data from another data line.
     *
     * @param dataLine the data line to copy from
     */
    public DataLine(DataLine dataLine) {
        line = dataLine.getLine();
    }

    /**
     * Get the data.
     *
     * @return the data
     */
    public int[] getLine() {
        return line;
    }

    public int getDataAt(int index){
        return line[index];
    }

    /**
     * Set the data.
     *
     * @param line the line of data
     */
    public void setLine(int[] line) {
        this.line = line;
    }

    /**
     * Add a line of data to this set of data element by element
     *
     * @param dataLine the data line to add
     */
    public void add(DataLine dataLine){
        int [] data = dataLine.getLine();
        for (int ii = 0; ii < line.length; ii++) {
            line[ii] += data[ii];
        }
    }

    /**
     * Divide the data in this set by a given number.
     *
     * @param divisor the number to divide by
     */
    public void divideBy(int divisor){
        for (int node : line) {
            node /= divisor;
        }
    }

    @Override
    public boolean equals(Object dataLine){
        boolean equal = true;
        if (dataLine instanceof DataLine) {

            int [] data = ((DataLine) dataLine).getLine();
            for (int ii = 0; ii < line.length; ii++) {
                if (line[ii] != data[ii]) {
                    equal = false;
                }
            }

            return equal;
        } else {
            return false;
        }
    }
    
    @Override
    public int hashCode() {
         return 31 + line.hashCode();
    }
}
