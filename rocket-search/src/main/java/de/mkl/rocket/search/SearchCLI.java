package de.mkl.rocket.search;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;

public class SearchCLI {

    public static void main(String[] args) throws Exception {

        SearchParameters searchParameters = parseParams(args);
        validateParams(searchParameters);
        run(searchParameters);
    }

    private static SearchParameters parseParams(String[] args){

        SearchParameters searchParameters = new SearchParameters();
        JCommander commander = new JCommander(searchParameters, args);
        if(searchParameters.help){
            commander.usage();
            System.exit(0);
        };
        return  searchParameters;
    }

    private static void validateParams(SearchParameters searchParameters){
        if((searchParameters.contributor == null && searchParameters.word == null) || (searchParameters.contributor != null && searchParameters.word != null)) {
            throw new ParameterException("You have to specify either a contributor or a word to execute a search!");
        }
    }

    private static void run(SearchParameters searchParameters) throws Exception {
        SearchProcessor searchProcessor = new WikipediaSearchProcessor(searchParameters.indexDirectory);
        if(searchParameters.word != null){
            searchProcessor.executeWordSearch(searchParameters.word);
        } else {
            searchProcessor.executeContributorSearch(getContributor(searchParameters));
        }
    }

    private static String getContributor(SearchParameters searchParameters){

        if(searchParameters.additionalContributorTokens != null){
            return searchParameters.contributor + " " + String.join(" " , searchParameters.additionalContributorTokens);
        } else{
            return searchParameters.contributor;
        }
    }
}
