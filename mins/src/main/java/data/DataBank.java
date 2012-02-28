package data;

public interface DataBank {

   public DataLine getNextPoint();

   public DataLine getPoint(int number);

    public void addPoint(DataLine datum);
}
