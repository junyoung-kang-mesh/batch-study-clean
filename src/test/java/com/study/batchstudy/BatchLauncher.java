package com.study.batchstudy;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
//@SpringBatchTest
@SpringBootTest
public class BatchLauncher {

  @Autowired JobLauncher jobLauncher;
  @Autowired  Job personalInfoRemoveJob;

  @Test
  public void hello()
      throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
    JobParametersBuilder builder = new JobParametersBuilder();
    builder.addString("day", String.valueOf(Math.random()));

    JobExecution exe = jobLauncher.run(personalInfoRemoveJob, builder.toJobParameters());
    System.out.println(exe);
  }
}
