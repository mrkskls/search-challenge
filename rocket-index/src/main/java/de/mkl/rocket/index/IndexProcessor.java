package de.mkl.rocket.index;

import java.io.IOException;

public interface IndexProcessor {

    /**
     * Creates a lucene index and returns the number of indexed documents
     *
     * @return the number of indexed documents
     * @throws IOException
     */
    int createIndex() throws IOException;
}
