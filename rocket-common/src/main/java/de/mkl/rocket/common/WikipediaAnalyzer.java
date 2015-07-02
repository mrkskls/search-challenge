package de.mkl.rocket.common;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.util.StopwordAnalyzerBase;
import org.apache.lucene.analysis.wikipedia.WikipediaTokenizer;
import org.apache.lucene.util.Version;

import java.io.IOException;
import java.io.Reader;


/**
 * This class is used to process wikipedia texts.
 * In contrast to the StandardAnalyzer it uses the WikipediaTokenizer and the PorterStemFilter.
 */
public class WikipediaAnalyzer extends StopwordAnalyzerBase {

    public WikipediaAnalyzer() {
        super(StopAnalyzer.ENGLISH_STOP_WORDS_SET);
    }

    @Override
    protected Analyzer.TokenStreamComponents createComponents(String fieldName, Reader reader) {
        final Tokenizer src = new WikipediaTokenizer(reader);
        StandardFilter standardFilter = new StandardFilter(src);
        LowerCaseFilter lowerCaseFilter = new LowerCaseFilter(standardFilter);
        final StopFilter stopFilter = new StopFilter(lowerCaseFilter, this.stopwords);
        PorterStemFilter porterStemFilter = new PorterStemFilter(stopFilter);
        return new TokenStreamComponents(src, porterStemFilter) {
            protected void setReader(Reader reader) throws IOException {
                super.setReader(reader);
            }
        };
    }
}
