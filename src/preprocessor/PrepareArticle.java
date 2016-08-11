package preprocessor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 
 * 文章预处理，将数据库导出的txt分割为3k大小的Article，并插入锚文字，后续需要进行Spin才能供Senuke使用。
 *@author lyh 
 */
public class PrepareArticle {

	static BufferedReader reader = null;
	static String fileName = "1.txt";
	static String outputDir = "output";
	static String anchorfile = "锚文字.xlsx";
	static int size = 3000;

	public static void main(String[] args) throws Exception {

		fileName = args[0];
		outputDir = args[1];
		anchorfile = args[2];

		File file = new File(fileName);
		reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
		char[] cbuffer = new char[size];
		int fileID = 1;
		int EOF = 0;
		while (-1 != EOF) {
			try {

				EOF = reader.read(cbuffer);
				int high = cbuffer.length - 1;
				int low = 0;

				while ('.' != cbuffer[low]) {
					low++;
				}
				if (cbuffer[low + 1] == ' ') {
					low++;
				}
				while ('.' != cbuffer[high]) {
					high--;
				}
				if (high - low < 2000) {
					continue;
				}
				String string = String.valueOf(cbuffer, low + 1, high - low);
				string = insertHyperLinks(string, anchorfile);

				BufferedWriter writer = new BufferedWriter(
						new OutputStreamWriter(new FileOutputStream(outputDir + "/" + fileID + ".txt"), "UTF-8"));
				writer.write(string);
				writer.close();
				System.out.println(fileID);
				fileID++;
			} catch (Exception e) {
				continue;
			}

		}
	}

	private static String insertHyperLinks(String string, String excelFile) {
		List<String> anchors = loadAnchors(excelFile);
		StringBuffer stringBuffer = new StringBuffer(string);
		Random random = new Random();
		int size = anchors.size();
		int length = stringBuffer.length();
		int position = 0;
		int paragraph = 4;

		for (int i = 0; i < paragraph - 1; i++) {
			position = length * i / paragraph + random.nextInt(length / paragraph);
			stringBuffer.insert(stringBuffer.indexOf(" ", position), "<br><br>");
		}
		length = stringBuffer.length();
		for (int i = 0; i < size; i++) {
			position = length * i / (size + 1) + random.nextInt(length / size);
			stringBuffer.insert(stringBuffer.indexOf(" ", position), " " + anchors.get(i));
		}

		return stringBuffer.toString();
	}

	public static List<String> loadAnchors(String excelFile) {
		List<String> anchors = new LinkedList<String>();
		XSSFWorkbook wb = null;
		try {
			wb = new XSSFWorkbook(new FileInputStream(excelFile));
			XSSFSheet sheet = wb.getSheetAt(0);
			Row row = sheet.getRow(0);
			for (int i = 0; i < 20; i++) {
				if (null != row.getCell(i)) {
					anchors.add(row.getCell(i).getStringCellValue());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (wb != null) {
				try {
					wb.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return anchors;
	}
}