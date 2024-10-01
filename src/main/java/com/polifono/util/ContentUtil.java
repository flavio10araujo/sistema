package com.polifono.util;

import com.polifono.domain.Content;

/**
 * This class has methods to change the content of the classes.
 */
public class ContentUtil {

    public static void main(String args[]) {

        Content original = new Content();

        original.setContent(""
                + "<p><span style=\"font-size:14px\"><span style=\"font-family:Verdana,Geneva,sans-serif\">Apesar de serem de diferentes tamanhos, todos esses tipos de saxofones s&atilde;o tocados da mesma forma com as mesmas posi&ccedil;&otilde;es dos dedos nas notas. Por&eacute;m, pelo fato de serem instrumentos em tonalidades diferentes, o resultado sonoro de cada nota &eacute; diferente. Com o passar dos nossos estudos, entenderemos com mais clareza essas particularidades de cada tipo de saxofone.</span></span></p>"

                + "<p><img alt=\"\" class=\"img-responsive\" src=\"https://media.polifono.com/img/sax_1_1_001_1_01.png\" style=\"height:705px; width:1000px\" /></p>"

                + "<p><span style=\"font-size:14px\"><span style=\"font-family:Verdana,Geneva,sans-serif\"><strong>Aten&ccedil;&atilde;o</strong>: Neste curso, utilizaremos o <u><strong>Saxofone Alto</strong></u>, que tem a afina&ccedil;&atilde;o em Mi bemol. No entanto como dito, voc&ecirc; poder&aacute; treinar os exerc&iacute;cios com qualquer modelo da fam&iacute;lia do saxofone.</span></span></p>"
        );

        //Content resultado = formatContent(original);

        //System.out.println(resultado.getContent());

        //System.out.println(getURLImageFromCompleteTagHTML("<img class=\"img-responsive\" alt=\"\" src=\"https://media.polifono.com/img/sax_1_1_001_1_01.png\" height=\"705\" width=\"1000\" />"));

        addFullScreenToVideo(
                "<div class='embed-container'><iframe src='https://player.vimeo.com/video/179239622' frameborder='0' webkitAllowFullScreen=\"true\" mozallowfullscreen=\"true\" allowFullScreen=\"true\"></iframe></div><p><br />");

        addFullScreenToVideo(
                "<div class=\"embed-container\"><iframe frameborder=\"0\" src=\"https://player.vimeo.com/video/179239622\"></iframe></div><p><br />");
    }

    public static Content formatContent(Content content) {

        if (content == null) {
            return null;
        }

        String formatted = content.getContent();

        String[] imgsOriginal = getImages(formatted);
        String[] imgsFormatted = formatImages(imgsOriginal);

        formatted = replaceImages(formatted, imgsOriginal, imgsFormatted);
        formatted = addClassText(formatted);
        formatted = removeSpaces(formatted);
        formatted = addFullScreenToVideo(formatted);

        content.setContent(formatted);

        return content;
    }

    public static String[] getImages(String content) {
        String htmlImgOpen = "<img ";
        String[] imgs = content.split(htmlImgOpen);

        for (int i = 1; i < imgs.length; i++) {
            imgs[i] = htmlImgOpen + imgs[i].substring(0, imgs[i].indexOf("/>") + 2);
        }

        return imgs;
    }

    public static String[] formatImages(String[] imgsOriginal) {
        String[] imgsFormatted = new String[imgsOriginal.length];

        for (int i = 1; i < imgsOriginal.length; i++) {
            imgsFormatted[i] = formatClassImgResponsive(imgsOriginal[i]);
            imgsFormatted[i] = formatImgWidthAndHeight(imgsFormatted[i]);
            imgsFormatted[i] = addLazyLoadingToImage(addPopupToImage(imgsFormatted[i]));
        }

        return imgsFormatted;
    }

    public static String formatClassImgResponsive(String content) {
        content = content.replaceAll("fr-dib fr-draggable ", "");
        content = content.replaceAll("class=\"img-responsive\" ", "");
        return content.replaceAll("<img ", "<img class=\"img-responsive\" ");
    }

    public static String addClassText(String content) {
        content = addClassFontSize(content);
        return content;
    }

    /**
     * In the text, there are 4 sizes of font-size: 24px, 20px, 16px and 14px.
     * This method will add a different css class for each font-size.
     * Thus, it will be possible to use the functionality increase-decrease text.
     */
    public static String addClassFontSize(String content) {
        content = content.replaceAll("style=\"font-size:24px\"", "class=\"fontSize24px\" style=\"font-size:24px\"");
        content = content.replaceAll("style=\"font-size:20px\"", "class=\"fontSize20px\" style=\"font-size:20px\"");
        content = content.replaceAll("style=\"font-size:16px\"", "class=\"fontSize16px\" style=\"font-size:16px\"");
        content = content.replaceAll("style=\"font-size:14px\"", "class=\"fontSize14px\" style=\"font-size:14px\"");
        return content;
    }

    /**
     * Ex.: style="height:705px; width:1000px" => height="705" width="1000"
     */
    public static String formatImgWidthAndHeight(String content) {
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

            content = content.replace(style, htmlHeight + "" + htmlWidth);
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
    public static String addPopupToImage(String content) {
        String htmlABegin = "<a class=\"image-popup-no-margins\" href=\"X\" title=\"\">";
        htmlABegin = htmlABegin.replace("X", getURLImageFromCompleteTagHTML(content));
        return htmlABegin + content + "</a>";
    }

    /**
     * This method will prepare the image to work with the jquery lazy-loading function.
     * With this, the image will be sent to the user only when the image seen on the screen.
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
    public static String addLazyLoadingToImage(String content) {
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
    public static String getURLImageFromCompleteTagHTML(String tagImg) {
        String img = "";

        int indexSrc = tagImg.indexOf("src=\"");

        if (indexSrc > 0) {
            img = tagImg.substring(indexSrc + 5);
            img = img.substring(0, img.indexOf("\""));
        }

        return img;
    }

    public static String replaceImages(String content, String[] imgsOriginal, String[] imgsFormatted) {
        for (int i = 1; i < imgsOriginal.length; i++) {
            content = content.replace(imgsOriginal[i], imgsFormatted[i]);
        }

        return content;
    }

    public static String removeSpaces(String content) {
        return content.replaceAll("<p>&nbsp;</p>", "");
    }

    /**
     * This method is just to confirm that the video has the properties to be fullscreen.
     */
    public static String addFullScreenToVideo(String content) {

        String iframe = "", newIframe = "";
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
