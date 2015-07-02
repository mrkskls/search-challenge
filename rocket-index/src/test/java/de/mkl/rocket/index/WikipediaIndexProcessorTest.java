package de.mkl.rocket.index;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class WikipediaIndexProcessorTest {

    IndexProcessor indexProcessor;

    @Before
    public void prepare(){

        URL url = ClassLoader.getSystemResource("test.xml");
        indexProcessor = new WikipediaIndexProcessor(new File(url.getPath()));
    }

    @Test
    public void test() {

        try {
            int indexedDocuments = indexProcessor.createIndex();
            Assert.assertEquals(1, indexedDocuments);
        } catch (IOException e) {
            Assert.fail();
        }
    }
}
