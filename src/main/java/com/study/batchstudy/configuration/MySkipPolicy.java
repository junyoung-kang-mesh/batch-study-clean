package com.study.batchstudy.configuration;

import javax.persistence.OptimisticLockException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.batch.core.step.skip.SkipPolicy;

@Slf4j
public class MySkipPolicy implements SkipPolicy {

  private final int MAX_SKIP_COUNT;

  public MySkipPolicy(int max_skip_count) {
    MAX_SKIP_COUNT = max_skip_count;
  }

  @Override
  public boolean shouldSkip(Throwable t, int skipCount) throws SkipLimitExceededException {

    if (t instanceof OptimisticLockException || t instanceof IllegalArgumentException) {
      if (skipCount < MAX_SKIP_COUNT) {
        log.warn("예외가 발생했지만 MAX_SKIP_COUNT 에 도달하지 않아 skip 합니다. {}/{} --{}", skipCount, MAX_SKIP_COUNT, t.getMessage());
        return true;
      } else {
        log.warn("예외가 MAX_SKIP_COUNT 에 도달하여 더이상 skip 할수 없습니다. {} --{}", skipCount, t.getMessage());
        return false;
      }
    }

    return false;
  }
}
