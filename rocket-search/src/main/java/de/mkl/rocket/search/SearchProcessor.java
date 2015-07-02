package de.mkl.rocket.search;

import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.grouping.TopGroups;

import java.util.List;

public interface SearchProcessor {

    /**
     *
     * @param word The word that should be found in the index
     * @return The ranked results that could be found for the input word
     * @throws Exception
     */
    TopGroups executeWordSearch(String word) throws Exception;

    /**
     *
     * @param contributor The name of the contributor that should be found.
     * @return The ranked results that could be found for the contributor
     * @throws Exception
     */
    TopDocs executeContributorSearch(String contributor) throws Exception;
}
