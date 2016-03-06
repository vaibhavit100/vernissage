package com.vernissage.preview.service;

import com.google.gson.Gson;
import com.vernissage.preview.model.LinkPreviewRequest;
import com.vernissage.preview.model.LinkPreviewResponse;
import com.vernissage.preview.model.Suggestion;
import com.vernissage.preview.model.SuggestionResults;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import static com.vernissage.preview.util.LinkPreviewConstant.*;

public class LinkPreviewService {

    private static final Logger log = LoggerFactory.getLogger(LinkPreviewService.class);

    public LinkPreviewResponse getContentPreview(LinkPreviewRequest linkPreviewRequest, boolean enableSuggestions) {
        LinkPreviewResponse linkPreviewResponse = new LinkPreviewResponse();
        if (linkPreviewRequest == null || linkPreviewRequest.getUrl().toString().length() == 0) {
            linkPreviewResponse.setErrorCode(HttpStatus.BAD_REQUEST.toString());
            linkPreviewResponse.setErrorMessage("url cannot be null");
            return linkPreviewResponse;
        }
        try {
            String contentPreviewUrl = linkPreviewRequest.getUrl().toString();
            log.debug("Requested URL is - {}", contentPreviewUrl);
            Response response = Jsoup.connect(contentPreviewUrl)
                    .ignoreContentType(true)
                    .userAgent(USER_AGENT)
                    .referrer(REFERRER)
                    .timeout((int) TIME_OUT)
                    .followRedirects(true)
                    .execute();
            Document document = response.parse();
            if (document != null) {
                String title = document.title();
                String thumbnail = LinkPreviewHelper.getMetaTag(document, IMAGE);
                if (StringUtils.isBlank(thumbnail)) {
                    thumbnail = LinkPreviewHelper.getImageTag(document, IMG, linkPreviewRequest);
                }
                String description = LinkPreviewHelper.getMetaTag(document, DESCRIPTION);
                if (StringUtils.isBlank(description)) {
                    description = title;
                }
                linkPreviewResponse.setOriginalUrl(contentPreviewUrl);
                linkPreviewResponse.setTitle(title);
                linkPreviewResponse.setDescription(description);
                linkPreviewResponse.setImage(thumbnail);
                if (enableSuggestions) {
                    linkPreviewResponse.setSuggestionsList(getSuggestionsForUrl(title, contentPreviewUrl));
                }
            }
            return linkPreviewResponse;
        } catch (IllegalArgumentException ex) {
            LinkPreviewResponse linkPreviewResponse1 = new LinkPreviewResponse();
            log.error("Error occurred while generating content preview - {}", ex);
            linkPreviewResponse1.setErrorCode(HttpStatus.BAD_REQUEST.toString() + " " + "Bad Request");
            linkPreviewResponse1.setErrorMessage("URL is malformed");
            return linkPreviewResponse1;
        } catch (UnknownHostException ex) {
            LinkPreviewResponse linkPreviewResponse2 = new LinkPreviewResponse();
            log.error("Error occurred while generating content preview - {}", ex);
            linkPreviewResponse2.setErrorCode(HttpStatus.BAD_REQUEST.toString() + " " + "Bad Request");
            linkPreviewResponse2.setErrorMessage("url host is not known");
            return linkPreviewResponse2;
        } catch (IOException ioException) {
            LinkPreviewResponse linkPreviewResponse3 = new LinkPreviewResponse();
            log.error("Error occurred while generating content preview - {}", ioException);
            linkPreviewResponse3.setErrorCode(HttpStatus.SERVICE_UNAVAILABLE.toString() + " " + "Service Unavailable");
            linkPreviewResponse3.setErrorMessage("error occured while generating preview");
            return linkPreviewResponse3;
        }
    }

    private List<Suggestion> getSuggestionsForUrl(String queryString, String contentPreviewUrl) throws IOException {
        List<Suggestion> suggestionList = new ArrayList<Suggestion>();

        URL url = new URL(SUGGESTION_SEARCH + URLEncoder.encode(queryString, ENCODING_CHARSET));

        Reader reader = new InputStreamReader(url.openStream(), ENCODING_CHARSET);
        SuggestionResults results = new Gson().fromJson(reader, SuggestionResults.class);
        int size = results.getResponseData().getResults().size();
        if (!results.getResponseData().getResults().isEmpty()) {
            for (int i = 0; i < size; i++) {
                Suggestion suggestion = new Suggestion();
                String title;
                String decodedUrl = URLDecoder.decode(results.getResponseData().getResults().get(i).getUrl());
                if (!decodedUrl.equals(contentPreviewUrl)) {
                    title = Jsoup.parse(results.getResponseData().getResults().get(i).getTitle()).text();
                    suggestion.setSuggestionTitle(title);
                    suggestion.setSuggestionUrl(decodedUrl);
                    suggestionList.add(suggestion);
                }
            }
        }
        return suggestionList;
    }
}