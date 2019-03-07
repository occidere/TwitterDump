package org.occidere.twitterdump.batch;

import org.occidere.twitterdump.dto.TwitterPhoto;
import org.occidere.twitterdump.repository.TwitterRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author occidere
 * @since 2019-03-02
 * Blog: https://blog.naver.com/occidere
 * Github: https://github.com/occidere
 */
public class TwitterWriter implements ItemWriter<TwitterPhoto>, StepExecutionListener {
	private Logger logger = LoggerFactory.getLogger(TwitterWriter.class);

	@Autowired
	private TwitterRepository repository;

	@Override
	public void beforeStep(StepExecution stepExecution) {

	}

	@Override
	public void write(List<? extends TwitterPhoto> items) throws Exception {
		logger.info("DB 저장 시작 ({} 건)", items.size());
		for (TwitterPhoto photo : items) {
			TwitterPhoto result = repository.save(photo);
			logger.info("result: " + result);
		}
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		return null;
	}
}
