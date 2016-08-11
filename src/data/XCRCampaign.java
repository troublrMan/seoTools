package data;

import java.util.Properties;
import java.util.Random;

import util.FileUtil;

/**
 * @author Lyh
 *
 */
public class XCRCampaign {

	private String campaignName;
	private String URLs;
	private String keywords;
	private String tags;
	private String title;
	private String company;
	private String category;
	private String templateFilename;
	private String bio;
	private String[] article;
	private XCRKeyword keyword1;
	private XCRKeyword keyword2;
	private XCRKeyword wiki;

	public XCRCampaign(Properties pps, int articlenum) {
		init(pps, articlenum);
	}

	/**
	 * 初始化，从各种文件中加载数据
	 * 
	 * @param pps
	 *            properties文件中的各种属性
	 * @param articlenum
	 *            每次消耗的文章数量
	 * @return
	 */
	private void init(Properties pps, int articlenum) {
		keyword1 = FileUtil.loadKeyWord(pps.getProperty("keywordFilename"));
		keyword2 = FileUtil.loadKeyWord(pps.getProperty("keywordFilename"));
		wiki = FileUtil.loadKeyWord(pps.getProperty("wikiFilename"));
		String titlePattern = FileUtil.readRandomExcel(pps.getProperty("titleFilename"));
		String bioPattern = FileUtil.readRandomExcel(pps.getProperty("bioFilename"));
		templateFilename = pps.getProperty("templateFilename");
		campaignName = keyword1.getName() + " " + keyword2.getName() + " " + wiki.getName();
		URLs = keyword1.getUrl() + "\r\n" + keyword2.getUrl() + "\r\n" + wiki.getUrl();
		keywords = keyword1.getName() + "\r\n" + keyword2.getName() + "\r\n" + wiki.getName();
		tags = "{ " + keyword1.getName() + " | " + keyword2.getName() + " | " + wiki.getName() + " } ";
		title = titlePattern.replaceAll("\\$", tags);
		bio = bioPattern.replaceAll("\\$", tags);
		article = FileUtil.readtxt(pps.getProperty("articleDirname"), articlenum);
		company = generateCompany();
		category = generateCategory();
	}

	/**
	 * 随机挑选一个词作为company。
	 */
	public static String generateCompany() {
		String[] companies = "amgen,gilead sciences,biogen Idec,CSL limited,shire,alexion pharmaceuticals,vertex pharmaceutical,regeneron pharmaceuticals,novozymes,illumina,amylin pharmaceutical,biomarin,medivation,seattle genetics,ariad pharmaceuticals,incyte,human genome,cubist"
				.split(",");
		return companies[new Random().nextInt(companies.length)];
	}

	/**
	 * 随机挑选三个相关词汇作为category。
	 */
	private String generateCategory() {
		String category = "";
		String[] categories = "biology,chemistry,health,phamaceutical,science,restrainer,medical,biological,chemical,clinical,depressor,experiment,inhibitor,pathway,activity,dxy,emuch,discuz,phpwind,Organism,inhibitor,lifescience,physiology,biochemistry,research,molecular biology,reagent,agonist,cell"
				.split(",");
		Random random = new Random();
		int randIndex = 0;
		int count = 0;
		while (count < 3) {
			randIndex = random.nextInt(categories.length);
			if (category.isEmpty()) {
				category = categories[randIndex];
				count++;
			} else if (!category.contains(categories[randIndex])) {
				category = category + "," + categories[randIndex];
				count++;
			} else {
				//continue;
			}
		}
		return category;
	}

	public String getCampaignName() {
		return campaignName;
	}

	public void setCampaignName(String campaignName) {
		this.campaignName = campaignName;
	}

	public String getURLs() {
		return URLs;
	}

	public void setURLs(String uRLs) {
		URLs = uRLs;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getTemplateFilename() {
		return templateFilename;
	}

	public void setTemplateFilename(String templateFilename) {
		this.templateFilename = templateFilename;
	}

	public String getBio() {
		return bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String[] getArticle() {
		return article;
	}

	public void setArticle(String[] article) {
		this.article = article;
	}

	public XCRKeyword getKeyword1() {
		return keyword1;
	}

	public void setKeyword1(XCRKeyword keyword1) {
		this.keyword1 = keyword1;
	}

	public XCRKeyword getKeyword2() {
		return keyword2;
	}

	public void setKeyword2(XCRKeyword keyword2) {
		this.keyword2 = keyword2;
	}

	public XCRKeyword getWiki() {
		return wiki;
	}

	public void setWiki(XCRKeyword wiki) {
		this.wiki = wiki;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
}