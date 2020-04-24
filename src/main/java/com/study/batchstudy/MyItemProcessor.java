package com.study.batchstudy;

import com.study.batchstudy.user.domain.PersonalInfoService;
import com.study.batchstudy.user.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemProcessor;

@Slf4j
public class MyItemProcessor implements ItemProcessor<User, User> , StepExecutionListener {

  private StepExecution stepExecution;
  private final PersonalInfoService personalInfoService;

  public MyItemProcessor(PersonalInfoService personalInfoService) {
    this.personalInfoService = personalInfoService;
  }

  @Override
  public User process(User item) throws Exception {
    log.info("userId : {}", item.getId());
    personalInfoService.removePersonalInfo(item);
    return item;
  }

  @Override
  public void beforeStep(StepExecution stepExecution) {
    this.stepExecution = stepExecution;
  }

  @Override
  public ExitStatus afterStep(StepExecution stepExecution) {
    return null;
  }
}
