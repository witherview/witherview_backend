package com.witherview.study;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class StudyApplicationContext implements ApplicationContextAware {
  public static ApplicationContext CONTEXT;
  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    CONTEXT = applicationContext;
  }
  // 스프링에서 생성한 bean을 이름으로 호출할 수 있도록 하는 기능
  public static Object getBean(String beanName) {
    return CONTEXT.getBean(beanName);
  }
}
