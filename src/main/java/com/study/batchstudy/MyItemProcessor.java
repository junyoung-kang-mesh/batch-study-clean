package com.study.batchstudy;

import com.study.batchstudy.user.domain.PersonalInfoService;
import com.study.batchstudy.user.domain.User;
import com.study.batchstudy.user.domain.UserRepository;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Slf4j
public class MyItemProcessor implements ItemProcessor<User, User> , StepExecutionListener {

  private StepExecution stepExecution;
  private final PersonalInfoService personalInfoService;
  private final UserRepository userRepository;

  public MyItemProcessor(PersonalInfoService personalInfoService, UserRepository userRepository) {
    this.personalInfoService = personalInfoService;
    this.userRepository = userRepository;
  }

  @Override
  public User process(User item) throws Exception {
    log.info("userId : {}", item);
    personalInfoService.removePersonalInfo(item);
    return item;
  }

  /**
   * initialize stepExecution
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
