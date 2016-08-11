package main;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import org.apache.commons.io.FileUtils;

import data.Coords;
import data.TNGCoords;
import data.XCRCampaign;
import util.FileUtil;

import static util.Steps.*;

public class TNG {
	static final int PIXEL_INTERVAL = 16;// 每个campain之间的间隔。
	static String TNGtemplate = null;

	public static void main(String[] args) {
		TIME = 1500;
		TNG task = new TNG();
		File keywordsDir = new File("keywords");
		TNGtemplate = FileUtil.loadProperties().getProperty("TNGtemplate");
		// 数据目录结构：keywords文件夹下有数个文件夹，
		// 每个文件夹用于设置一个campaign，
		// 其中包含一个放关键词的txt文件和一个用于导入anchortext的csv文件。
		if (keywordsDir.isDirectory()) {
			int size = keywordsDir.listFiles().length;

			for (int i = 0; i < size; i++) {

				File dir = keywordsDir.listFiles()[i];
				String keywords = null;
				String csv = null;

				for (File file : dir.listFiles()) {
					if (file.getName().endsWith("txt")) {
						try {
							keywords = FileUtils.readFileToString(file);
						} catch (IOException e) {
							System.out.println("读取txt出错！");
							e.printStackTrace();
						}
					} else if (file.getName().endsWith("csv")) {
						csv = file.getAbsolutePath();
						System.out.println(csv);
					}
					if (keywords != null && csv != null) {
						task.assignCampaign(keywords, csv, i);
					}
				}
			}
		}

	}

	private void assignCampaign(String keywords, String csv, int times) {

		// 在标题栏上面多点两次确保Senuke在界面前端。
		mouseClick(TNGCoords.WIZARD);
		mouseClick(new Coords(1050, 50));

		mouseClick(TNGCoords.WIZARD);
		openFile(TNGCoords.OPEN_FILE, TNGtemplate);
		mouseClick(TNGCoords.NEXT);

		inputContent(TNGCoords.CAMPAIGN_NAME, keywords.replaceAll("\\s", " ") + new Random().nextInt());
		inputContent(TNGCoords.KEYWORDS, keywords);
		inputContent(TNGCoords.URLS, "www.selleckchem.com");
		mouseClick(TNGCoords.NEXT2);

		mouseClick(TNGCoords.GENERATE_PROFILE);
		mouseClick(TNGCoords.GENERATE_CATEGORY);
		inputContent(TNGCoords.COMPANY, XCRCampaign.generateCompany());
		mouseClick(TNGCoords.NEXT3);

		mouseClick(TNGCoords.BOOKMARK);
		mouseClick(TNGCoords.BOOKMARK_SELECT);
		mouseClick(TNGCoords.TIER2);
		mouseClick(TNGCoords.TIER2_SELECT);
		mouseClick(TNGCoords.TIER1);
		mouseClick(TNGCoords.TIER1_SELECT);
		mouseClick(TNGCoords.NEXT4);

		mouseClick(TNGCoords.NEXT5);
		mr.delay(12000);
		mouseClick(TNGCoords.FINISH);
		mr.delay(18000);

		editMoneySite(csv, times);

		mouseClick(TNGCoords.SELECT_PROJECTS);
		selectProjectShift(TNGCoords.PINGER_START, TNGCoords.PINGER_END);
		mouseClick(TNGCoords.CUT);
		selectProjectShift(TNGCoords.INDEX_START, TNGCoords.INDEX_END);
		mouseClick(TNGCoords.CUT);
		mouseClick(TNGCoords.T1_WP);
		mouseClick(TNGCoords.CUT);

		Coords[] t1 = { TNGCoords.T1_AD_W3, TNGCoords.T1_SN_W2, TNGCoords.T1_WK_W3 };
		selectProjectCtrl(t1);
		mouseClick(TNGCoords.SCHEDULE);
		mouseClick(TNGCoords.EDIT_SCHEDULE);
		mr.delay(1000);
		mr.pressEnter();
		mouseClick(TNGCoords.ARROW_DOWN);
		mouseClick(TNGCoords.SELECT);
		mr.delay(1000);
		mr.pressEnter();
		selectProjectShift(TNGCoords.WAIT1, TNGCoords.WAIT3);
		mouseClick(TNGCoords.CUT);

	}

	private void editMoneySite(String csv, int times) {
		Coords campaign = new Coords(TNGCoords.SELECT_CAMPAIGN.getX(),
				TNGCoords.SELECT_CAMPAIGN.getY() + times * PIXEL_INTERVAL);
		mouseClick(campaign);
		mouseClick(TNGCoords.SELECT_URLS);
		mouseDoubleClick(TNGCoords.SELECT_MONEYSITE);
		mr.delay(5000);
		openFile(TNGCoords.LOAD_MONEYSITE, csv);
		mouseClick(TNGCoords.EXIT);
	}

}
