package commons;


import data.playback.FileReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;

import java.io.File;


public class ProjectTests extends AbstractTestNGSpringContextTests {

    @Value("dataFile.name")
    protected String dataFileName;

    @Autowired
    protected File dataFile;

    @Autowired
    FileReader dataReader;
}

