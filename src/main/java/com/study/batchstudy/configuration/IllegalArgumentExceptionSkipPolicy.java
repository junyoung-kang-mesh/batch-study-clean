package com.study.batchstudy.configuration;


import javax.persistence.OptimisticLockException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.batch.core.step.skip.SkipPolicy;

@Slf4j
public class IllegalArgumentExceptionSkipPolicy implements SkipPolicy {

  private int MAX_SKIP_COUNT;

  public IllegalArgumentExceptionSkipPolicy(int maxSkipCount) {
    this.MAX_SKIP_COUNT = maxSkipCount;
  }

  @Override
  public boolean shouldSkip(Throwable t, int skipCount) throws SkipLimitExceededException {

    if (t instanceof IllegalArgumentException) {
      if (skipCount < MAX_SKIP_COUNT) {
        log.warn("IllegalArgumentException 예외가 발생했지만 MAX_SKIP_COUNT 에 도달하지 않아 skip 합니다. {}/{}" , skipCount , MAX_SKIP_COUNT);
        return true;
      }
      else {
        log.warn("IllegalArgumentException 예외가 MAX_SKIP_COUNT 만큼 발생하여 skip 할수 없습니다. {}" , skipCount);
      }
    }




    return false;
  }

}