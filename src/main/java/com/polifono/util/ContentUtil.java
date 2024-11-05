package com.polifono.util;

import java.util.Optional;

import com.polifono.domain.Content;

/**
 * This class has methods to change the content of the classes.
 */
public class ContentUtil {

    public static Optional<Content> formatContent(Content content) {
        if (content == null) {
            return Optional.empty();
        }

        String formatted = content.getContent();

        String[] imagesOriginal = getImages(formatted);
        String[] imagesFormatted = formatImages(imagesOriginal);

        formatted = replaceImages(formatted, imagesOriginal, imagesFormatted);
        formatted = addClassText(formatted);
        formatted = removeSpaces(formatted);
        formatted = addFullScreenToVideo(formatted);

        content.setContent(formatted);

        return Optional.of(content);
    }

    private static String[] getImages(String content) {
        String htmlImgOpen = "<img ";
        String[] images = content.split(htmlImgOpen);

        for (int i = 1; i < images.length; i++) {
            images[i] = htmlImgOpen + images[i].substring(0, images[i].indexOf("/>") + 2);
        }

        return images;
    }

    private static String[] formatImages(String[] imagesOriginal) {
        String[] imagesFormatted = new String[imagesOriginal.length];

        for (int i = 1; i < imagesOriginal.length; i++) {
            imagesFormatted[i] = formatClassImgResponsive(imagesOriginal[i]);
            imagesFormatted[i] = formatImgWidthAndHeight(imagesFormatted[i]);
            imagesFormatted[i] = addLazyLoadingToImage(addPopupToImage(imagesFormatted[i]));
        }

        return imagesFormatted;
    }

    private static String formatClassImgResponsive(String content) {
        content = content.replaceAll("fr-dib fr-draggable ", "");
        content = content.replaceAll("class=\"img-responsive\" ", "");
        return content.replaceAll("<img ", "<img class=\"img-responsive\" ");
    }

    private static String addClassText(String content) {
        content = addClassFontSize(content);
        return content;
    }

    /**
     * In the text, there are 4 sizes of font-size: 24px, 20px, 16px and 14px.
     * This method will add a different css class for each font-size.
     * Thus, it will be possible to use the functionality increase-decrease text.
     */
    private static String addClassFontSize(String content) {
        content = content.replaceAll("style=\"font-size:24px\"", "class=\"fontSize24px\" style=\"font-size:24px\"");
        content = content.replaceAll("style=\"font-size:20px\"", "class=\"fontSize20px\" style=\"font-size:20px\"");
        content = content.replaceAll("style=\"font-size:16px\"", "class=\"fontSize16px\" style=\"font-size:16px\"");
        content = content.replaceAll("style=\"font-size:14px\"", "class=\"fontSize14px\" style=\"font-size:14px\"");
        return content;
    }

    /**
     * Ex.: style="height:705px; width:1000px" => height="705" width="1000"
     */
    private static String formatImgWidthAndHeight(String content) {
        String htmlHeight = "height=\"X\" ";
        String htmlWidth = "width=\"X\"";
        String style, height, width;
        int indexStyle, indexHeight, indexWidth;

        indexStyle = content.indexOf("style=\"height:");

        if (indexStyle > 0) {
            style = content.substring(indexStyle);
            style = style.substring(0, style.indexOf("px\"") + 3);

            indexHeight = style.indexOf("height:");
            height = style.substring(indexHeight);
            height = height.substring(7, height.indexOf("px"));

            indexWidth = style.indexOf("width:");
            width = style.substring(indexWidth);
            width = width.substring(6, width.lastIndexOf("px"));

            htmlHeight = htmlHeight.replace("X", height);
            htmlWidth = htmlWidth.replace("X", width);

            content = content.replace(style, htmlHeight + htmlWidth);
        }

        return content;
    }

