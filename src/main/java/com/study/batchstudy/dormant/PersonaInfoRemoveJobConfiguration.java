package com.study.batchstudy.dormant;

import com.study.batchstudy.MyItemProcessor;
import com.study.batchstudy.dormant.listener.MyChunkListener;
import com.study.batchstudy.dormant.listener.MyItemProcessListener;
import com.study.batchstudy.dormant.listener.MyItemReadListenerListener;
import com.study.batchstudy.dormant.listener.MyItemWritListener;
import com.study.batchstudy.dormant.listener.MySkipPolicy;
import com.study.batchstudy.dormant.tasklet.MyTasklet;
import com.study.batchstudy.user.domain.PersonalInfoService;
import com.study.batchstudy.user.domain.User;
import com.study.batchstudy.user.domain.UserRepository;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManagerFactory;
import javax.persistence.OptimisticLockException;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

@Slf4j // log 사용을 위한 lombok 어노테이션
@RequiredArgsConstructor // 생성자 DI를 위한 lombok 어노테이션
@Configuration
public class PersonaInfoRemoveJobConfiguration {

  private final JobBuilderFactory jobBuilderFactory;
  private final StepBuilderFactory stepBuilderFactory;
  private final EntityManagerFactory entityManagerFactory;
  private final DataSource dataSource;
  private int chunkSize = 2;

  private final PersonalInfoService personalInfoService;
  private final UserRepository userRepository;


  private static final String JOB_NAME = "personalInfoRemoveJob";
  private static final String STEP_NAME = "personalInfoRemoveStep";

  @Bean
  public Job personalInfoRemoveJob() {
    return jobBuilderFactory.get(JOB_NAME)
        .start(personalInfoRemoveStep())
        .next(myTask())
        .build();
  }

  @Bean
  @JobScope
  public Step myTask(){
    return stepBuilderFactory.get("myTask").tasklet(new MyTasklet()).build();
  }

  @Bean
  @JobScope
  public Step personalInfoRemoveStep() {

    return stepBuilderFactory
        .get(STEP_NAME)
        .<User, User>chunk(chunkSize)
        .faultTolerant()
        .skipPolicy(new MySkipPolicy(1000))
        .retry(OptimisticLockException.class).retryLimit(10000)
        .reader(personalInfoItemReader()).listener(new MyItemReadListenerListener())
        .processor(itemProcessor()).listener(new MyItemProcessListener())
        .writer(personalInfoItemWriter()).listener(new MyItemWritListener())
        .listener(myChunkListener())
        .build();
  }


  @Bean
  @StepScope
  public JpaPagingItemReader<User> personalInfoItemReader(/*@Value("#{jobParameters[day]}") Integer day*/) {

    JpaPagingItemReader reader = new JpaPagingItemReader();
    reader.setName("itemReader");
    reader.setEntityManagerFactory(entityManagerFactory);

    reader.setPageSize(chunkSize);
    reader.setQueryString(
        "SELECT p FROM User p "
            + "WHERE p.tokenRefreshedAt >= :start "
            + "AND p.tokenRefreshedAt < :end");

    Instant start = LocalDateTime.of(2020, 4, 25, 0, 0, 0).toInstant(ZoneOffset.UTC);
    Instant end = LocalDateTime.of(2020, 4, 26, 0, 0, 0).toInstant(ZoneOffset.UTC);

    HashMap<String, Object> parameterValues = new HashMap<>();
    parameterValues.put("start", start);
    parameterValues.put("end", end);

    reader.setParameterValues(parameterValues);

    return reader;
  }


  @Bean
  @StepScope
  public MyItemProcessor itemProcessor() {
    return new MyItemProcessor(personalInfoService, userRepository);
  }


  @Bean
  @StepScope
  public JpaItemWriter<User> personalInfoItemWriter() {
    JpaItemWriter<User> jpaItemWriter = new JpaItemWriter<>();
    jpaItemWriter.setEntityManagerFactory(entityManagerFactory);
    return jpaItemWriter;
  }

  @Bean
  public MyChunkListener myChunkListener() {
    return new MyChunkListener();
  }

  @Bean
  public SkipPolicy mySkipPolicy() {
    return new MySkipPolicy(20);
  }


}
