package com.github.bpark.companion.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PhraseResponse {

    private String subject;

    private String object;

    private String verb;

    private String type;


    public String getSubject() {
        return subject;
    }

    public String getObject() {
        return object;
    }

    public String getVerb() {
        return verb;
    }

    public String getType() {
        return type;
    }

}
