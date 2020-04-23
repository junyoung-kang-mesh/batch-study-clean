package com.study.batchstudy.user.domain;

import org.springframework.stereotype.Service;

@Service
public class PersonalInfoService {

  public void removePersonalInfo(User user){
    user.deleteName();
  }
}
