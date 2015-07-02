package de.mkl.rocket.index;

import de.mkl.rocket.common.WikipediaAnalyzer;
import de.mkl.rocket.common.IndexedWikipediaField;
import de.mkl.rocket.index.wiki.xml.parser.PageHandler;
import de.mkl.rocket.index.wiki.xml.parser.WikiPage;
import de.mkl.rocket.index.wiki.xml.parser.WikiXMLParser;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.MMapDirectory;
import org.apache.lucene.util.Version;
import org.apache.tools.bzip2.CBZip2InputStream;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * This class can be used to create a lucene index that contains wikipedia pages. Based on a wikipedia xml input file,
 * it will use a SAX parser to create lucene documents that contains the title, contributor name, redirect and the text.
 */
public class WikipediaIndexProcessor implements IndexProcessor {

    private IndexWriter writer;
    private File wikipediaFile;
    private File outputDirectory;

    private static final String BZ2_ENDING = ".bz2";
    private static final int PRINT_STEPS = 10000;
    private static final String ENCODING = "UTF-8";

    public WikipediaIndexProcessor(File wikipediaFile) {
        this.wikipediaFile = wikipediaFile;
    }

    public WikipediaIndexProcessor(File wikipediaFile, File outpurDirectory) {
        this.wikipediaFile = wikipediaFile;
        this.outputDirectory = outpurDirectory;
    }

    private static final String RELATIVE_INDEX_DIRECTORY = "/index";

    @Override
    public int createIndex() throws IOException{

        int indexedDocuments = 0;
        try{
            initializeIndexWriter();
            parseWikipediaFile();
            indexedDocuments = writer.numDocs();
        }
        catch (IOException|SAXException e) {
            e.printStackTrace();
        } finally {
            if(writer != null){
                writer.close();
            }
        }
        return indexedDocuments;
    }

    private void parseWikipediaFile() throws IOException, SAXException {

        PageHandler handler = new WikipediaPageHandler();
        InputStream inputStream = getInputStream();
        Reader reader = new InputStreamReader(inputStream, ENCODING);
        WikiXMLParser wxp = new WikiXMLParser(handler, new InputSource(reader));
        wxp.parse();
        inputStream.close();
        writer.commit();
    }

    private InputStream getInputStream() throws IOException {

        InputStream inputStream = new FileInputStream(wikipediaFile);
        if(wikipediaFile.getAbsolutePath().endsWith(BZ2_ENDING)){
            inputStream.read();
            inputStream.read();
            CBZip2InputStream compressedStream = new CBZip2InputStream(inputStream);
            return compressedStream;
        } else{
            return inputStream;
        }
    }

    private void initializeIndexWriter() throws IOException {

        writer = new IndexWriter(getIndexDirectory(wikipediaFile), getIndexWriterConfig());
    }

    private Directory getIndexDirectory(File wikipediaFile) throws IOException {
        if(outputDirectory == null){
            outputDirectory = new File(wikipediaFile.getParent() + RELATIVE_INDEX_DIRECTORY);
        }
        return MMapDirectory.open(outputDirectory);
    }

    private IndexWriterConfig getIndexWriterConfig(){

        Map<String,Analyzer> analyzerPerField = new HashMap<>();
        analyzerPerField.put(IndexedWikipediaField.CONTRIBUTOR.getFieldName(), new StandardAnalyzer(Version.LUCENE_43));
        PerFieldAnalyzerWrapper analyzer =
                new PerFieldAnalyzerWrapper(new WikipediaAnalyzer(Version.LUCENE_43), analyzerPerField);

        IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_43, analyzer);
        iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        return iwc;
    }

    class WikipediaPageHandler implements PageHandler {

        public void processPage(WikiPage wikiPage) {

            Document document = new Document();
            if(wikiPage.getTitle() != null){
                Field titleField = new StringField(IndexedWikipediaField.TITLE.getFieldName(), wikiPage.getTitle(), Field.Store.YES);
                document.add(titleField);
            }
            if(wikiPage.getContributorName() != null){
                Field contributorField = new TextField(IndexedWikipediaField.CONTRIBUTOR.getFieldName(), wikiPage.getContributorName(), Field.Store.NO);
                document.add(contributorField);
            }
            if(wikiPage.getText() != null){
                Field textField = new TextField(IndexedWikipediaField.TEXT.getFieldName(), wikiPage.getText(), Field.Store.NO);
                document.add(textField);
            }
            String redirect = wikiPage.getRedirect() != null ? wikiPage.getRedirect() : wikiPage.getTitle();
            if(redirect != null){
                Field redirectField = new StringField(IndexedWikipediaField.REDIRECT.getFieldName(), redirect, Field.Store.NO);
                document.add(redirectField);
            }
            try {
                writer.addDocument(document);
                printProgress();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void printProgress(){
        if(writer.numDocs() % PRINT_STEPS == 0){
            System.out.println(writer.numDocs() + " documents processed.");
        }
    }
}


