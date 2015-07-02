package de.mkl.rocket.index;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

import java.io.File;

public class InputFileValidator implements IParameterValidator {

    @Override
    public void validate(String s, String wikipediaFile) throws ParameterException {

        File file  = new File(wikipediaFile);
        if(!file.exists()  || !file.isFile()) {
            throw new ParameterException("The wikipedia file you specified does not exist ('" + wikipediaFile + "'))!");
        }
    }
}
