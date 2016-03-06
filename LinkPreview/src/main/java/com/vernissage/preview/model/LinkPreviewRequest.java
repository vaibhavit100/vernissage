package com.vernissage.preview.model;

import java.net.URI;

public class LinkPreviewRequest {

    private final URI url;
    private final int maxWidth;
    private final int maxHeight;
    private final int minWidth;
    private final int minHeight;


    private LinkPreviewRequest(LinkPreviewRequestBuilder builder) {
        this.url = builder.url;
        this.maxWidth = builder.maxWidth;
        this.maxHeight = builder.maxHeight;
        this.minWidth = builder.minWidth;
        this.minHeight = builder.minHeight;
    }

    public URI getUrl() {
        return url;
    }

    public int getMaxWidth() {
        return maxWidth;
    }

    public int getMaxHeight() {
        return maxHeight;
    }

    public int getMinWidth() {
        return minWidth;
    }

    public int getMinHeight() {
        return minHeight;
    }

    public static class LinkPreviewRequestBuilder {
        //Required Parameter
        private final URI url;
        private int maxWidth;
        private int maxHeight;
        private int minWidth;
        private int minHeight;

        public LinkPreviewRequestBuilder(URI url) {
            this.url = url;
        }

        public LinkPreviewRequestBuilder maxWidth(int value) {
            this.maxWidth = value;
            return this;
        }

        public LinkPreviewRequestBuilder maxHeight(int value) {
            this.maxHeight = value;
            return this;
        }

        public LinkPreviewRequestBuilder minWidth(int minWidth) {
            this.minWidth = minWidth;
            return this;
        }

        public LinkPreviewRequestBuilder minHeight(int value) {
            this.minHeight = value;
            return this;
        }

        public LinkPreviewRequest build() {
            return new LinkPreviewRequest(this);
        }
    }
}