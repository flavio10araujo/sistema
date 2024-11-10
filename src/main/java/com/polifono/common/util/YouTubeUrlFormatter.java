package com.polifono.common.util;

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
        String videoId = "";

        if (url.startsWith("https://www.youtube.com/watch?v=")) {
            videoId = url.substring(url.indexOf("?v=") + 3);
        } else if (url.startsWith("https://youtu.be/")) {
            videoId = url.substring(url.indexOf(".be/") + 4);
        } else if (url.startsWith("https://m.youtube.com/watch?v=")) {
            videoId = url.substring(url.indexOf("?v=") + 3);
        }

        if (videoId.contains("&")) {
            videoId = videoId.substring(0, videoId.indexOf("&"));
        }

        return videoId;
    }
}
