//package com.study.batchstudy.configuration;
//
//import com.study.batchstudy.user.domain.User;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.batch.core.SkipListener;
//
///**
// * 커밋
// */
//@Slf4j
//public class MySkipListener implements SkipListener<User, User> {
//
//
//  @Override
//  public void onSkipInRead(Throwable t) {
//    log.error("message : ", t);
//
//  }
//
//  @Override
//  public void onSkipInWrite(User item, Throwable t) {
//    log.error("error occurred on write! : {}", item);
//    log.error("message : ", t);
//
//  }
//
//  @Override
//  public void onSkipInProcess(User item, Throwable t) {
//    log.error("error occurred on process! : {}", item);
//    log.error("message : ", t);
//
//  }
//}
