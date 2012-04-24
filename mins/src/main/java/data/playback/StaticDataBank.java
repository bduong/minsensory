package data.playback;

import data.DataBank;
import data.DataLine;

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
    public List<DataLine> getPoints(int begin, int end) {
        return data.subList(begin, end);
    }

    @Override
    public void addPoint(DataLine datum) {
        data.add(datum);
    }

    @Override
    public int getSize() {
        return data.size();
    }

    @Override
    public void resetTo(int point) {
        count = point;
    }

    @Override
    public int getPosition() {
        return count;
    }

    @Override
    public boolean isAtEnd() {
        return count >= data.size()-1;
    }

    @Override
    public void clear() {
        data.clear();
    }
}
