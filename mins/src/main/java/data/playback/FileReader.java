package data.playback;

import data.DataReader;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class FileReader implements DataReader {

    private BufferedInputStream reader;
    private String fileName;
    private byte [] bytes;

    public FileReader(String fileName) throws IOException {
        this.fileName = fileName;
        reader = new BufferedInputStream(new FileInputStream(fileName), 1024*1024);
        bytes = new byte[2];
    }

    public FileReader(File file) throws IOException {
        reader = new BufferedInputStream(new FileInputStream(file), 1024*1024);
        fileName = file.getName();
        bytes = new byte[2];
    }

    @Override
    public int readNextInt() throws IOException{
        int bytesRead = reader.read(bytes, 0, 2);
        if(bytesRead == -1) {
            throw new IOException("End of File");
        }
        int value = bytes[0] << 8;
        return (0x0000FFFF & ((value) | (bytes[1] & 0x000000FF)));
    }

    public String getFileName(){
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void close() throws IOException {
        reader.close();
    }
}
