package main;

import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import data.Coords;
import data.XCRCoords;
import data.XCRCampaign;
import util.FileUtil;
import util.ImgUtil;
import static util.Steps.*;

/**
 * 此程序用于对 Senuke NCR 进行自动化设置 campaign 操作，软件使用详情可咨询 SEO 同事。
 * 
 * @author Lyh
 * 
 */
public class XCR {

	static final int PIXEL_INTERVAL = 16;// 每个campain之间的间隔。
	static final int articleNum = 6;

	/**
	 * @param args
	 *            导出、运行前设置： 到 data.XCRCoords 中修改成相应的屏幕分辨率和系统版本。
	 *            根据tempelate修改相应的文章数量及bookmark部分。
	 */
	public static void main(String[] args) {
		XCR task = new XCR();
		Properties pps = FileUtil.loadProperties();

		int times = Integer.parseInt(pps.getProperty("times").trim());
		String schedule = pps.getProperty("schedule").trim();

		for (int i = 0; i < times; i++) {
			XCRCampaign campaign = new XCRCampaign(pps, articleNum);
			task.assignCampaign(campaign, i, schedule);
		}
	}

	/**
	 * @param campaign
	 * @param times
	 *            需要配置campaign的个数。
	 * @param schedule
	 *            每个campaign之间的时间间隔。格式为 HH:MM
	 */
	private void assignCampaign(XCRCampaign campaign, int times, String schedule) {
		mouseClick(XCRCoords.WIZARD);
		mouseClick(XCRCoords.WIZARD);
		mr.delay(TIME);
		inputContent(XCRCoords.CAMPAIGN_NAME, campaign.getCampaignName());
		inputContent(XCRCoords.URLS, campaign.getURLs());
		inputContent(XCRCoords.KEYWORDS, campaign.getKeywords());
		mouseClick(XCRCoords.STEP_NEXT);
		inputContent(XCRCoords.TITLE, campaign.getTitle());
		clearContent(XCRCoords.NUMBER_OF_LINKS);
		inputContent(XCRCoords.NUMBER_OF_LINKS, "1");
		mouseClick(XCRCoords.GENERATE_PROFILE);
		inputContent(XCRCoords.COMPANY, campaign.getCompany());
		inputContent(XCRCoords.CATEGORY, campaign.getCategory());
		mouseClick(XCRCoords.GENERATE_CATEGORY);
		mouseClick(XCRCoords.STEP_NEXT);
		inputContent(XCRCoords.BIO1, campaign.getBio());
		inputContent(XCRCoords.BIO2, campaign.getBio());
		mouseClick(XCRCoords.STEP_NEXT);
		openFile(XCRCoords.OPEN_FILE, campaign.getTemplateFilename());
		mouseClick(XCRCoords.STEP_NEXT2);
		mouseClick(XCRCoords.GENERATE_PROFILE2);

		creatEmail();
		testEmail();

		mouseClick(XCRCoords.STEP_NEXT2);
		for (String content : campaign.getArticle()) {
			inputContent(XCRCoords.ARTICLE, content);
			mouseClick(XCRCoords.STEP_NEXT2);
		}
		// bookMark部分，模板没有这部分的就注释掉。
		// for (int i = 0; i < 3; i++) {
		// inputContentStep(XCRCoords.ARTICLE,
		// campaign.getArticle()[i].substring(0,
		// campaign.getArticle()[i].indexOf('.', 50) + 1));
		// mouseClickStep(XCRCoords.STEP_NEXT2);
		// }

		mouseClick(XCRCoords.CHECK_CAPTCHA);
		mr.delay(15000);

		setSchedule(times, schedule);

		mouseClick(XCRCoords.STEP_NEXT);
		mr.delay(5000);
		mouseClick(XCRCoords.STEP_NEXT2);
		mr.delay(20000);
		editMoneySite(campaign, times);
	}

