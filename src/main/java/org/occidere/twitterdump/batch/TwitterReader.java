package org.occidere.twitterdump.batch;

import org.occidere.twitterdump.common.TwitterCrawler;
import org.occidere.twitterdump.dto.TwitterPhoto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Iterator;

/**
 * @author occidere
 * @since 2019-03-02
 * Blog: https://blog.naver.com/occidere
 * Github: https://github.com/occidere
 */
public class TwitterReader implements ItemReader<TwitterPhoto>, StepExecutionListener {
	private Logger logger = LoggerFactory.getLogger(TwitterReader.class);

	@Autowired
	private TwitterCrawler crawler;
	private Iterator<TwitterPhoto> iterator;

	@Override
	public void beforeStep(StepExecution stepExecution) {
		logger.info("Twitter 다운로드 시작!");
		iterator = crawler.getResult().iterator();
		logger.info("Twitter 다운로드 완료!");
	}

	@Override
	public TwitterPhoto read() throws Exception {
		return iterator.hasNext() ? iterator.next() : null;
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		return null;
	}
}
