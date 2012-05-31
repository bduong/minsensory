/* **************************************************************************
 * Copyright (C) 2011
 * Benjamin Duong, ECE @ Boston University
  *
 * All rights reserved.
 * ************************************************************************** */

 package data;

import java.util.List;

/**
 * The <code>DataBank</code> object provides an interface to create a bank of data points
 * to update the UI with.
 */
public interface DataBank {

    /**
     * Gets the next data point stored in the data bank.
     *
     * @return The next data point.
     */
   public DataLine getNextPoint();

    /**
     * Gets a specific point stored in the data bank.
     *
     * @param number the index of the data point to get.
     * @return The data point.
     */
   public DataLine getPoint(int number);

    /**
     * Get a number of points from a start point to and end point
     *
     * @param begin the index to being
     * @param end the index to end
     * @return The data points as a <code>List</code>
     */
   public List<DataLine> getPoints(int begin, int end);

    /**
     * Add a data point to the end of the data bank.
     *
     * @param datum the data point to add.
     */
   public void addPoint(DataLine datum);

    /**
     * Gets the number of data points in the data bank.
     *
     * @return The number of data points.
     */
   public int getSize();

    /**
     * Resets the data bank to a given index.
     *
     * @param point The index to reset to.
     */
   public void resetTo(int point);

    /**
     * Gets the current index of the data bank
     *
     * @return The current index.
     */
   public int getPosition();

    /**
     * Checks if the data bank is at the end of the data.
     *
     * @return <code>True</code> if at the end, <code>false</code> otherwise
     */
   public boolean isAtEnd();

    /**
     * Clears all data from the data bank.
     */
   public void clear();
}
