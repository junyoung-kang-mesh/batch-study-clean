package com.study.batchstudy.dormant.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;

@Slf4j
public class MyChunkListener implements ChunkListener {

  @Override
  public void beforeChunk(ChunkContext context) {

  }

  @Override
  public void afterChunk(ChunkContext context) {
    log.info("========================= chunk reporting start =========================");
    StepExecution stepExecution = context.getStepContext().getStepExecution();

    int skipCount = stepExecution.getSkipCount();
    int rs = stepExecution.getReadSkipCount();
    int ps = stepExecution.getProcessSkipCount();
    int ws = stepExecution.getWriteSkipCount();




    int readCount = stepExecution.getReadCount();
    int writeCount = stepExecution.getWriteCount();
    int commitCount = stepExecution.getCommitCount();


    log.info("rs:{}, ps:{}, ws:{}, skipCount:{}", rs, ps, ws, skipCount);
    log.info("readCount {}, writeCount:{}, commitCount:{}",readCount, writeCount, commitCount);
    log.info("========================= chunk reporting end =========================");
  }

  @Override
  public void afterChunkError(ChunkContext context) {

  }
}
