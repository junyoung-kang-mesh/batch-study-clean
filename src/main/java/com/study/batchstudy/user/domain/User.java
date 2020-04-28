package com.study.batchstudy.user.domain;

import java.time.Instant;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Table(name = "user")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class User {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  private Integer day;

  @Column(name = "token_refreshed_at")
  private Instant tokenRefreshedAt;

  private String desc;

  public User(String name, Integer day) {
    this.name = name;
    this.day = day;
    this.tokenRefreshedAt = Instant.now();
  }


  public void deleteName() {
    name = "#####";
  }

  public void setTokenRefreshedAt(Instant now) {
    this.tokenRefreshedAt = now;
  }
}
