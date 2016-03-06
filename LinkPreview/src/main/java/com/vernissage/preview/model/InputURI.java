package com.vernissage.preview.model;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.net.URI;

public class InputURI {

    private final URI url;

    @JsonCreator
    public InputURI(@JsonProperty("url") URI url) {
        this.url = url;
    }

    public URI getUrl() {
        return url;
    }
}
