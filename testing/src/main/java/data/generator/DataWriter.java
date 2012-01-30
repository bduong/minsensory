package data.generator;

import data.DataLine;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class DataWriter {

    public static void createFile(String fileName, int dataSize, int numberOfLines) throws IOException {
        DataGen dataGen = new DataGen(dataSize);
        ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(fileName));

        for (int ii = 0; ii < numberOfLines; ii ++) {
            try {
                DataLine line = dataGen.getNextLine();
                for (int datum : line.getLine()) {
                    outputStream.writeInt(datum);
                }
            } catch (Exception e) {
                throw  new IOException("Could Not Write To File");
            }
        }

        outputStream.close();

    }
}
