package com.study.batchstudy.configuration;

import com.study.batchstudy.user.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemProcessListener;

@Slf4j
public class MyItemProcessListener implements ItemProcessListener<User, User> {

  @Override
  public void beforeProcess(User item) {

  }

  @Override
  public void afterProcess(User item, User result) {

  }

  @Override
  public void onProcessError(User item, Exception e) {
    log.error("e! ", e);


  }
}
