package com.vernissage.preview.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class LinkPreviewResponse {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("title")
    private String title;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("description")
    private String description;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("image_url")
    private String image;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("original_url")
    private String originalUrl;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("related_links")
    private List<Suggestion> suggestionsList;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("error_code")
    private String errorCode;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("error_message")
    private String errorMessage;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public List<Suggestion> getSuggestionsList() {
        return suggestionsList;
    }

    public void setSuggestionsList(List<Suggestion> suggestionsList) {
        this.suggestionsList = suggestionsList;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
