package data.realtime;

import data.DataBank;
import data.DataLine;

import javax.swing.*;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public class DataCollector extends SwingWorker<Void, DataLine>{

    private int numberOfLinesSince = 0;
    private DataBank dataBank;
    private boolean stop;
    private COMReader dataReader;
    private BufferedOutputStream outputStream;
    private byte [] bytes;

    public DataCollector(DataBank dataBank, COMReader reader) throws URISyntaxException, IOException {
      this.dataBank = dataBank;
      dataReader = reader;
      stop = false;
    }

    public DataCollector(DataBank dataBank, COMReader reader, File file) throws URISyntaxException, IOException {
        this.dataBank = dataBank;
        dataReader = reader;
        stop = false;
        outputStream = new BufferedOutputStream(new FileOutputStream(file));
        bytes = new byte[2];
    }

    public void setFileName() {

    }
    
    @Override
    protected Void doInBackground() throws Exception {
        numberOfLinesSince++;
        while (!stop) {
            int [] dataLine = new int[256];
            for (int ii = 0; ii < 256; ii++) {
                dataLine[ii] = dataReader.readNextInt();
                createWritableBytes(dataLine[ii]);
                outputStream.write(bytes);
            }
            publish(new DataLine(dataLine));
        }
        return null;
    }

    private void createWritableBytes(int i) {
        int upper =  i & 0x0000FF00;
        int lower = i & 0x000000FF;
        bytes[0] = (byte) (upper >> 8);
        bytes[1] = (byte) lower;
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
