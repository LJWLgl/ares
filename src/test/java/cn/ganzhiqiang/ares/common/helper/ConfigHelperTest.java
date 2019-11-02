package cn.ganzhiqiang.ares.common.helper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.ganzhiqiang.ares.Ares;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Ares.class)
public class ConfigHelperTest {

  @Resource
  private ConfigHelper configHelper;

  @Test
  public void test() {
    String value = configHelper.getValueByKey("redis.cache.host", String.class, "test");
    System.out.println(value);
  }

  @Test
  public void test01() {
    String value = ConfigHelper.getValue("redis.cache.host", String.class, "test");
    System.out.println(value);
  }

}
