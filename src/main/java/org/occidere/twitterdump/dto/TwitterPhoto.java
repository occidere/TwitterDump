package org.occidere.twitterdump.dto;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
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
	private String id;
	public void setId(String id) {
		// /official_izone/status/1103685850502651904/ -> /official_izone/status/1103685850502651904
		if (StringUtils.endsWith(id, "/")) {
			id = id.substring(0, id.length() -1 );
		}
		// 1103685850502651904
		this.id = StringUtils.substringAfterLast(id, "/");
	}

	private String pageUrl;
	public void setPageUrl(String pageUrl) {
		this.pageUrl = pageUrl;
		setId(pageUrl);
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
