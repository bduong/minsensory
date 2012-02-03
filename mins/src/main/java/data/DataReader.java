package data;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class DataReader {

    private ObjectInputStream reader;
    private String fileName;

    public DataReader(String fileName) throws IOException {
        this.fileName = fileName;
        reader = new ObjectInputStream(new FileInputStream(fileName));
    }
    
    public DataReader(File file) throws IOException {
        reader = new ObjectInputStream(new FileInputStream(file));
    }
    
    public int readNextInt() throws IOException {
        return reader.readInt();
    }
    
    public String getFileName(){
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}