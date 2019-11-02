package cn.ganzhiqiang.ares.common.utils;

import lombok.extern.slf4j.Slf4j;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


@Slf4j
public class HttpUtil {

  private static final String DEFAULT_CHARSET = "UTF-8";

  private static final PoolingHttpClientConnectionManager phccm =
      new PoolingHttpClientConnectionManager();

  static { // 这边来设置并发
    phccm.setDefaultMaxPerRoute(10);
    phccm.setMaxTotal(20);
  }

  private static CloseableHttpClient client = HttpClients.custom().setConnectionManager(phccm)
      .build();

  private static final RequestConfig CONFIG = RequestConfig.custom()
      .setConnectTimeout(5000).setSocketTimeout(10000)
      .build();

  public static String doGet(String url, Map<String, String> queryParam) {
    URI uri = generateURLParams(url, queryParam);
    HttpGet get = new HttpGet(uri);
    get.setConfig(CONFIG);
    CloseableHttpResponse httpResponse = null;
    try {
      httpResponse = client.execute(get);
      return generateHttpResponse(httpResponse);
    } catch (IOException e) {
      e.printStackTrace();
      log.error(url, e);
      return null;
    }
  }

  /**
   * POST请求, json主体
   * 
   * @param url api
   * @param params query string
   * @param json 请求体内是json字符串
   */
  public static String doPost(String url, Map<String, String> params, String json) {
    URI uri = generateURLParams(url, params);
    HttpPost post = new HttpPost(uri);
    post.setConfig(CONFIG);
    CloseableHttpResponse httpResponse = null;
    try {
      HttpEntity entity = new StringEntity(json, DEFAULT_CHARSET);
      post.setEntity(entity);
      post.setHeader("Content-Type", "application/json");
      httpResponse = client.execute(post);
      return generateHttpResponse(httpResponse);
    } catch (IOException e) {
      log.error(url, e);
      return null;
    }
  }

  /**
   * POST请求, json主体
   *
   * @param url api
   * @param params query string
   * @param json 请求体内是json字符串
   * @param headers 请求header
   */
  public static String doPost(String url, Map<String, String> params, String json,
      Map<String, String> headers) {
    URI uri = generateURLParams(url, params);
    HttpPost post = new HttpPost(uri);
    post.setConfig(CONFIG);
    headers.entrySet().forEach(item -> post.addHeader(item.getKey(), item.getValue()));
    CloseableHttpResponse httpResponse = null;
    try {
      HttpEntity entity = new StringEntity(json, DEFAULT_CHARSET);
      post.setEntity(entity);
      post.setHeader("Content-Type", "application/json");
      httpResponse = client.execute(post);
      return generateHttpResponse(httpResponse);
    } catch (IOException e) {
      log.error(url, e);
      return null;
    }
  }

  /**
   * POST请求,form主体
   * 
   * @param url api
   * @param form form表单
   */
  public static String doPost(String url, Map<String, String> form) {
    URI uri = generateURLParams(url, null);
    List<NameValuePair> urlEncodedForm = generateUrlEncodeForm(form);
    HttpPost post = new HttpPost(uri);
    post.setConfig(CONFIG);
    CloseableHttpResponse httpResponse = null;
    try {
      HttpEntity entity = new UrlEncodedFormEntity(urlEncodedForm, DEFAULT_CHARSET);
      post.setEntity(entity);
      httpResponse = client.execute(post);
      return generateHttpResponse(httpResponse);
    } catch (IOException e) {
      log.info(url, e);
      return null;
    }
  }

  public static String doPost(String url) {
    URI uri = generateURLParams(url, null);
    HttpPost post = new HttpPost(uri);
    post.setConfig(CONFIG);
    CloseableHttpResponse httpResponse = null;
    try {
      httpResponse = client.execute(post);
      return generateHttpResponse(httpResponse);
    } catch (IOException e) {
      log.info(url, e);
      return null;
    }
  }

  public static String doMultiPartPost(String url, Map<String, String> params, MultipartFile file) {
    URI uri = generateURLParams(url, null);
    HttpPost post = new HttpPost(uri);
    CloseableHttpResponse httpResponse = null;
    try {
      MultipartEntityBuilder builder = MultipartEntityBuilder.create()
          .addBinaryBody("file", file.getInputStream());
      params.entrySet().forEach(item -> builder.addTextBody(item.getKey(), item.getValue()));
      HttpEntity entity = builder.build();
      post.setEntity(entity);
      httpResponse = client.execute(post);
      return generateHttpResponse(httpResponse);
    } catch (IOException e) {
      log.info(url, e);
      return null;
    }
  }

  private static URI generateURLParams(String url, Map<String, String> params) {
    URI uri = null;
    try {
      URIBuilder uriBuilder = new URIBuilder(url);
      if (null != params) {
        for (Entry<String, String> entry : params.entrySet()) {
          uriBuilder.addParameter(entry.getKey(), entry.getValue());
        }
      }
      uri = uriBuilder.build();
    } catch (URISyntaxException e) {
      log.error(url, e);
      return uri;
    }
    return uri;
  }

  private static List<NameValuePair> generateUrlEncodeForm(Map<String, String> form) {
    if (CollectionUtils.isEmpty(form)) {
      return null;
    }
    List<NameValuePair> list = new ArrayList<>();
    for (Entry<String, String> entry : form.entrySet()) {
      list.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
    }
    return list;
  }

  private static String generateHttpResponse(CloseableHttpResponse httpResponse) {
    if (null != httpResponse) {
      HttpEntity entity = null;
      try {
        entity = httpResponse.getEntity();
        return EntityUtils.toString(entity, DEFAULT_CHARSET);
      } catch (IOException e) {
        return null;
      } finally {
        try {
          httpResponse.close();
        } catch (IOException e) {
          log.error("Close Response Failed", e);
        }
      }
    }
    return null;
  }

}