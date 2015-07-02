package de.mkl.rocket.index.wiki.xml.parser;

/**
 * This class represents all elements that will be parsed from the wikipedia xml file.
 */
public class WikiPage {

    private String title;
    private String contributorName;
    private String text;
    private String redirect;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContributorName() {
        return contributorName;
    }

    public void setContributorName(String contributorName) {
        this.contributorName = contributorName;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getRedirect() {
        return redirect;
    }

    public void setRedirect(String redirect) {
        this.redirect = redirect;
    }
}
