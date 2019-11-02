package cn.ganzhiqiang.ares.context;

/**
 * @author zq_gan
 * @since 2019/9/8
 **/

public class DataSourceConfig {
  protected String driverClassName = "com.mysql.jdbc.Driver";
  protected int maxActive = 10;
  protected int maxIdle = 10;
  protected int minIdle = 1;
  protected int initialSize = 1;
  protected int maxWait = 5000;
  protected boolean testOnBorrow = false;
  protected boolean testOnReturn = false;
  protected boolean testWhileIdle = false;
  protected boolean defaultAutoCommit = true;
  protected boolean readOnly = false;

  public DataSourceConfig() {
  }

  public int getMaxWait() {
    return this.maxWait;
  }

  public void setMaxWait(int maxWait) {
    this.maxWait = maxWait;
  }

  public boolean isDefaultAutoCommit() {
    return this.defaultAutoCommit;
  }

  public void setDefaultAutoCommit(boolean defaultAutoCommit) {
    this.defaultAutoCommit = defaultAutoCommit;
  }

  public String getDriverClassName() {
    return this.driverClassName;
  }

  public void setDriverClassName(String driverClassName) {
    this.driverClassName = driverClassName;
  }

  public int getInitialSize() {
    return this.initialSize;
  }

  public void setInitialSize(int initialSize) {
    this.initialSize = initialSize;
  }

  public int getMaxActive() {
    return this.maxActive;
  }

  public void setMaxActive(int maxActive) {
    this.maxActive = maxActive;
  }

  public int getMaxIdle() {
    return this.maxIdle;
  }

  public void setMaxIdle(int maxIdle) {
    this.maxIdle = maxIdle;
  }

  public int getMinIdle() {
    return this.minIdle;
  }

  public void setMinIdle(int minIdle) {
    this.minIdle = minIdle;
  }

  public boolean isTestOnBorrow() {
    return this.testOnBorrow;
  }

  public void setTestOnBorrow(boolean testOnBorrow) {
    this.testOnBorrow = testOnBorrow;
  }

  public boolean isTestOnReturn() {
    return this.testOnReturn;
  }

  public void setTestOnReturn(boolean testOnReturn) {
    this.testOnReturn = testOnReturn;
  }

  public boolean isTestWhileIdle() {
    return this.testWhileIdle;
  }

  public void setTestWhileIdle(boolean testWhileIdle) {
    this.testWhileIdle = testWhileIdle;
  }

  public boolean isReadOnly() {
    return this.readOnly;
  }

  public void setReadOnly(boolean readOnly) {
    this.readOnly = readOnly;
  }
}
