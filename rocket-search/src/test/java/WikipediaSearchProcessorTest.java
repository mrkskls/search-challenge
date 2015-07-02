import de.mkl.rocket.search.SearchProcessor;
import de.mkl.rocket.search.WikipediaSearchProcessor;

import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.grouping.TopGroups;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WikipediaSearchProcessorTest {

    SearchProcessor searchProcessor;

    @Before
    public void prepare(){

        URL url = ClassLoader.getSystemResource("index");
        try {
            searchProcessor = new WikipediaSearchProcessor(new File(url.getPath()));
        } catch (IOException e) {
            Assert.fail();
        }
    }

    @Test
    public void testWordSearch(){

        String word = "purpose";
        try {
            TopGroups topDocs = searchProcessor.executeWordSearch(word);
            Assert.assertEquals(1, topDocs.totalHitCount);
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void testContributorSearch(){

        String contributor = "mkl";
        try {
            TopDocs topDocs = searchProcessor.executeContributorSearch(contributor);
            Assert.assertTrue(topDocs.totalHits > 0);
        } catch (Exception e) {
            Assert.fail();
        }
    }
}
