package com.polifono.util;

import com.polifono.domain.Content;

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
		
		System.out.println(getURLImageFromCompleteTagHTML("<img class=\"img-responsive\" alt=\"\" src=\"https://media.polifono.com/img/sax_1_1_001_1_01.png\" height=\"705\" width=\"1000\" />"));
	}
	
	public static Content formatContent(Content content) {
        
		String formatted = content.getContent();
		
		String[] imgsOriginal = getImages(formatted);
		String[] imgsFormatted = formatImages(imgsOriginal);
		
		formatted = replaceImages(formatted, imgsOriginal, imgsFormatted);
		
		content.setContent(formatted);
		
		return content;
    }
	
	public static String[] getImages(String content) {
		//System.out.println("getImages - begin");
		String htmlImgOpen = "<img ";
		String[] imgs = content.split(htmlImgOpen);
		
		for (int i = 1; i < imgs.length; i++) {
			imgs[i] = htmlImgOpen + imgs[i].substring(0, imgs[i].indexOf("/>") + 2);
			//System.out.println(imgs[i]);
		}
		
		//System.out.println("getImages - end");
		return imgs;
	}
	
	public static String[] formatImages(String[] imgsOriginal) {
		//System.out.println("formatImages - begin");
		String[] imgsFormatted = new String[imgsOriginal.length];
		
		for (int i = 1; i < imgsOriginal.length; i++) {
			imgsFormatted[i] = formatClassImgResponsive(imgsOriginal[i]);
			imgsFormatted[i] = formatImgWidthAndHeight(imgsFormatted[i]);
			imgsFormatted[i] = addPopupToImage(imgsFormatted[i]);
			//System.out.println(imgsFormatted[i]);
		}
		
		//System.out.println("formatImages - end");
		return imgsFormatted;
	}
	
	public static String formatClassImgResponsive(String content) {
		content = content.replaceAll("fr-dib fr-draggable ", "");
		content = content.replaceAll("class=\"img-responsive\" ", "");
		return content.replaceAll("<img ", "<img class=\"img-responsive\" ");
	}
	
	/**
	 * Ex.: style="height:705px; width:1000px" => height="705" width="1000" 
	 * 
	 * @param content
	 * @return
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
			//System.out.println("Style = " + style);
			
			indexHeight = style.indexOf("height:");
			height = style.substring(indexHeight);
			height = height.substring(7, height.indexOf("px"));
			//System.out.println("Height = " + height);
			
			indexWidth = style.indexOf("width:");
			width = style.substring(indexWidth);
			width = width.substring(6, width.lastIndexOf("px"));
			//System.out.println("Width = " + width);
			
			htmlHeight = htmlHeight.replace("X", height);
			htmlWidth = htmlWidth.replace("X", width);
			
			content = content.replace(style, htmlHeight + "" + htmlWidth);
		}
		
		return content;
	}
	
	/**
	 * This method will add an html "a href" around the image.
	 * With this, it is possible to click on the image to see it in its original size in a popup.
	 * 
	 * Ex.: 
	 * From:
	 * <img src="https://media.polifono.com/img/recorder_1_1_001_1_01.jpg" width="75" height="75" />
	 * To:
	 * <a class="image-popup-vertical-fit" href="https://media.polifono.com/img/recorder_1_1_001_1_01.jpg" title="">
	 * <img src="https://media.polifono.com/img/recorder_1_1_001_1_01.jpg" width="75" height="75" />
	 * </a>
	 * 
	 * @param content
	 * @return
	 */
	public static String addPopupToImage(String content) {
		String htmlABegin = "<a class=\"image-popup-vertical-fit\" href=\"X\" title=\"\">";
		htmlABegin = htmlABegin.replace("X", getURLImageFromCompleteTagHTML(content));
		return htmlABegin + content + "</a>";  
	}
	
	/**
	 * Ex.:
	 * tagImg = <img class="img-responsive" alt="" src="https://media.polifono.com/img/sax_1_1_001_1_01.png" height="705" width="1000" />
	 * return = https://media.polifono.com/img/sax_1_1_001_1_01.png
	 * 
	 * @param tagImg
	 * @return
	 */
	public static String getURLImageFromCompleteTagHTML(String tagImg) {
		// <img class="img-responsive" alt="" src="https://media.polifono.com/img/sax_1_1_001_1_01.png" height="705" width="1000" />
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
}