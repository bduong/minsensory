package data;

import javax.swing.*;
import java.util.List;

public class DataCollector extends SwingWorker<Void, DataLine>{

    private int numberOfLinesSince = 0;
    private DataBank dataBank;

    public DataCollector(DataBank dataBank) {
      this.dataBank = dataBank;
    }
    
    @Override
    protected Void doInBackground() throws Exception {
        numberOfLinesSince++;
        //do some stuff to get data
        publish(new DataLine(256));
        return null;
    }

    @Override
    protected void process(List<DataLine> dataLines) {
        int snapshotNumber = numberOfLinesSince;
        numberOfLinesSince = 0;
        DataLine returnedLine = new DataLine(dataLines.remove(0));
        for (int ii = 1; ii < snapshotNumber; ii ++) {
            returnedLine.add(dataLines.remove(0));
        }
        returnedLine.divideBy(snapshotNumber);
        dataBank.addPoint(returnedLine);
    }


}
