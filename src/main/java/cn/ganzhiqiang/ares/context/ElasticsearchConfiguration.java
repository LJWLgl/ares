package cn.ganzhiqiang.ares.context;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author zq_gan
 * @since 2019/12/2
 **/

@Configuration
public class ElasticsearchConfiguration implements FactoryBean<RestHighLevelClient>, InitializingBean, DisposableBean {
  private static final Logger LOGGER = LoggerFactory.getLogger(ElasticsearchConfiguration.class);

  private String host = "140.143.144.28";
  private int port = 9200;
//  private String username;
//  @Value("${spring.data.elasticsearch.password}")
//  private String password;

  private RestHighLevelClient restHighLevelClient;

  @Override
  public void destroy() throws Exception {
    try {
      LOGGER.info("Closing elasticSearch client");
      if (restHighLevelClient != null) {
        restHighLevelClient.close();
      }
    } catch (final Exception e) {
      LOGGER.error("Error closing ElasticSearch client: ", e);
    }
  }

  @Override
  public RestHighLevelClient getObject() throws Exception {
    return restHighLevelClient;
  }

  @Override
  public Class<RestHighLevelClient> getObjectType() {
    return RestHighLevelClient.class;
  }

  @Override
  public boolean isSingleton() {
    return false;
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    buildClient();
  }

  protected void buildClient() {
    final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
//    credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));
    RestClientBuilder builder = RestClient.builder(new HttpHost(host, port))
        .setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider));
    restHighLevelClient = new RestHighLevelClient(builder);
  }

}
