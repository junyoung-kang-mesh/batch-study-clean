package com.study.batchstudy.dormant.my;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class MyJobConfiguration {

  private final JobBuilderFactory jobBuilderFactory;
  private final StepBuilderFactory stepBuilderFactory;
  private final DataSource dataSource; // DataSource DI

  private static final int chunkSize = 2;

  @Bean
  public Job myJob() {
    return jobBuilderFactory.get("myJob")
        .start(myStep())
        .build();
  }

  @Bean
  public Step myStep() {
    return stepBuilderFactory.get("myStep")
        .<Long, Long>chunk(chunkSize)
        .reader(myReader(null))
        .processor(myProcessor())
        .writer(myWriter(null))
        .build();
  }

  @Bean
  public ItemProcessor<? super Long, ? extends Long> myProcessor() {
    return x -> {
      log.info("x:" + x);
      return x;
    };
  }

  @Bean
  @StepScope
  public ItemReader<Long> myReader(JdbcTemplate jdbcTemplate) {
    return new MyItemReader(jdbcTemplate);
  }

  @Bean
  @StepScope
  public ItemWriter<Long> myWriter(NamedParameterJdbcTemplate jdbcTemplate) {
    return new MyItemWriter(jdbcTemplate);
  }

}