	/**
	 * 设置时间，每个任务比上一个多一个间隔时间。Senuke上的时间不会自动roll，需要计算出延后之后的年、月、日、时分别自行设置。
	 * 
	 * @param times
	 *            第几次任务
	 * @param schedule
	 *            每次间隔,格式 为 小时:分钟
	 */
	private void setSchedule(int times, String schedule) {
		String[] schedules = schedule.split(":");
		Calendar calendar = Calendar.getInstance();
		int hour = Integer.parseInt(schedules[0]) * times;
		calendar.add(Calendar.HOUR, hour);
		int minute = 30 + schedule.length() > 1 ? Integer.parseInt(schedules[1]) * times : 0;
		calendar.add(Calendar.MINUTE, minute);
		System.out.println(new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss").format(calendar.getTime()));
		setDate(calendar);
	}

	private void setDate(Calendar calendar) {
		Calendar now = Calendar.getInstance();
		now.add(Calendar.MINUTE, 30);// Senuke上面默认的时间是当前时间过半小时。
		if (now.get(Calendar.YEAR) != calendar.get(Calendar.YEAR)) {
			setIntValue(XCRCoords.SCHEDULE_YEAR, calendar.get(Calendar.YEAR));
		}
		if (now.get(Calendar.MONTH) != calendar.get(Calendar.MONTH)) {
			setIntValue(XCRCoords.SCHEDULE_MONTH, calendar.get(Calendar.MONTH) + 1);
		}
		if (now.get(Calendar.DAY_OF_MONTH) != calendar.get(Calendar.DAY_OF_MONTH)) {
			setIntValue(XCRCoords.SCHEDULE_DAY, calendar.get(Calendar.DAY_OF_MONTH));
		}
		if (now.get(Calendar.HOUR_OF_DAY) != calendar.get(Calendar.HOUR_OF_DAY)) {
			setIntValue(XCRCoords.SCHEDULE_HOUR, calendar.get(Calendar.HOUR_OF_DAY));
		}
		if (now.get(Calendar.MINUTE) != calendar.get(Calendar.MINUTE)) {
			setIntValue(XCRCoords.SCHEDULE_MINUTE, calendar.get(Calendar.MINUTE));
		}
		if (now.get(Calendar.AM_PM) == Calendar.PM && calendar.get(Calendar.AM_PM) == Calendar.AM) {
			// hour处输入大于12的数时AM会自动跳PM，反之则不会跳回来，需要自行设置。
			mouseClick(XCRCoords.SCHEDULE_NOON);
			mouseClick(XCRCoords.SCHEDULE_UP);
		}
	}

	private void editMoneySite(XCRCampaign senuke, int times) {
		Coords campaign = new Coords(XCRCoords.SELECT_CAMPAIGN.getX(),
				XCRCoords.SELECT_CAMPAIGN.getY() + (times) * PIXEL_INTERVAL);
		mouseDoubleClick(campaign);
		mouseClick(XCRCoords.SELECT_URLS);
		mouseDoubleClick(XCRCoords.SELECT_MONEYSITE);
		mr.delay(5000);

		mouseClick(XCRCoords.SELECT_ANCHOR_TEXT1);
		mouseClick(XCRCoords.EDIT_ANCHOR);
		clearContentWithKeyboard(XCRCoords.EDIT_ANCHOR_TEXT);
		inputContent(XCRCoords.EDIT_ANCHOR_TEXT, senuke.getKeyword1().getAnchor());
		mr.pressEnter();

		mouseClick(XCRCoords.SELECT_ANCHOR_TEXT2);
		mouseClick(XCRCoords.EDIT_ANCHOR);
		clearContentWithKeyboard(XCRCoords.EDIT_ANCHOR_TEXT);
		inputContent(XCRCoords.EDIT_ANCHOR_TEXT, senuke.getKeyword2().getAnchor());
		mr.pressEnter();

		mouseClick(XCRCoords.SELECT_ANCHOR_TEXT3);
		mouseClick(XCRCoords.EDIT_ANCHOR);
		clearContentWithKeyboard(XCRCoords.EDIT_ANCHOR_TEXT);
		inputContent(XCRCoords.EDIT_ANCHOR_TEXT, senuke.getWiki().getAnchor());
		mr.pressEnter();

		mouseClick(XCRCoords.EXIT);
	}

	/**
	 * 测试生成的Email。
	 * 每隔8秒截图检查，与成功弹窗相似度在0.95以上可以认为已经成功，相似度0.86以上认为是出错弹窗，否则继续等待，五次以后重新生成Email。
	 */
	private void testEmail() {
		mouseClick(XCRCoords.TEST_MAIL);
		BufferedImage testsuccessImg = ImgUtil.readImg("picture/testsuccess.bmp");
		int timer = 0;
		while (timer < 5) {
			mr.delay(8000);
			timer++;
			BufferedImage img = mr.screenShot(XCRCoords.MSG_BOX_START, XCRCoords.MSG_BOX_END);
			// try {
			// ImageIO.write(img, "bmp", new File("picture/testPicture/" +
			// System.currentTimeMillis() + ".bmp"));
			// } catch (IOException e) {
			// e.printStackTrace();
			// }
			double similarity = ImgUtil.calculateSimilarity(img, testsuccessImg);
			System.out.println(new SimpleDateFormat("HH:mm:ss").format(new Date()) + "   test：" + similarity);
			if (similarity > 0.95) {
				mr.clickMouse(XCRCoords.MSG_BOX_OK);
				break;
			} else if (similarity > 0.86) {
				mr.clickMouse(XCRCoords.MSG_BOX_OK);
				if (timer < 5) {
					mouseClick(XCRCoords.TEST_MAIL);
				}
			}
			if (timer == 5) {
				creatEmail();
				testEmail();
				break;
			}
		}
	}

	/**
	 * 生成Email。每隔8秒截图检查，与出错弹窗相似度0.95以上认为已经出错，重新点击，否则认为已经成功，进入下一步测试。测试不成功会返回来。
	 */
	private void creatEmail() {
		mouseClick(XCRCoords.SELECT_MAIL_PROTOCOL);
		mouseClick(XCRCoords.SELECT_MAIL_PROTOCOL2);
		mouseClick(XCRCoords.CREATE_MAIL);
		mouseClick(XCRCoords.CREATE_MAIL2);
		BufferedImage errorImg = ImgUtil.readImg("picture/emailerror.bmp");
		for (int timer = 0; timer < 5; timer++) {
			mr.delay(8000);
			BufferedImage img = mr.screenShot(XCRCoords.MSG_BOX_START, XCRCoords.MSG_BOX_END);
			// try {
			// ImageIO.write(img, "bmp", new File("picture/emailPicture/" +
			// System.currentTimeMillis() + ".bmp"));
			// } catch (IOException e) {
			// e.printStackTrace();
			// }
			double similarity = ImgUtil.calculateSimilarity(img, errorImg);
			System.out.println(new SimpleDateFormat("HH:mm:ss").format(new Date()) + "   email：" + similarity);
			if (similarity > 0.95) {
				mr.clickMouse(XCRCoords.MSG_BOX_OK);
				mr.delay(3000);
				mouseClick(XCRCoords.SELECT_MAIL_PROTOCOL);
				mouseClick(XCRCoords.SELECT_MAIL_PROTOCOL2);
				mouseClick(XCRCoords.CREATE_MAIL);
				mouseClick(XCRCoords.CREATE_MAIL2);
				timer = 0;
			}
		}

	}

}
