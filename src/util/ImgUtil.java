package util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImgUtil {

	/**
	 * 读取图片文件
	 * 
	 * @param filename
	 * @return BufferImage
	 */
	public static BufferedImage readImg(String filename) {
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File(filename));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return image;
	}

	/**
	 * 逐个像素对比，计算相似度。 两张图片大小必须一样，且图片格式要使用bmp或png等无损压缩格式，有损压缩会因为算法不同造成相似度计算不准确。
	 * 
	 * @param img1
	 * @param img2
	 * @return 相似度
	 */
	public static double calculateSimilarity(BufferedImage img1, BufferedImage img2) {
		double similarity = 0;
		int width = img1.getWidth();
		int height = img1.getHeight();
		int count = 0;
		if (!(width == img2.getWidth()) || !(height == img2.getHeight())) {
			System.out.println("图片大小不一样！");
			return 0;
		}
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				if (img1.getRGB(i, j) == img2.getRGB(i, j)) {
					count++;
				}
			}
		}

		similarity = count * 1.0 / (width * height);
		return similarity;
	}

	public static int[][] getImageRGB(BufferedImage img) {
		int width = img.getWidth();
		int height = img.getHeight();
		int[][] result = new int[height][width];
		for (int h = 0; h < height; h++) {
			for (int w = 0; w < width; w++) {
				result[h][w] = img.getRGB(w, h) & 0xFFFFFF;
			}
		}
		return result;
	}

}
