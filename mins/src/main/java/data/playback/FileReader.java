package data.playback;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Reads data from a file.
 */
public class FileReader {

    private BufferedInputStream reader;
    private String fileName;
    private byte [] bytes;

    /**
     * Creates a new <code>FileReader</code>
     *
     * @param fileName the name of the file
     * @throws IOException If we cannot open the file.
     */
    public FileReader(String fileName) throws IOException {
        this.fileName = fileName;
        reader = new BufferedInputStream(new FileInputStream(fileName), 1024*1024);
        bytes = new byte[2];
    }

    /**
     * Creates a new <code>FileReader</code>
     *
     * @param file the file to open
     * @throws IOException If we cannot open the file.
     */
    public FileReader(File file) throws IOException {
        reader = new BufferedInputStream(new FileInputStream(file), 1024*1024);
        fileName = file.getName();
        bytes = new byte[2];
    }

    /**
     * Reads in the next 2 bytes as an int.
     *
     * @return the next 2 bytes cast as an int
     * @throws IOException If we are at the end of the file.
     */
    public int readNextInt() throws IOException{
        int bytesRead = reader.read(bytes, 0, 2);
        if(bytesRead == -1) {
            throw new IOException("End of File");
        }
        int value = bytes[0] << 8;
        return (0x0000FFFF & ((value) | (bytes[1] & 0x000000FF)));
    }

    /**
     * Closes the file stream.
     *
     * @throws IOException If we cannot close the file.
     */
    public void close() throws IOException {
        reader.close();
    }
}
