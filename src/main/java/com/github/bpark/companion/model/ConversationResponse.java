package com.github.bpark.companion.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@SuppressWarnings("unused")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConversationResponse {

    private List<PhraseResponse> responses;

    public List<PhraseResponse> getResponses() {
        return responses;
    }
}
