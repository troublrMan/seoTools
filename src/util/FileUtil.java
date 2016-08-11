package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.Random;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import data.XCRKeyword;

/**
 * 读取文件相关操作
 * 
 * @author lyh
 */
public class FileUtil {
	
	/**
	 * 导入配置文件
	 *  
	 * @return
	 */
	public static Properties loadProperties() {
		Properties pps = new Properties();
		try {
			pps.load(new FileInputStream("setting.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return pps;
	}

	/**
	 * 导入产品关键词和wiki词，及其URL、Anchor，顺序读取，并将已读的标记1.
	 * 
	 * @param filename
	 * @return
	 */
	public static XCRKeyword loadKeyWord(String filename) {
		XCRKeyword keywords = new XCRKeyword();
		XSSFWorkbook wb = null;
		FileOutputStream out = null;
		try {
			wb = new XSSFWorkbook(new FileInputStream(filename));
			XSSFSheet sheet = wb.getSheetAt(0);
			int rowcount = sheet.getLastRowNum();
			for (int i = 1; i < rowcount; i++) {
				Row row = sheet.getRow(i);
				if (row.getCell(3) == null || row.getCell(3).getNumericCellValue() != 1) {
					keywords.setName(row.getCell(0).getStringCellValue().trim());
					keywords.setUrl(row.getCell(1).getStringCellValue().trim());
					keywords.setAnchor(row.getCell(2).getStringCellValue().trim());
					row.createCell(3, Cell.CELL_TYPE_NUMERIC).setCellValue(1);// 已读的行标记1；
					break;
				}
			}
			out = new FileOutputStream(filename);
			wb.write(out);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (wb != null) {
				try {
					wb.close();
				//	out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return keywords;
	}

	/**
	 * 随机读取Excel文件中的一行，用于读取title和bio
	 * 
	 * @param filename
	 * @return
	 */
	public static String readRandomExcel(String filename) {
		String result = "";
		XSSFWorkbook wb = null;
		try {
			wb = new XSSFWorkbook(new FileInputStream(filename));
			XSSFSheet sheet = wb.getSheetAt(0);
			int rownum = sheet.getLastRowNum();
			Row row = null;
			row = sheet.getRow(new Random().nextInt(rownum - 1));
			result = row.getCell(0).getStringCellValue();
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
		return result;
	}

	/**
	 * 读取txt格式的Article
	 * 
	 * @param filedir
	 * @param num
	 * @return
	 */
	public static String[] readtxt(String filedir, int num) {
		String[] articles = new String[num];
		int count = 0;
		File directory = new File(filedir);
		File[] files = directory.listFiles();
		FileReader fr = null;
		BufferedReader br = null;
		for (int i = 0; i < files.length && count < num; i++) {
			if (files[i].getName().startsWith("~")) {
				continue;
			} else {
				try {
					fr = new FileReader(files[i]);
					br = new BufferedReader(fr);
					String temp = br.readLine();
					String temptotal = "";
					while (temp != null) {
						temptotal += temp;
						temp = br.readLine();
					}
					articles[count] = temptotal;
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (br != null) {
						try {
							br.close();
							fr.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
				count++;
				files[i].renameTo(new File(filedir + "/~" + files[i].getName()));
			}
		}
		return articles;

	}
}
