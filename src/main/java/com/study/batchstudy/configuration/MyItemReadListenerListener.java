package com.study.batchstudy.configuration;

import com.study.batchstudy.user.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.batch.core.StepExecution;

@Slf4j
public class MyItemReadListenerListener implements ItemReadListener<User> {

  @Override
  public void beforeRead() {
    log.info("beforeRead");

  }

  @Override
  public void afterRead(User item) {
    log.info("afterRead  id:{}", item.getId());

  }

  @Override
  public void onReadError(Exception ex) {
    log.info("onReadError");

  }
}
