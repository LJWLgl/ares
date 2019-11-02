package cn.ganzhiqiang.ares.context;

import javax.annotation.Resource;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cn.ganzhiqiang.ares.common.helper.ConfigHelper;

import java.util.Properties;

/**
 * @author nanxuan
 * @since 2018/5/14
 **/

@Configuration
public class EmailSessionConfig {

  @Resource
  private ConfigHelper configHelper;

  public Properties qqPropertiy() {
    String username = configHelper.getValueByKey("email.username", String.class);
    String password = configHelper.getValueByKey("email.password", String.class);

    Properties props = new Properties();
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.ssl.enable", "true");
    props.put("mail.debug", "false");
    props.put("mail.transport.protocol", "smtp");
    props.put("mail.debug", "false");
    props.put("mail.smtp.timeout", "10000");
    props.put("mail.smtp.port", "465");
    props.put("mail.smtp.host", "smtp.qq.com");
    props.put("username", username);
    props.put("password", password);
    return props;
  }

  @Bean
  public Session emailSession() {
    Properties props = qqPropertiy();
    String username = props.getProperty("username");
    String password = props.getProperty("password");
    Session session = Session.getInstance(props, new Authenticator() {
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(username, password);
        }
    });
    return session;
  }

}
