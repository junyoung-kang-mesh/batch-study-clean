package com.study.batchstudy.dormant.my;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class MyItemReader implements ItemReader<Long> {

  private final JdbcTemplate template;
  private List<Long> userIds;
  private int nextI;

  @Override
  public Long read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
    if (userIds == null || userIds.size() == 0) {
      userIds = template.queryForList("select id from user", Long.class);
    }
    log.info("find : {} ", userIds);

    if (nextI < userIds.size()) {
      Long aLong = userIds.get(nextI);
      nextI++;
      return aLong;
    }

    return null;
  }
}
