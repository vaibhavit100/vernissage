package com.vernissage.preview.controller;

import com.vernissage.preview.model.InputURI;
import com.vernissage.preview.util.LinkPreviewConstant;
import com.vernissage.preview.model.LinkPreviewRequest;
import com.vernissage.preview.model.LinkPreviewResponse;
import com.vernissage.preview.service.LinkPreviewService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/", produces ="application/json")
public class LinkPreviewController {

    private static final Logger log = LoggerFactory.getLogger(LinkPreviewController.class);

    @RequestMapping(value = "preview", method = RequestMethod.GET)
    public LinkPreviewResponse getPreview(@RequestParam(value = LinkPreviewConstant.URL, required = false) URI url,
                                          @RequestParam(value = LinkPreviewConstant.ENABLE_SUGGESTIONS, required = false, defaultValue = "false") boolean enableSuggestions,
                                          @RequestParam(value = LinkPreviewConstant.MAXWIDTH, required = false, defaultValue = LinkPreviewConstant.DEFAULT_MAXWIDTH) int maxWidth,
                                          @RequestParam(value = LinkPreviewConstant.MAXHEIGHT, required = false, defaultValue = LinkPreviewConstant.DEFAULT_MAXHEIGHT) int maxHeight,
                                          @RequestParam(value = LinkPreviewConstant.MINWIDTH, required = false, defaultValue = LinkPreviewConstant.DEFAULT_MINWIDTH) int minWidth,
                                          @RequestParam(value = LinkPreviewConstant.MINHEIGHT, required = false, defaultValue = LinkPreviewConstant.DEFAULT_MINHEIGHT) int minHeight) {
        log.info("Get Request for Previewing {}", url);
        LinkPreviewResponse previewResponse = new LinkPreviewResponse();

        if (url == null || StringUtils.isBlank(url.toString())) {
            previewResponse.setErrorCode(HttpStatus.BAD_REQUEST.toString() + " " + "Bad Request");
            previewResponse.setErrorMessage("url cannot be null");
            return previewResponse;
        }
        LinkPreviewRequest linkPreviewRequest = new LinkPreviewRequest.LinkPreviewRequestBuilder(
                    url).maxWidth(maxWidth).maxHeight(maxHeight).minWidth(minWidth).minHeight(minHeight).build();
        LinkPreviewService linkPreviewService = new LinkPreviewService();
        previewResponse = linkPreviewService.getContentPreview(linkPreviewRequest, enableSuggestions);
        return previewResponse;
    }

    @RequestMapping(value = "preview", method = RequestMethod.POST)
    public List<LinkPreviewResponse> getPreviewList(@RequestBody List<InputURI> urlList,
                                                    @RequestParam(value = LinkPreviewConstant.ENABLE_SUGGESTIONS, required = false, defaultValue = "false") boolean enableSuggestions,
                                                    @RequestParam(value = LinkPreviewConstant.MAXWIDTH, required = false, defaultValue = LinkPreviewConstant.DEFAULT_MAXWIDTH) int maxWidth,
                                                    @RequestParam(value = LinkPreviewConstant.MAXHEIGHT, required = false, defaultValue = LinkPreviewConstant.DEFAULT_MAXHEIGHT) int maxHeight,
                                                    @RequestParam(value = LinkPreviewConstant.MINWIDTH, required = false, defaultValue = LinkPreviewConstant.DEFAULT_MINWIDTH) int minWidth,
                                                    @RequestParam(value = LinkPreviewConstant.MINHEIGHT, required = false, defaultValue = LinkPreviewConstant.DEFAULT_MINHEIGHT) int minHeight) {

        List<LinkPreviewResponse> linkPreviewResponseList = new ArrayList<LinkPreviewResponse>();
        LinkPreviewResponse previewResponse = new LinkPreviewResponse();
        if (urlList == null || urlList.isEmpty()) {
            previewResponse.setErrorCode(HttpStatus.BAD_REQUEST.toString() + " " + "Bad Request");
            previewResponse.setErrorMessage("url-list cannot be null");
            linkPreviewResponseList.add(previewResponse);
        } else {
            for (InputURI url : urlList) {
                log.info("Get Request for Previewing {}", urlList);

                if (url.getUrl() == null || StringUtils.isBlank(url.getUrl().toString())) {
                    LinkPreviewResponse previewResponse1 = new LinkPreviewResponse();
                    previewResponse1.setErrorCode(HttpStatus.BAD_REQUEST.toString() + " " + "Bad Request");
                    previewResponse1.setErrorMessage("url cannot be null");
                    linkPreviewResponseList.add(previewResponse1);
                } else {
                    LinkPreviewRequest linkPreviewRequest = new LinkPreviewRequest.LinkPreviewRequestBuilder(
                            url.getUrl()).maxWidth(maxWidth).maxHeight(maxHeight).minWidth(minWidth).minHeight(minHeight).build();
                    LinkPreviewService linkPreviewService = new LinkPreviewService();
                    previewResponse = linkPreviewService.getContentPreview(linkPreviewRequest, enableSuggestions);
                    linkPreviewResponseList.add(previewResponse);
                }
            }
        }
        return linkPreviewResponseList;
    }
}
