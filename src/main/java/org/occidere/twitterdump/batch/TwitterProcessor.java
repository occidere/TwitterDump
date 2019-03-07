package org.occidere.twitterdump.batch;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.occidere.twitterdump.dto.TwitterPhoto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author occidere
 * @since 2019-03-02
 * Blog: https://blog.naver.com/occidere
 * Github: https://github.com/occidere
 */
public class TwitterProcessor implements ItemProcessor<TwitterPhoto, TwitterPhoto>, StepExecutionListener {
	private Logger logger = LoggerFactory.getLogger(TwitterProcessor.class);
	private Logger errorLogger = LoggerFactory.getLogger("errorLogger");

	@Value("${img.base.dir}")
	private String imgBaseDir;

	@Override
	public void beforeStep(StepExecution stepExecution) {

	}

	@Override
	public TwitterPhoto process(TwitterPhoto item) throws Exception {
		return saveImageIfSuccess(item);
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		return null;
	}

	private TwitterPhoto saveImageIfSuccess(TwitterPhoto photo) {
		List<String> originImageUrls = photo.getOriginImageUrls();
		if (CollectionUtils.isEmpty(originImageUrls)) {
			return null;
		}

		int seq = 0;
		List<String> savedImageUrls = new ArrayList<>(originImageUrls.size());
		for (String originImageUrl : originImageUrls) {
			try {
				String hash = DigestUtils.md5Hex(originImageUrl);
				String fileName = String.format("%s/%s_%s_%s_%02d.jpg", imgBaseDir, hash, photo.getAuthor(), photo.getDate(), seq);
				seq++;

				FileUtils.copyURLToFile(new URL(originImageUrl), new File(fileName));
				savedImageUrls.add(fileName);
				logger.info("Image downloaded: {}", fileName);
			} catch (Exception e) {
				errorLogger.error("ImageDownloadFailed", e);
				return null;
			}
		}

		photo.setSavedImageUrls(savedImageUrls);

		return photo;
	}
}
