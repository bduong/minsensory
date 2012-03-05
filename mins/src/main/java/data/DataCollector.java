package data;

import gui.OperatingMode;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import javax.swing.*;
import java.util.List;

public class DataCollector extends SwingWorker<Void, DataLine>{

    private int numberOfLinesSince = 0;
    private DataBank dataBank;
    private boolean stop;
    private DataReader dataReader;

    public DataCollector(DataBank dataBank, DataReader reader) throws URISyntaxException, IOException {
      this.dataBank = dataBank;
      File dataFile = new File(this.getClass().getResource("dataFile.txt").toURI());
      dataReader = reader;
        stop = false;
    }
    
    @Override
    protected Void doInBackground() throws Exception {
        numberOfLinesSince++;
        int [] dataLine = new int[256];
        for (int ii = 0; ii < 256; ii++) {
            dataLine[ii] = dataReader.readNextInt();
        }
        publish(new DataLine(dataLine));
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
