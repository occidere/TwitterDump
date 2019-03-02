package org.occidere.twitterdump.dto;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author occidere
 * @since 2019-03-02
 * Blog: https://blog.naver.com/occidere
 * Github: https://github.com/occidere
 */
@Data
@Document(collection = "twitter")
public class TwitterPhoto  implements Serializable {
	private static final long serialVersionUID = 1L;

	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

	@Id
	@Field("pageUrl")
	private String pageUrl;
	public void setPageUrl(String pageUrl) {
		this.pageUrl = pageUrl;
	}

	private String date; // yyyyMMddHHmmss
	public void setDate(String date) {
		try {
			LocalDateTime.parse(date, FORMATTER);
			this.date = date;
		} catch (Exception e) {
			this.date = LocalDateTime.now().format(FORMATTER);
		}
	}

	private String author;
	private String content;
	private List<String> originImageUrls;
	private List<String> savedImageUrls;
}
