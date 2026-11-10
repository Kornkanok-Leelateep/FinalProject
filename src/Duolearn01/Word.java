package Duolearn01;

import java.io.Serializable;

public class Word implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String category;
    private final String english;
    private final String thai;
    private final String example;

    public Word(String category, String english, String thai, String example) {
        this.category = category;
        this.english = english;
        this.thai = thai;
        this.example = example;
    }

    public String getCategory() { return category; }
    public String getEnglish() { return english; }
    public String getThai() { return thai; }
    public String getExample() { return example; }

    @Override
    public String toString() {
        return english + " — " + thai;
    }
}
