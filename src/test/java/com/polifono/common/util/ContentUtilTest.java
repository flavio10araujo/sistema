package com.polifono.common.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.polifono.model.entity.Content;

public class ContentUtilTest {

    @Test
    public void givenNullInput_WhenFormatContent_ThenReturnEmptyString() {
        assertEquals(ContentUtil.formatContent(null), Optional.empty());
    }

    @Test
    public void givenContent_WhenFormatContent_ThenReturnFormattedContent() {
        Content content = Content.builder().content(getContentOriginal()).build();
        Content expected = Content.builder().content(getContentFormatted()).build();
        Optional<Content> actual = ContentUtil.formatContent(content);
        assertEquals(expected.getContent(), actual.get().getContent());
    }

    private String getContentOriginal() {
        return """
                <p>
                <span style="font-size:24px"><strong><span style="font-family:Verdana,Geneva,sans-serif">1.&nbsp;&nbsp;&nbsp;FASE 1 &ndash; Nota&ccedil;&atilde;o Musical</span></strong></span>
                </p>
                <div class="embed-container"><iframe frameborder="0" src="https://player.vimeo.com/video/179239622"></iframe></div>
                <p><br />
                <span style="font-size:20px"><span style="font-family:Verdana,Geneva,sans-serif"><strong>1.1&nbsp;&nbsp;&nbsp;Nota&ccedil;&atilde;o Musical</strong></span></span><br /><br />
                <span style="font-size:14px"><span style="font-family:Verdana,Geneva,sans-serif">S&atilde;o todos os sinais gr&aacute;ficos que representam a escrita musical.</span></span>
                </p>
                <p>
                <strong><span style="font-size:16px"><span style="font-family:Verdana,Geneva,sans-serif">1.1.1&nbsp;&nbsp; &nbsp;Sons musicais e nome das notas</span></span></strong><br /><br />
                <span style="font-size:14px"><span style="font-family:Verdana,Geneva,sans-serif">S&atilde;o os sons utilizados para fazer m&uacute;sica.</span></span>
                </p>
                <p>
                <span style="font-size:14px"><span style="font-family:Verdana,Geneva,sans-serif">Som grave: Um som dito grave &eacute; um som de baixa frequ&ecirc;ncia da audi&ccedil;&atilde;o humana.<br />
                Som agudo: Um som dito agudo &eacute; um som de alta frequ&ecirc;ncia da audi&ccedil;&atilde;o humana.<br /></span></span><br /><br />
                <span style="font-size:14px"><span style="font-family:Verdana,Geneva,sans-serif">O nome de cada nota musical foi dado por Guido Darezzo (992-1050):</span></span><br /><br />
                <span style="font-size:14px"><span style="font-family:Verdana,Geneva,sans-serif"><strong>Ut</strong>queant laxis &ndash; A s&iacute;laba &quot;Ut&quot; por ser de dif&iacute;cil dic&ccedil;&atilde;o foi trocada por &quot;D&oacute;&quot;<br /></span></span>
                </p>
                <p>
                <span style="font-size:14px"><span style="font-family:Verdana,Geneva,sans-serif"><img class="fr-dib fr-draggable img-responsive" src="https://media.polifono.com/img/recorder_1_1_001_1_01.jpg" /></span></span>
                </p>
                <p>
                <span style="font-size:14px"><span style="font-family:Verdana,Geneva,sans-serif">A Flauta Doce se divide em tr&ecirc;s partes:<br />
                &bull;&nbsp;&nbsp; &nbsp;Cabe&ccedil;a: &eacute; a parte que o m&uacute;sico assopra. Na cabe&ccedil;a temos a Janela, o L&aacute;bio e o Bocal;<br />
                </p>
                <p><span style="font-size:14px"><span style="font-family:Verdana,Geneva,sans-serif">Aten&ccedil;&atilde;o: Nesse curso utilizaremos a Flauta Soprano.</span></span></p>
                <img class="fr-dib fr-draggable img-responsive" src="https://media.polifono.com/img/imagem_test_001.jpg" />
                """;
    }

