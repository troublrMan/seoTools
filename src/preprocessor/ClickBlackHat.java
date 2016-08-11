package preprocessor;

import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.InputEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 将小黑帽放在屏幕右上角，自动点击提取其库中的内容。
 * 用于生成title和bio。
 * 运行前需要修改坐标。
 * @author lyh
 *
 */
public class ClickBlackHat {

	public static void main(String[] args) throws Exception {

		for (int i = 0; i < 2000; i++) {
			Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
			StringSelection selection = new StringSelection(null);
			clip.setContents(selection, null);
			Robot robot = new Robot();
			clickGenerate(robot);
			Thread.sleep(50);
			clickCopy(robot);
			Thread.sleep(50);
			Transferable clipT = clip.getContents(null);
			if (clipT != null && clipT.isDataFlavorSupported(DataFlavor.stringFlavor)) {
				String content = (String) clipT.getTransferData((DataFlavor.stringFlavor));
				System.out.println(content);
				dealExcel(content, "name.xlsx");
			}
		}
	}

	private static void clickCopy(Robot robot) {
		robot.mouseMove(1400, 300);
		robot.mousePress(InputEvent.BUTTON1_MASK);
		robot.mouseRelease(InputEvent.BUTTON1_MASK);
	}

	private static void clickGenerate(Robot robot) {
		robot.mouseMove(1230, 300);
		robot.mousePress(InputEvent.BUTTON1_MASK);
		robot.mouseRelease(InputEvent.BUTTON1_MASK);
	}

	/**
	 * 保存到Excel中
	 * 
	 * @param title
	 * @param filename
	 */
	public static void dealExcel(String title, String filename) {

		XSSFWorkbook wb = null;
		FileOutputStream out = null;
		try {
			wb = new XSSFWorkbook(new FileInputStream(filename));
			XSSFSheet sheet = wb.getSheetAt(0);
			int rownum = sheet.getLastRowNum();
			Row row = sheet.createRow(rownum + 1);
			row.createCell(0).setCellValue(title);
			out = new FileOutputStream(filename);
			wb.write(out);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (wb != null) {
				try {
					wb.close();
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
