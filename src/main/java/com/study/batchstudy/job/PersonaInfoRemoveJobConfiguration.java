package com.study.batchstudy.job;

import com.study.batchstudy.configuration.MyChunkListener;
import com.study.batchstudy.configuration.MyItemReadListenerListener;
import com.study.batchstudy.configuration.MySkipPolicy;
import com.study.batchstudy.user.domain.PersonalInfoService;
import com.study.batchstudy.user.domain.User;
import javax.persistence.EntityManagerFactory;
import javax.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j // log 사용을 위한 lombok 어노테이션
@RequiredArgsConstructor // 생성자 DI를 위한 lombok 어노테이션
@Configuration
public class PersonaInfoRemoveJobConfiguration {

  private final JobBuilderFactory jobBuilderFactory;
  private final StepBuilderFactory stepBuilderFactory;
  private final EntityManagerFactory entityManagerFactory;
  private int chunkSize = 2;
  private final PersonalInfoService personalInfoService;


  private static final String JOB_NAME = "personalInfoRemoveJob";
  private static final String STEP_NAME = "personalInfoRemoveStep";

  @Bean
  public Job personalInfoRemoveJob() {
    return jobBuilderFactory.get(JOB_NAME).start(personalInfoRemoveStep()).build();
  }

  @Bean
  @JobScope
  public Step personalInfoRemoveStep() {

    return stepBuilderFactory
        .get(STEP_NAME)
        .<User, User>chunk(chunkSize)
        .faultTolerant()
        .reader(personalInfoItemReader()).listener(new MyItemReadListenerListener())
        .processor(itemProcessor())
        .writer(personalInfoItemWriter())
        .faultTolerant()
        .skipPolicy(mySkipPolicy())
        .listener(myChunkListener())
        .build();
  }


  @Bean
  @StepScope
  public JpaPagingItemReader<User> personalInfoItemReader(/*@Value("#{jobParameters[day]}") Integer day*/ ) {

    JpaPagingItemReader reader = new JpaPagingItemReader();
    reader.setName("itemReader");
    reader.setEntityManagerFactory(entityManagerFactory);
    reader.setPageSize(chunkSize);
    reader.setQueryString("SELECT p FROM User p");

    return reader;
  }


  @Bean
  @StepScope
  public ItemProcessor<User, User> itemProcessor() {
    return user -> {
      log.info("userId : {}", user.getId());

      // null 반환시 filter_count +1
//      if (true) {
//        throw new OptimisticLockException("exception! id : " + user.getId());
//      }

//      if (user.getId().equals(13L)) {
//        throw new IllegalArgumentException("exception! id : " + user.getId());
//      }

      personalInfoService.removePersonalInfo(user);
      return user;
    };
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
