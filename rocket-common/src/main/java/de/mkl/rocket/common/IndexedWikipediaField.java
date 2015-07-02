package de.mkl.rocket.common;

public enum IndexedWikipediaField {

    TITLE("title"), CONTRIBUTOR("contributor"), TEXT("text"), REDIRECT("redirect");

    IndexedWikipediaField(String fieldName) {

        this.fieldName = fieldName;
    }

    private String fieldName;

    public String getFieldName() {

        return fieldName;
    }
}
