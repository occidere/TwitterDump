package org.occidere.twitterdump.batch;

import org.occidere.twitterdump.common.TwitterCrawler;
import org.occidere.twitterdump.dto.TwitterPhoto;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.*;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @author occidere
 * @since 2019-03-02
 * Blog: https://blog.naver.com/occidere
 * Github: https://github.com/occidere
 */
@Configuration
@EnableBatchProcessing
public class BatchConfiguration extends DefaultBatchConfigurer {
	@Override
	public void setDataSource(DataSource dataSource) {
	}

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Bean
	public Job twitterDumpJob(Step twitterDumpStep) {
		return jobBuilderFactory.get("twitterDumpJob")
				.incrementer(new RunIdIncrementer())
				.flow(twitterDumpStep)
				.end()
				.build();
	}

	@Bean
	public Step twitterDumpStep() {
		return stepBuilderFactory.get("twitterDumpStep")
				.transactionManager(dummyTransactionManager())
				.<TwitterPhoto, TwitterPhoto>chunk(1)
				.reader(reader())
				.processor(processor())
				.writer(writer())
				.build();
	}

	@Bean
	public ResourcelessTransactionManager dummyTransactionManager() {
		return new ResourcelessTransactionManager();
	}

	@Bean
	@StepScope
	public TwitterCrawler twitterCrawler(
			@Value("#{jobParameters[url]}") String url,
			@Value("#{jobParameters[dateRange] == null ? 1 : jobParameters[dateRange]}") int dateRange) {
		System.out.println("dateRange: " + dateRange);
		TwitterCrawler crawler = new TwitterCrawler();
		crawler.setUrl(url);
		crawler.setRange(dateRange);
		return crawler;
	}

	@Bean
	public ItemReader<TwitterPhoto> reader() {
		return new TwitterReader();
	}

	@Bean
	public ItemProcessor<TwitterPhoto, TwitterPhoto> processor() {
		return new TwitterProcessor();
	}

	@Bean
	public ItemWriter<TwitterPhoto> writer() {
		return new TwitterWriter();
	}

	// TODO: 배치 완성
	// TODO: RestAPI 완성
	// TODO: MongoDB 연동
}
