package de.mkl.rocket.index;

import com.beust.jcommander.Parameter;

import java.io.File;


public class IndexParameters {

    @Parameter(names = {"--help", "-h"}, help = true)
    boolean help;

    @Parameter(names = {"--wikipedia-file", "-wf"}, required = true, description = "The wikipedia input file (xml or bz2)", validateWith = InputFileValidator.class)
    File wikipediaFile;

    @Parameter(names = {"--output-directory", "-od"}, description = "The directory where the index will be created. If it is not specified, it will be created in the wikipedia input directory.")
    File outputDirectory;
}
