package org.occidere.twitterdump.batch;

import org.occidere.twitterdump.dto.TwitterPhoto;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemProcessor;

/**
 * @author occidere
 * @since 2019-03-02
 * Blog: https://blog.naver.com/occidere
 * Github: https://github.com/occidere
 */
public class TwitterProcessor implements ItemProcessor<TwitterPhoto, TwitterPhoto>, StepExecutionListener {
	@Override
	public void beforeStep(StepExecution stepExecution) {

	}

	@Override
	public TwitterPhoto process(TwitterPhoto item) throws Exception {
		return null;
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		return null;
	}
}
