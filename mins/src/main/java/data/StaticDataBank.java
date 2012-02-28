package data;

import java.util.ArrayList;
import java.util.List;

public class StaticDataBank implements DataBank {

    private List<DataLine> data;
    private int count;

    public StaticDataBank() {
        data = new ArrayList<DataLine>();
        count = 0;
    }

    @Override
    public DataLine getNextPoint() {
        return data.get(count++);
    }

    @Override
    public DataLine getPoint(int number) {
        return data.get(number);
    }

    @Override
    public void addPoint(DataLine datum) {
        data.add(datum);
    }
}
