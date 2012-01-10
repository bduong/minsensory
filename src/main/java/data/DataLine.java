package data;

public class DataLine {
    private int [] line;

    public DataLine(int size) {
       line = new int[size];
    }

    public DataLine(int [] data) {
        line = data;
    }

    public DataLine(DataLine dataLine) {
        line = dataLine.getLine();
    }
        
    public int[] getLine() {
        return line;
    }

    public void setLine(int[] line) {
        this.line = line;
    }

    public void add(DataLine dataLine){
        int [] data = dataLine.getLine();
        for (int ii = 0; ii < line.length; ii++) {
            line[ii] += data[ii];
        }
    }

    public void divideBy(int divisor){
        for (int node : line) {
            node /= divisor;
        }
    }
}