    private String getContentFormatted() {
        return """
                <p>
                <span class="fontSize24px" style="font-size:24px"><strong><span style="font-family:Verdana,Geneva,sans-serif">1.&nbsp;&nbsp;&nbsp;FASE 1 &ndash; Nota&ccedil;&atilde;o Musical</span></strong></span>
                </p>
                <div class="embed-container"><iframe frameborder="0" src="https://player.vimeo.com/video/179239622" webkitAllowFullScreen="true" mozallowfullscreen="true" allowFullScreen="true"></iframe></div>
                <p><br />
                <span class="fontSize20px" style="font-size:20px"><span style="font-family:Verdana,Geneva,sans-serif"><strong>1.1&nbsp;&nbsp;&nbsp;Nota&ccedil;&atilde;o Musical</strong></span></span><br /><br />
                <span class="fontSize14px" style="font-size:14px"><span style="font-family:Verdana,Geneva,sans-serif">S&atilde;o todos os sinais gr&aacute;ficos que representam a escrita musical.</span></span>
                </p>
                <p>
                <strong><span class="fontSize16px" style="font-size:16px"><span style="font-family:Verdana,Geneva,sans-serif">1.1.1&nbsp;&nbsp; &nbsp;Sons musicais e nome das notas</span></span></strong><br /><br />
                <span class="fontSize14px" style="font-size:14px"><span style="font-family:Verdana,Geneva,sans-serif">S&atilde;o os sons utilizados para fazer m&uacute;sica.</span></span>
                </p>
                <p>
                <span class="fontSize14px" style="font-size:14px"><span style="font-family:Verdana,Geneva,sans-serif">Som grave: Um som dito grave &eacute; um som de baixa frequ&ecirc;ncia da audi&ccedil;&atilde;o humana.<br />
                Som agudo: Um som dito agudo &eacute; um som de alta frequ&ecirc;ncia da audi&ccedil;&atilde;o humana.<br /></span></span><br /><br />
                <span class="fontSize14px" style="font-size:14px"><span style="font-family:Verdana,Geneva,sans-serif">O nome de cada nota musical foi dado por Guido Darezzo (992-1050):</span></span><br /><br />
                <span class="fontSize14px" style="font-size:14px"><span style="font-family:Verdana,Geneva,sans-serif"><strong>Ut</strong>queant laxis &ndash; A s&iacute;laba &quot;Ut&quot; por ser de dif&iacute;cil dic&ccedil;&atilde;o foi trocada por &quot;D&oacute;&quot;<br /></span></span>
                </p>
                <p>
                <span class="fontSize14px" style="font-size:14px"><span style="font-family:Verdana,Geneva,sans-serif"><a class="image-popup-no-margins lazy-img" href="https://media.polifono.com/img/recorder_1_1_001_1_01.jpg" title=""><img data-url="https://media.polifono.com/img/recorder_1_1_001_1_01.jpg"  class="img-responsive" src=""  data-min-width="0" data-max-width="999999" /></a></span></span>
                </p>
                <p>
                <span class="fontSize14px" style="font-size:14px"><span style="font-family:Verdana,Geneva,sans-serif">A Flauta Doce se divide em tr&ecirc;s partes:<br />
                &bull;&nbsp;&nbsp; &nbsp;Cabe&ccedil;a: &eacute; a parte que o m&uacute;sico assopra. Na cabe&ccedil;a temos a Janela, o L&aacute;bio e o Bocal;<br />
                </p>
                <p><span class="fontSize14px" style="font-size:14px"><span style="font-family:Verdana,Geneva,sans-serif">Aten&ccedil;&atilde;o: Nesse curso utilizaremos a Flauta Soprano.</span></span></p>
                <a class="image-popup-no-margins lazy-img" href="https://media.polifono.com/img/imagem_test_001.jpg" title=""><img data-url="https://media.polifono.com/img/imagem_test_001.jpg"  class="img-responsive" src=""  data-min-width="0" data-max-width="999999" /></a>
                """;
    }
}
