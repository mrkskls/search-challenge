package de.mkl.rocket.index.wiki.xml.parser;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum WikipediaTags {

    PAGE("page"), TITLE("title"), CONTRIBUTOR_NAME("username"), TEXT("text"), REDIRECT("redirect", "title");

    private String tagName;
    private String attribute;

    private static Map<String, WikipediaTags> tagNameMap;

    static {
        tagNameMap = new HashMap<>();
        for(WikipediaTags tag: WikipediaTags.values()){
            tagNameMap.put(tag.getTagName(), tag);
        }
    }

    WikipediaTags(String tagName) {
        this.tagName = tagName;
    }

    WikipediaTags(String tagName, String attribute) {
        this.tagName = tagName;
        this.attribute = attribute;
    }

    public String getTagName() {
        return tagName;
    }

    public String getAttribute() {
        return attribute;
    }

    public static WikipediaTags getTagByName(String tagName){
        return tagNameMap.get(tagName);
    }
}
