package data;

import java.util.LinkedList;

public class DataBank {

    private LinkedList<DataLine> data;

    public DataBank() {
        data = new LinkedList<DataLine>();
    }

    public DataLine getNextPoint() {
        return data.removeFirst();
    }

    public DataLine getPoint(int number) {
        for (int ii = 0; ii < number; ii++) {
            data.removeFirst();
        }
        return data.removeFirst();
    }
    
    public void addPoint(DataLine datum){
        data.add(datum);
    }
}
