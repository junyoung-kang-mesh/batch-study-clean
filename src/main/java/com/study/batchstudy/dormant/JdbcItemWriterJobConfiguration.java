package com.study.batchstudy.dormant;


import com.study.batchstudy.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import javax.sql.DataSource;
import java.time.Instant;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class JdbcItemWriterJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource; // DataSource DI

    private static final int chunkSize = 2;

    @Bean
    public Job jdbcBatchItemWriterJob() {
        return jobBuilderFactory.get("jdbcBatchItemWriterJob")
                .start(jdbcBatchItemWriterStep())
                .build();
    }

    @Bean
    public Step jdbcBatchItemWriterStep() {
        return stepBuilderFactory.get("jdbcBatchItemWriterStep")
                .<User, User>chunk(chunkSize)
                .reader(jdbcBatchItemWriterReader())
                .processor(jdbcBatchItemWriterProcessor())
                .writer(jdbcBatchItemWriter())
                .build();
    }

  private ItemProcessor<User,User> jdbcBatchItemWriterProcessor() {
    return (x) -> {
      x.deleteName();
      x.setTokenRefreshedAt(Instant.now());
      return x;
    };
  }

  @Bean
    public JdbcCursorItemReader<User> jdbcBatchItemWriterReader() {
        return new JdbcCursorItemReaderBuilder<User>()
                .fetchSize(chunkSize)
                .dataSource(dataSource)
                .rowMapper(new BeanPropertyRowMapper<>(User.class))
                .sql("SELECT id, name, day, token_refreshed_at FROM user")
                .name("jdbcBatchItemWriter")
                .build();
    }

    /**
     * reader에서 넘어온 데이터를 하나씩 출력하는 writer
     */
    @Bean // beanMapped()을 사용할때는 필수
    public JdbcBatchItemWriter<User> jdbcBatchItemWriter() {
        return new JdbcBatchItemWriterBuilder<User>()
                .dataSource(dataSource)
                .sql("update user set name=:name, token_refreshed_at=now()")
                .beanMapped()
                .build();
    }
}
