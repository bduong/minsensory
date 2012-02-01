package data;

import java.util.LinkedList;

/**
 * Holds the lines of data to be processed
 */
public class DataBank {

    private LinkedList<DataLine> data;

    /**
     * Constructs an empty data bank
     */
    public DataBank() {
        data = new LinkedList<DataLine>();
    }

    /**
     * Removes and returns the first data point.
     *
     * @return The first data point stored in the bank.
     */
    public DataLine getNextPoint() {
        return data.removeFirst();
    }

    /**
     * Gets the ith data point in the data bank. Removes all points before the ith point inclusively.
     *
     * @param number the number of the data point
     * @return The data point.
     */
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
    public void addPoint(DataLine datum){
        data.add(datum);
    }
}
