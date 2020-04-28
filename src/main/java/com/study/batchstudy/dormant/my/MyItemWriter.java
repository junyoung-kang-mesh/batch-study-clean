package com.study.batchstudy.dormant.my;

import com.study.batchstudy.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class MyItemWriter implements ItemWriter<Long> {


  private final NamedParameterJdbcTemplate jdbcTemplate;

  @Override
  public void write(List<? extends Long> items) throws Exception {
    log.info("items : " + items);

    SqlParameterSource parameters = new MapSqlParameterSource("ids", items);

    int update =
        jdbcTemplate
            .update("update user set name='bb', token_refreshed_at=now() where id IN (:ids)", parameters);


    System.out.println(update);

  }
}
