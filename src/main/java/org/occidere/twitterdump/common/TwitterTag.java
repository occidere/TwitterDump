package org.occidere.twitterdump.common;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author occidere
 * @since 2019-03-02
 * Blog: https://blog.naver.com/occidere
 * Github: https://github.com/occidere
 */
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public enum TwitterTag {
	ALL_POST_DIV("timeline"),
	POST_LI("js-stream-item stream-item stream-item\n"),
	POST_URL_ATTR("data-permalink-path"),
	POST_AUTHOR_ATTR("data-screen-name"),
	POST_CONTENT_CLASS("content"),
	POST_TIMESTAMP_CLASS("tweet-timestamp.js-permalink.js-nav.js-tooltip"),
	POST_TIMESTAMP_ATTR("data-time-ms"),
	POST_TEXT_DIV("js-tweet-text-container"),
	POST_IMG_DIV("AdaptiveMedia-container");

	@Getter
	private String value;

}
