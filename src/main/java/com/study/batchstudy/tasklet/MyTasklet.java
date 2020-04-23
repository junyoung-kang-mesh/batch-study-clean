package com.study.batchstudy.tasklet;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.util.Map;

@Slf4j
public class MyTasklet implements Tasklet {
  @Override
  public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

    Map<String, Object> jobExecutionContext = chunkContext.getStepContext().getJobExecutionContext();
    String myValue = (String) jobExecutionContext.get("MY_KEY");


    log.info("myValue : " + myValue);

    return RepeatStatus.FINISHED;
  }
}
