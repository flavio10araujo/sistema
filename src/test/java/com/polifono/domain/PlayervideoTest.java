package com.polifono.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PlayervideoTest {

    @Test
    public void givenPlayerVideo_whenGetUrlFormatted_thenReturnFormattedUrl() {
        String expected = "https://www.youtube.com/embed/jCJhLO90DEU";
        Playervideo playervideo = new Playervideo();

        playervideo.setUrl("https://www.youtube.com/watch?v=jCJhLO90DEU");
        Assertions.assertEquals(expected, playervideo.getUrlFormatted());

        playervideo.setUrl("https://youtu.be/jCJhLO90DEU");
        Assertions.assertEquals(expected, playervideo.getUrlFormatted());

        playervideo.setUrl("https://m.youtube.com/watch?v=jCJhLO90DEU");
        Assertions.assertEquals(expected, playervideo.getUrlFormatted());

        playervideo.setUrl("https://www.youtube.com/watch?v=jCJhLO90DEU&t=13s");
        Assertions.assertEquals(expected, playervideo.getUrlFormatted());

        playervideo.setUrl("https://www.youtube.com/watch?v=jCJhLO90DEU&list=RDGMEMJQXQAmqrnmK1SEjY_rKBGAVMIqPATbDhrb4&start_radio=1");
        Assertions.assertEquals(expected, playervideo.getUrlFormatted());
    }

    @Test
    public void givenPlayerVideoWithoutUrl_whenGetUrlFormatted_thenReturnEmptyString() {
        Playervideo playervideo = new Playervideo();

        playervideo.setUrl("");
        Assertions.assertEquals("", playervideo.getUrlFormatted());

        playervideo.setUrl(null);
        Assertions.assertEquals("", playervideo.getUrlFormatted());
    }

    @Test
    public void givenPlayerVideoWithInvalidUrl_whenGetUrlFormatted_thenReturnEmptyString() {
        Playervideo playervideo = new Playervideo();

        playervideo.setUrl("https://www.youtube.com");
        Assertions.assertEquals("", playervideo.getUrlFormatted());

        playervideo.setUrl("https://www.youtub.com/watch?v=jCJhLO90DEU");
        Assertions.assertEquals("", playervideo.getUrlFormatted());

        playervideo.setUrl("https://www.google.com/watch?v=jCJhLO90DEU");
        Assertions.assertEquals("", playervideo.getUrlFormatted());
    }
}
