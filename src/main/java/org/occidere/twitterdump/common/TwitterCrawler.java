package org.occidere.twitterdump.common;

import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.occidere.twitterdump.dto.TwitterPhoto;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.occidere.twitterdump.common.TwitterTag.*;

/**
 * @author occidere
 * @since 2019-03-02
 * Blog: https://blog.naver.com/occidere
 * Github: https://github.com/occidere
 */
public class TwitterCrawler {
	private Logger log = LoggerFactory.getLogger(TwitterCrawler.class);
	private Logger errorLog = LoggerFactory.getLogger("errorLogger");
	private static final String DATE_FORMAT = "yyyyMMddHHmmss";

	@Value("${chromedriver.path}")
	private String chromedriverPath = "/Users/occidere/Downloads/chromedriver";
	@Setter
	private int range;
	@Setter
	protected String url; // ex) https://twitter.com/official_izone/media

	public List<TwitterPhoto> getResult() {
		return getTwitterPhotos();
	}

	private List<TwitterPhoto> getTwitterPhotos() {
		List<TwitterPhoto> photos = new ArrayList<>();
		WebDriver driver = getChromeDriver(url);

		boolean isNotFinishedInRange = true;
		do {
			try {
				Document doc = Jsoup.parse(driver.getPageSource());
				Element timeline = doc.getElementById(ALL_POST_DIV.getValue());

				Elements posts = timeline.getElementsByClass(POST_LI.getValue());
				for (Element post : posts) {
					Element meta = post.getElementsByTag("div").first();
					String author = meta.attr(POST_AUTHOR_ATTR.getValue());
					String pageUrl = meta.attr(POST_URL_ATTR.getValue());

					Element content = post.getElementsByClass(POST_CONTENT_CLASS.getValue()).first();
					long postTimeMillis = Long.parseLong(
							content.select(String.format("a.%s > span", POST_TIMESTAMP_CLASS.getValue()))
									.attr(POST_TIMESTAMP_ATTR.getValue()));
					String date = new SimpleDateFormat(DATE_FORMAT).format(new Date(postTimeMillis));

					// 지정한 날짜 범위를 지난 글이면 stop
					if (!isInRange(date)) {
						isNotFinishedInRange = false;
						break;
					}

					String text = StringUtils.trimToEmpty(content.getElementsByClass(POST_TEXT_DIV.getValue())
							.first()
							.text());

					Element imageDiv = content.getElementsByClass(POST_IMG_DIV.getValue()).first();
					if (imageDiv != null) {
						List<String> originImageUrls = imageDiv.getElementsByTag("img").eachAttr("src");
						// 사진이 있는 경우만 처리
						if (!CollectionUtils.isEmpty(originImageUrls)) {
							photos.add(new TwitterPhoto() {{
								setDate(date);
								setContent(text);
								setOriginImageUrls(originImageUrls);
								setAuthor(author);
								setPageUrl(pageUrl);
							}});
						}
					}
				}
			} catch (Exception e) {
				errorLog.error("트윗 파싱 실패", e);
			} finally {
				scrollDown(driver, 1500); // 사진이 더 남았으면 스크롤
			}
		} while (isNotFinishedInRange);

		driver.quit();

		log.info("Photo Size: {}", photos.size());

		return photos;
	}

	/**
	 * url 에 해당하는 트위터와 연결된 상태의 WebDriver 객체를 반환한다
	 *
	 * @param url 연결할 트위터 url. ex) https://twitter.com/official_izone/media
	 * @return url 에 해당하는 트위터와 연결된 상태의 WebDriver 객체
	 */
	private WebDriver getChromeDriver(String url) {
		System.setProperty("webdriver.chrome.driver", chromedriverPath);

		ChromeOptions chromeOptions = new ChromeOptions();
		chromeOptions.addArguments("headless");
		chromeOptions.addArguments("--window-size=1920,1080"); // 화면이 너무 작으면 > 클릭 버튼이 표시 안되서 에러남

		WebDriver driver = new ChromeDriver(chromeOptions);
		driver.get(url);

		return driver;
	}

	protected boolean isInRange(String date) {
		LocalDate postDate = LocalDateTime.parse(date, DateTimeFormatter.ofPattern(DATE_FORMAT)).toLocalDate(); // yyyyMMddHHmmss
		long days = Math.abs(ChronoUnit.DAYS.between(postDate, LocalDate.now()));
		return days <= range;
	}

	/**
	 * 페이지 스크롤 다운
	 *
	 * @param driver
	 * @param waitMs 스크롤 다운하고 이미지 로딩할 동안 기다릴 대기시간
	 */
	private void scrollDown(WebDriver driver, int waitMs) {
		try {
			log.info("scroll");
			((JavascriptExecutor) driver).executeScript("window.scrollBy(0,10000);");
			Thread.sleep(waitMs);
		} catch (Exception e) {
			errorLog.error("ScrollDownError", e);
		}
	}
	public static void main(String[] args) {
		TwitterCrawler crawler = new TwitterCrawler() {{
			setUrl("https://twitter.com/official_izone/media");
			setRange(15);
		}};
		crawler.getResult().forEach(System.out::println);
	}
 }


