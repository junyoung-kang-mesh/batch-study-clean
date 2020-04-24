package com.study.batchstudy;

import com.study.batchstudy.user.domain.PersonalInfoService;
import com.study.batchstudy.user.domain.User;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@StepScope
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

    List<String> usernames = getUserNameList();
    usernames.add(item.getName());
    return item;
  }

  /**
   * stepExecution 을 멤버변수로 쓸수있게 함
   * @param stepExecution
   */
  @Override
  public void beforeStep(StepExecution stepExecution) {
    this.stepExecution = stepExecution;
    stepExecution.getExecutionContext().put("usernames", new ArrayList<String>());
  }

  @Override
  public ExitStatus afterStep(StepExecution stepExecution) {
    log.info("user size : {}", getUserNameList().size());
    return stepExecution.getExitStatus();
  }


  private List<String> getUserNameList() {
    ExecutionContext executionContext = stepExecution.getExecutionContext();
    return (List<String>) executionContext.get("usernames");
  }

}
