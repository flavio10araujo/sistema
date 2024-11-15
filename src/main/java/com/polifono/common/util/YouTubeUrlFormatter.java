package com.polifono.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YouTubeUrlFormatter {

    public static String formatUrl(String url) {
        if (url == null || url.isEmpty()) {
            return "";
        }

        String videoId = extractYoutubeVideoIdFromUrl(url);
        if (videoId.isEmpty()) {
            return "";
        }

        return "https://www.youtube.com/embed/" + videoId;
    }

    private static String extractYoutubeVideoIdFromUrl(String url) {
        if (url == null || url.isEmpty()) {
            return "";
        }

        String pattern = "^(?:https?:\\/\\/)?(?:www\\.|m\\.)?(?:youtube\\.com\\/watch\\?v=|youtu\\.be\\/)([a-zA-Z0-9_-]{11})(?:&.*)?$";
        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(url);

        if (matcher.find()) {
            return matcher.group(1);
        }

        return "";
    }
}
