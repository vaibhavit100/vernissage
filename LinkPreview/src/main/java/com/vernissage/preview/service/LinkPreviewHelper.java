package com.vernissage.preview.service;

import com.vernissage.preview.util.LinkPreviewConstant;
import com.vernissage.preview.model.LinkPreviewRequest;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class LinkPreviewHelper {

    //To find image and description for popular sites like youtube, dailymotion, twitter etc
    protected static String getMetaTag(Document document, String attribute) {
        Elements metaElements = document.select("meta[name=" + attribute + "]");
        String metaName = getAttributes(metaElements);
        if (checkParam(metaName)) {
            return metaName;
        }
        String ogAttribute = LinkPreviewConstant.OG + attribute;
        metaElements = document.select("meta[property=" + ogAttribute + "]");
        String metaProperty = getAttributes(metaElements);
        if (checkParam(metaProperty)) {
            return metaProperty;
        }
        return null;
    }

    // To find image for other sites
    protected static String getImageTag(Document document, String attribute, LinkPreviewRequest linkPreviewRequest) throws IOException {
        Elements images = document.getElementsByTag(attribute);
        for (Element element : images) {
            if (checkParam(element.attr(LinkPreviewConstant.ABSOLUTE_URL)) && validateImageDimensions(element, LinkPreviewConstant.ABSOLUTE_URL, linkPreviewRequest)) {
                return element.attr(LinkPreviewConstant.ABSOLUTE_URL);
            }
        }
        return null;
    }

    // To validate image dimensions as per request
    private static boolean validateImageDimensions(Element element, String attribute, LinkPreviewRequest linkPreviewRequest) throws IOException {
        BufferedImage image = ImageIO.read(new URL(element.attr(attribute)));
        return checkParam(image) && validateWidth(linkPreviewRequest, image) && validateHeight(linkPreviewRequest, image);
    }


    // Validating image width
    private static boolean validateWidth(LinkPreviewRequest contentPreviewRequest, BufferedImage image) {
        return image.getWidth() >= contentPreviewRequest.getMinWidth() && image.getWidth() < contentPreviewRequest.getMaxWidth() ? true : false;
    }

    // Validating image height
    private static boolean validateHeight(LinkPreviewRequest linkPreviewRequest, BufferedImage image) {
        return image.getHeight() >= linkPreviewRequest.getMinHeight() && image.getHeight() < linkPreviewRequest.getMaxHeight() ? true : false;

    }

    private static boolean checkParam(Object paramName) {
        return !(paramName == null || paramName == "");

    }

    // Helper method
    private static String getAttributes(Elements elements) {
        for (Element metaNameElement : elements) {
            if (checkParam(metaNameElement.attr(LinkPreviewConstant.CONTENT))) {
                return metaNameElement.attr(LinkPreviewConstant.CONTENT);
            }
        }
        return null;
    }


}