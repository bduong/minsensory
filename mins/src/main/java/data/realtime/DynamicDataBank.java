package data.realtime;

import data.DataBank;
import data.DataLine;

import java.util.LinkedList;

/**
 * Holds the lines of data to be processed
 */
public class DynamicDataBank implements DataBank {

    private LinkedList<DataLine> data;

    /**
     * Constructs an empty data bank
     */
    public DynamicDataBank() {
        data = new LinkedList<DataLine>();
    }

    /**
     * Removes and returns the first data point.
     *
     * @return The first data point stored in the bank.
     */
    @Override
    public DataLine getNextPoint() {
        return data.removeFirst();
    }

    /**
     * Gets the ith data point in the data bank. Removes all points before the ith point inclusively.
     *
     * @param number the number of the data point
     * @return The data point.
     */
    @Override
    public DataLine getPoint(int number) {
        for (int ii = 0; ii < number; ii++) {
            data.removeFirst();
        }
        return data.removeFirst();
    }

    /**
     * Add a data point to the bottom of the bank.
     *
     * @param datum the data point
     */
    @Override
    public void addPoint(DataLine datum){
        data.add(datum);
    }

    @Override
    public int getSize() {
        return data.size();
    }
}
