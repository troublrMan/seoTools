package preprocessor;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import data.XCRCoords;
import util.MyRobot;

public class ScreenShot {

	/**
	 * 
	 * 屏幕截图，供计算相似度使用
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		Thread.sleep(3000);
		MyRobot mr = new MyRobot();
		BufferedImage img = mr.screenShot(XCRCoords.MSG_BOX_START, XCRCoords.MSG_BOX_END);
		ImageIO.write(img, "bmp", new File("save.bmp"));
		System.out.println("OK");
	}
}
