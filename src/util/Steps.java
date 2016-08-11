package util;

import java.awt.AWTException;
import java.awt.event.KeyEvent;

import data.Coords;

/**
 * 常用步骤封装
 * 
 * @author lyh
 *
 */
public class Steps {
	// 每个操作之间的间隔
	public static int TIME = 1500;
	public static MyRobot mr;

	static {
		try {
			mr = new MyRobot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}

	public static void mouseClick(Coords coords) {
		mr.delay(TIME);
		mr.clickMouse(coords);
	}

	public static void mouseDoubleClick(Coords coords) {
		mr.delay(TIME);
		mr.doubleClickMouse(coords);
	}

	/**
	 * 根据文件名打开文件。
	 * 
	 * @param coords
	 * @param filename
	 */
	public static void openFile(Coords coords, String filename) {
		mouseClick(coords);
		mr.setClipboardContent(filename);
		mr.delay(TIME);
		mr.paste();
		mr.delay(TIME);
		mr.pressEnter();
	}

	/**
	 * 全选然后清除内容
	 * 
	 * @param coords
	 */
	public static void clearContent(Coords coords) {
		mr.delay(TIME);
		mr.clickMouse(coords);
		mr.selectAll();
		mr.pressKey(KeyEvent.VK_BACK_SPACE);
	}

	/**
	 * 将剪切板内容粘贴上
	 * 
	 * @param coords
	 * @param content
	 */
	public static void inputContent(Coords coords, String content) {
		mr.delay(TIME);
		mr.clickMouse(coords);
		mr.setClipboardContent(content);
		mr.paste();
	}

	/**
	 * 将传入的 int 值用键盘敲出来。 用于XCR中的schedule设置，只能接受键盘输入，不能复制粘贴。
	 * 正好每个数字的键值等于ASCLL值，不用转换。
	 * 
	 * @param coords
	 * @param value
	 */
	public static void setIntValue(Coords coords, int value) {
		mouseClick(coords);
		String string = String.valueOf(value);
		for (int i = 0; i < string.length(); i++) {
			mr.pressKey(string.charAt(i));
			mr.delay(100);
		}
	}

	/**
	 * 用键盘清除内容。 用于XCR中删除旧的AnchorText，只能键盘操作，不能进行全选后删除。 暂定只删除 3*50个字符。
	 */
	public static void clearContentWithKeyboard(Coords coords) {
		for (int i = 0; i < 3; i++) {
			mouseClick(coords);
			for (int j = 0; j < 50; j++) {
				mr.pressKey(KeyEvent.VK_BACK_SPACE);
			}
		}
	}

	/**
	 * 选择特定的projects，使用Shit键连续选择。
	 * 
	 * @param start
	 * @param end
	 * @param cut
	 */
	public static void selectProjectShift(Coords start, Coords end) {
		mouseClick(start);
		mr.keyPress(KeyEvent.VK_SHIFT);
		mouseClick(end);
		mr.keyRelease(KeyEvent.VK_SHIFT);
	}
	
	/**
	 * 选择特定的projects，使用Ctrl键选择多个。
	 * 
	 * @param coordses 
	 */
	public static void selectProjectCtrl(Coords[] coordses) {
		mr.keyPress(KeyEvent.VK_CONTROL);
		for(Coords coords:coordses){
			mouseClick(coords);
		}
		mr.keyRelease(KeyEvent.VK_CONTROL);
	}
}
