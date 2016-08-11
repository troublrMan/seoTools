package test;

import java.awt.image.BufferedImage;
import java.io.UnsupportedEncodingException;


import util.ImgUtil;

public class SimilarityTest {

	public static void main(String[] args) throws UnsupportedEncodingException {

		 BufferedImage img1 = ImgUtil.readImg("picture/testerror.bmp");
		 BufferedImage img2 = ImgUtil.readImg("picture/testsuccess.bmp");
		 BufferedImage img3 = ImgUtil.readImg("picture/emailerror.bmp");
		 System.out.println(ImgUtil.calculateSimilarity(img1, img2));
		 System.out.println(ImgUtil.calculateSimilarity(img1, img3));
		 System.out.println(ImgUtil.calculateSimilarity(img2, img3));
String string =new String();
		System.out.println(string=="");
		
	}

}
