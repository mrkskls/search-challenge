package de.mkl.rocket.index;

import com.beust.jcommander.JCommander;
import sun.security.jca.JCAUtil;

import java.io.IOException;

public class IndexCLI {

    public static void main(String[] args) throws IOException {

        IndexParameters indexParameters = parseParams(args);
        IndexProcessor indexProcessor = new WikipediaIndexProcessor(indexParameters.wikipediaFile, indexParameters.outputDirectory);
        indexProcessor.createIndex();
    }

    private static IndexParameters parseParams(String[] args){

        IndexParameters indexParameters = new IndexParameters();
        JCommander commander = new JCommander(indexParameters, args);
        if(indexParameters.help){
            commander.usage();
            System.exit(0);
        }
        return indexParameters;
    };
}
