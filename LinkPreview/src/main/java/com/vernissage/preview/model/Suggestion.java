package com.vernissage.preview.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Suggestion {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("title")
    private String suggestionTitle;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("url")
    private String suggestionUrl;

    public String getSuggestionTitle() {
        return suggestionTitle;
    }

    public void setSuggestionTitle(String suggestionTitle) {
        this.suggestionTitle = suggestionTitle;
    }

    public String getSuggestionUrl() {
        return suggestionUrl;
    }

    public void setSuggestionUrl(String suggestionUrl) {
        this.suggestionUrl = suggestionUrl;
    }
}
