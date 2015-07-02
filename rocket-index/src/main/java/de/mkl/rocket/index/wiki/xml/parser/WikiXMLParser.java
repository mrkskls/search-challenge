package de.mkl.rocket.index.wiki.xml.parser;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.IOException;

/**
 * This parser is used to extract the wikipedia titles, contributor names, redirects and texts.
 */
public class WikiXMLParser extends DefaultHandler {

    protected WikiPage currentPage;
    private StringBuilder currentText = new StringBuilder();
    private PageHandler pageHandler;

    private XMLReader reader;
    private InputSource inputSource;

    public WikiXMLParser(PageHandler pageHandler, InputSource inputSource) throws SAXException {

        this.pageHandler = pageHandler;
        this.inputSource = inputSource;
        reader = XMLReaderFactory.createXMLReader();
        reader.setContentHandler(this);
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attr){

        WikipediaTags wikipediaTag = WikipediaTags.getTagByName(qName);
        if(wikipediaTag != null){
            if(wikipediaTag.equals(WikipediaTags.PAGE)){
                currentPage = new WikiPage();
            } else if(wikipediaTag.equals(WikipediaTags.REDIRECT)){
                currentPage.setRedirect(attr.getValue(WikipediaTags.REDIRECT.getAttribute()));
            }
        }
        currentText = null;
    }

    @Override
    public void endElement(String uri, String name, String qName){

        WikipediaTags wikipediaTag = WikipediaTags.getTagByName(qName);
        if(wikipediaTag != null){
            switch (wikipediaTag){
                case PAGE:
                    pageHandler.processPage(currentPage);
                    break;
                case TITLE:
                    currentPage.setTitle(getText());
                    break;
                case CONTRIBUTOR_NAME:
                    currentPage.setContributorName(getText());
                    break;
                case TEXT:
                    currentPage.setText(getText());
                    break;
            }
        }
        currentText = null;
    }

    private String getText(){
        return currentText != null ? currentText.toString() : null;
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (currentText == null) {
            currentText = new StringBuilder(length);
        }
        currentText.append(ch, start, length);
    }

    public void parse() throws IOException, SAXException {
        reader.parse(inputSource);
    }
}
