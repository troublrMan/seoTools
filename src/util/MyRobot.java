package util;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import data.Coords;


/**
 * 鼠标、键盘相关操作
 * @author lyh
 *
 */
public class MyRobot extends Robot {

	public MyRobot() throws AWTException {
		super();
	}

	public void clickMouse(Coords coords) {
		mouseMove(coords.getX(), coords.getY());
		mousePress(InputEvent.BUTTON1_MASK);
		mouseRelease(InputEvent.BUTTON1_MASK);
	}

	public void doubleClickMouse(Coords coords) {
		clickMouse(coords);
		delay(100);
		clickMouse(coords);
		
	}

	/**
	 * 特定区域截图
	 * @param startCoords 左上坐标
	 * @param endCoords	右下坐标
	 * @return BufferedImage
	 */
	public BufferedImage screenShot(Coords startCoords, Coords endCoords) {
		BufferedImage bi = null;
		int width = endCoords.getX() - startCoords.getX();
		int height = endCoords.getY() - startCoords.getY();
		bi = createScreenCapture(new Rectangle(startCoords.getX(), startCoords.getY(), width, height));
		return bi;
	}

	/**
	 * 设置剪贴板内容
	 * @param content
	 */
	public void setClipboardContent(String content) {
		Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
		StringSelection selection = new StringSelection(content);
		clip.setContents(selection, null);
	}

	public void pressKey(int botton) {
		keyPress(botton);
		keyRelease(botton);
	}

	public void pressTwoKey(int botton1, int botton2) {
		keyPress(botton1);
		keyPress(botton2);
		keyRelease(botton1);
		keyRelease(botton2);
	}

	public void pressThreeKey(int botton1, int botton2, int botton3) {
		keyPress(botton1);
		keyPress(botton2);
		keyPress(botton3);
		keyRelease(botton1);
		keyRelease(botton2);
		keyRelease(botton3);
	}

	public void pressEnter() {
		pressKey(KeyEvent.VK_ENTER);
	}
	
	
	public void copy() {
		pressTwoKey(KeyEvent.VK_CONTROL, KeyEvent.VK_C);
	}

	public void selectAll() {
		pressTwoKey(KeyEvent.VK_CONTROL, KeyEvent.VK_A);
	}

	public void paste() {
		pressTwoKey(KeyEvent.VK_CONTROL, KeyEvent.VK_V);
	}
}
