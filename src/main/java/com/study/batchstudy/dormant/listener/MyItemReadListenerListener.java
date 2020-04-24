package com.study.batchstudy.dormant.listener;

import com.study.batchstudy.user.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemReadListener;

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
