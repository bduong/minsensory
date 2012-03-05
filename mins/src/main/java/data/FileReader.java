package data;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class FileReader implements DataReader{

    private ObjectInputStream reader;
    private String fileName;

    public FileReader(String fileName) throws IOException {
        this.fileName = fileName;
        reader = new ObjectInputStream(new FileInputStream(fileName));
    }
    
    public FileReader(File file) throws IOException {
        reader = new ObjectInputStream(new FileInputStream(file));
    }

    @Override
    public int readNextInt() {
        try {
            return reader.readInt();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return 0;
    }
    
    public String getFileName(){
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

}
