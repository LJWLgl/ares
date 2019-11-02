package cn.ganzhiqiang.ares;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 * @author nanxuan
 * @since 2017/12/10
 **/

@SpringBootApplication
public class Ares extends SpringBootServletInitializer {

  public static void main(String[] args) {
    SpringApplication.run(Ares.class, args);
  }

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
    return builder.sources(Ares.class);
  }
}
