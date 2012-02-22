package database;

import data.DataBank;
import data.DataLine;
import data.generator.DataGen;
import org.apache.log4j.Logger;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static type.TestType.ALL;
import static type.TestType.DATA;

@Test(groups= { ALL, DATA})
public class DataBankTests{
    
    private DataBank dataBank;
    private DataGen dataGen;
    
    private final int dataSize = 256;
    
    private List<DataLine> dataLines;

    Logger logger = Logger.getLogger(this.getClass());
            
    @BeforeClass
    private void createEntities() {
       dataBank = new DataBank();
       dataGen = new DataGen(dataSize);       
       dataLines = new ArrayList<DataLine>();
    }
    
    
    @Test
    public void dataBankHoldsValues() {
       for( int ii = 0; ii < 1000; ii++) {
           DataLine line = dataGen.getNextLine();
           dataBank.addPoint(line);
           dataLines.add(line);
       }
       
       for( int ii = 0; ii < 1000; ii++) {
            assertEquals(dataBank.getNextPoint(), dataLines.get(ii), "Data At Line " + ii +" Does Not Match");
       }
    }
    
    @Test
    public void dataReadFromFileIsStored() throws IOException {
       // logger.info(dataFile.getName());
//        DataWriter.createFile(dataFile, 256, 1000);

    }
}
