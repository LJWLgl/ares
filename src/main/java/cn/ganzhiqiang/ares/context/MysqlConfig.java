package cn.ganzhiqiang.ares.context;

import com.zaxxer.hikari.HikariDataSource;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import cn.ganzhiqiang.ares.common.helper.ConfigHelper;

import javax.sql.DataSource;

/**
 * @author nanxuan
 * @since 2017/12/10
 **/

@Configuration
public class MysqlConfig {

  @Autowired
  private ConfigHelper configHelper;

  @Bean
  public HikariDataSource ayyMaster() {
    String ayyUrl = configHelper.getValueByKey("mysql.ayy.url", String.class);
    String ayyUsername = configHelper.getValueByKey("mysql.ayy.user", String.class);
    String ayyPassword = configHelper.getValueByKey("mysql.ayy.password", String.class);
    return createDataSource(createDataSourceConfig(), ayyUrl, ayyUsername, ayyPassword);
  }

  private DataSourceConfig createDataSourceConfig() {
    DataSourceConfig dataSourceConfig = new DataSourceConfig();
    dataSourceConfig.setMaxActive(60);
    dataSourceConfig.setMaxIdle(60);
    dataSourceConfig.setMinIdle(60);
    dataSourceConfig.setInitialSize(10);
    dataSourceConfig.setMaxWait(3000);
    return dataSourceConfig;
  }

  @Bean(name = {"writeTpl", "readTpl"})
  public SqlSessionTemplate sqlSessionTemplate() throws Exception {
    return createSqlSessionTemplate(ayyMaster(), "cn.ganzhiqiang.ares",
        "classpath:cn/ganzhiqiang/ares/mapper/*.xml");
  }

  @Bean
  public TransactionTemplate transactionTpl() {
    return createTransactionTemplate(ayyMaster());
  }

  public HikariDataSource createDataSource(DataSourceConfig config, String url, String userName, String userPassword) {
    HikariDataSource ds = new HikariDataSource();
    ds.setDriverClassName(config.getDriverClassName());
    ds.setAutoCommit(config.isDefaultAutoCommit());
    ds.setMaximumPoolSize(config.getMaxActive());
    ds.setMinimumIdle(config.getMaxIdle());
    ds.setReadOnly(config.isReadOnly());
    ds.setJdbcUrl(url);
    ds.setUsername(userName);
    ds.setPassword(userPassword);
    return ds;
  }

  public  SqlSessionTemplate createSqlSessionTemplate(DataSource datasource, String aliasPackage, String mapperResource) throws Exception {
    SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
    sessionFactory.setDataSource(datasource);
    sessionFactory.setTypeAliasesPackage(aliasPackage);
    PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
    Resource[] resources = resolver.getResources(mapperResource);
    sessionFactory.setMapperLocations(resources);
    return new SqlSessionTemplate(sessionFactory.getObject());
  }

  public TransactionTemplate createTransactionTemplate(DataSource dataSource) {
    DataSourceTransactionManager dsm = new DataSourceTransactionManager(dataSource);
    return new TransactionTemplate(dsm);
  }

}
