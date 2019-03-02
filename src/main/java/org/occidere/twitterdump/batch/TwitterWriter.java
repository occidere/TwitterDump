package org.occidere.twitterdump.batch;

import org.occidere.twitterdump.dto.TwitterPhoto;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

/**
 * @author occidere
 * @since 2019-03-02
 * Blog: https://blog.naver.com/occidere
 * Github: https://github.com/occidere
 */
public class TwitterWriter implements ItemWriter<TwitterPhoto>, StepExecutionListener {
	@Override
	public void beforeStep(StepExecution stepExecution) {

	}

	@Override
	public void write(List<? extends TwitterPhoto> items) throws Exception {

	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		return null;
	}
}
