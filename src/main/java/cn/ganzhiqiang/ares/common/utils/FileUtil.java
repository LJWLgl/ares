package cn.ganzhiqiang.ares.common.utils;

import com.alibaba.fastjson.JSON;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author zqgan
 * @since 2018/10/31
 **/

public class FileUtil {

    public static String DEFAULT_PATH_PREFIX = "/gzq/config/ares/";

    public static String getProperty(String path, String key) {
        InputStream in = FileUtil.class.getResourceAsStream(path);
        Properties prop = new Properties();
        try {
            prop.load(in);
            return prop.getProperty(key);
        } catch (IOException e) {
            return null;
        }
    }


  public static <T> T getProperty(String path, String key, Class<T> clz, T defaultValue) {
    InputStream in = null;
    try {
      in = new FileInputStream(new File(path));
      Properties prop = new Properties();
      prop.load(in);
      return cast(prop.getProperty(key), clz);
    } catch (Exception e) {
      return defaultValue;
    }
  }

    public static <T> List<T> readJsonList(String fileName, Class<T> clz) {
      if (StringUtils.isBlank(fileName)) {
        return new ArrayList<>();
      }
      String value = readJsonFile(fileName);
      if (StringUtils.isBlank(value)) {
        return new ArrayList<>();
      }
      return JSON.parseArray(value, clz);
    }

  @SuppressWarnings(value="unchecked")
  private static <T> T cast(String value, Class<T> clz) {
    if (value == null) {
      return null;
    }
    if (clz.equals(Long.class)) {
      return (T) Long.valueOf(value);
    } else if (clz.equals(Integer.class)) {
      return (T) Integer.valueOf(value);
    } else if (clz.equals(String.class)) {
      return (T) value;
    } else if (clz.equals(Boolean.class)) {
      return (T) Boolean.valueOf(value);
    } else if (clz.equals(Double.class)) {
      return (T) Double.valueOf(value);
    } else if (clz.equals(Float.class)) {
      return (T) Float.valueOf(value);
    } else if (clz.equals(BigDecimal.class)) {
      return (T) BigDecimal.valueOf(Double.valueOf(value));
    } else {
      return null;
    }
  }


    public static String readJsonFile(String fileName) {
        String jsonStr = "";
        try {
            File jsonFile = new File(DEFAULT_PATH_PREFIX + fileName);
            FileReader fileReader = new FileReader(jsonFile);

            Reader reader = new InputStreamReader(new FileInputStream(jsonFile),"utf-8");
            int ch = 0;
            StringBuffer sb = new StringBuffer();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            fileReader.close();
            reader.close();
            jsonStr = sb.toString();
            return jsonStr;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
