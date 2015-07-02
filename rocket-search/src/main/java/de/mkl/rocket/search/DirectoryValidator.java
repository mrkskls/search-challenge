package de.mkl.rocket.search;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

import java.io.File;

public class DirectoryValidator implements IParameterValidator {

    @Override
    public void validate(String s, String luceneDirectory) throws ParameterException {

        File directory  = new File(luceneDirectory);
        if(!directory.exists()  || !directory.isDirectory()) {
            throw new ParameterException("The lucene directory you specified does not exist ('" + luceneDirectory + "'))!");
        }
    }
}
