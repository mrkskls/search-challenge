package de.mkl.rocket.search;

import com.beust.jcommander.Parameter;

import java.io.File;
import java.util.List;

public class SearchParameters {

    @Parameter(names = {"--help", "-h"}, help = true)
    boolean help;

    @Parameter(names = {"--word", "-w"})
    String word;

    @Parameter(names = {"--contributor", "-c"})
    String contributor;

    @Parameter(names = {"-index-directory", "-id"}, required = true, description = "the lucene index directory that contains the wikipedia data", validateWith = DirectoryValidator.class)
    File indexDirectory;

    @Parameter
    List<String> additionalContributorTokens;
}