    /**
     * This method will add a html "a href" around the image.
     * With this, it is possible to click on the image to see it in its original size in a popup.
     * <p>
     * Ex.:
     * From:
     * <img src="https://media.polifono.com/img/recorder_1_1_001_1_01.jpg" width="75" height="75" />
     * To:
     * <a class="image-popup-no-margins" href="https://media.polifono.com/img/recorder_1_1_001_1_01.jpg" title="">
     * <img src="https://media.polifono.com/img/recorder_1_1_001_1_01.jpg" width="75" height="75" />
     * </a>
     */
    private static String addPopupToImage(String content) {
        String htmlABegin = "<a class=\"image-popup-no-margins\" href=\"X\" title=\"\">";
        htmlABegin = htmlABegin.replace("X", getURLImageFromCompleteTagHTML(content));
        return htmlABegin + content + "</a>";
    }

    /**
     * This method will prepare the image to work with the jquery lazy-loading function.
     * With this, the image will be sent to the user only when the image is displayed on the screen.
     * <p>
     * Ex.:
     * From:
     * <a class="image-popup-no-margins" href="https://media.polifono.com/img/recorder_1_1_001_1_01.jpg" title="">
     * <img src="https://media.polifono.com/img/recorder_1_1_001_1_01.jpg" width="75" height="75" />
     * </a>
     * To:
     * <a class="image-popup-no-margins lazy-img" href="https://media.polifono.com/img/recorder_1_1_001_1_01.jpg" title="">
     * <img src="" data-url="https://media.polifono.com/img/recorder_1_1_001_1_01.jpg" width="75" height="75" data-min-width="0" data-max-width="999999" />
     * </a>
     */
    private static String addLazyLoadingToImage(String content) {
        content = content.replace("image-popup-no-margins", "image-popup-no-margins lazy-img");
        content = content.replace("/>", " data-min-width=\"0\" data-max-width=\"999999\" />");

        String src = getURLImageFromCompleteTagHTML(content);

        content = content.replace("src=\"" + src + "\"", "src=\"\"");
        content = content.replace("<img", "<img data-url=\"" + src + "\" ");

        return content;
    }

    /**
     * Ex.:
     * tagImg = <img class="img-responsive" alt="" src="https://media.polifono.com/img/sax_1_1_001_1_01.png" height="705" width="1000" />
     * return = https://media.polifono.com/img/sax_1_1_001_1_01.png
     */
    private static String getURLImageFromCompleteTagHTML(String tagImg) {
        String img = "";

        int indexSrc = tagImg.indexOf("src=\"");

        if (indexSrc > 0) {
            img = tagImg.substring(indexSrc + 5);
            img = img.substring(0, img.indexOf("\""));
        }

        return img;
    }

    private static String replaceImages(String content, String[] imagesOriginal, String[] imagesFormatted) {
        for (int i = 1; i < imagesOriginal.length; i++) {
            content = content.replace(imagesOriginal[i], imagesFormatted[i]);
        }

        return content;
    }

    private static String removeSpaces(String content) {
        return content.replaceAll("<p>&nbsp;</p>", "");
    }

    /**
     * This method is just to confirm that the video has the properties to be fullscreen.
     */
    private static String addFullScreenToVideo(String content) {
        String iframe, newIframe;
        int indexIframe = content.indexOf("<iframe");

        if (indexIframe > 0) {
            iframe = content.substring(indexIframe);
            iframe = iframe.substring(0, iframe.indexOf(">") + 1);

            newIframe = iframe;

            // The video must have these three properties to be fullscreen:
            // webkitAllowFullScreen="true" mozallowfullscreen="true" allowFullScreen="true"
            if (!newIframe.contains("webkitAllowFullScreen")) {
                newIframe = newIframe.replace(">", " webkitAllowFullScreen=\"true\">");
            }

            if (!newIframe.contains("mozallowfullscreen")) {
                newIframe = newIframe.replace(">", " mozallowfullscreen=\"true\">");
            }

            if (!newIframe.contains("allowFullScreen")) {
                newIframe = newIframe.replace(">", " allowFullScreen=\"true\">");
            }

            content = content.replace(iframe, newIframe);
        }

        return content;
    }
}
