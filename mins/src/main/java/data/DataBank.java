package data;

import java.util.List;

public interface DataBank {

   public DataLine getNextPoint();

   public DataLine getPoint(int number);

   public List<DataLine> getPoints(int begin, int end);

   public void addPoint(DataLine datum);

   public int getSize();

   public void resetTo(int point);

   public int getPosition();

   public boolean isAtEnd();

   public void clear();
}
