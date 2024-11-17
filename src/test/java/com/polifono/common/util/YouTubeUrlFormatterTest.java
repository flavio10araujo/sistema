package com.polifono.common.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class YouTubeUrlFormatterTest {
    @Test
    public void givenValidUrl_whenFormatUrl_thenReturnFormattedUrl() {
        String expected = "https://www.youtube.com/embed/jCJhLO90DEU";

        Assertions.assertEquals(expected, YouTubeUrlFormatter.formatUrl("https://www.youtube.com/watch?v=jCJhLO90DEU"));
        Assertions.assertEquals(expected, YouTubeUrlFormatter.formatUrl("https://youtu.be/jCJhLO90DEU"));
        Assertions.assertEquals(expected, YouTubeUrlFormatter.formatUrl("https://m.youtube.com/watch?v=jCJhLO90DEU"));
        Assertions.assertEquals(expected, YouTubeUrlFormatter.formatUrl("https://www.youtube.com/watch?v=jCJhLO90DEU&t=13s"));
        Assertions.assertEquals(expected,
                YouTubeUrlFormatter.formatUrl("https://www.youtube.com/watch?v=jCJhLO90DEU&list=RDGMEMJQXQAmqrnmK1SEjY_rKBGAVMIqPATbDhrb4&start_radio=1"));
    }

    @Test
    public void givenEmptyUrl_whenFormatUrl_thenReturnEmptyString() {
        Assertions.assertEquals("", YouTubeUrlFormatter.formatUrl(("")));
        Assertions.assertEquals("", YouTubeUrlFormatter.formatUrl(null));
    }

    @Test
    public void givenInvalidUrl_whenFormatUrl_thenReturnEmptyString() {
        Assertions.assertEquals("", YouTubeUrlFormatter.formatUrl("https://www.youtube.com"));
        Assertions.assertEquals("", YouTubeUrlFormatter.formatUrl("https://www.youtub.com/watch?v=jCJhLO90DEU"));
        Assertions.assertEquals("", YouTubeUrlFormatter.formatUrl("https://www.google.com/watch?v=jCJhLO90DEU"));
    }
}
