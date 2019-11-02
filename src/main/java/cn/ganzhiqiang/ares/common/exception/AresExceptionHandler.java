package cn.ganzhiqiang.ares.common.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.ganzhiqiang.ares.common.enums.NapiRespStatus;
import cn.ganzhiqiang.ares.common.warpper.NapiRespWrapper;

/**
 * @author zqgan
 * @since 2019/3/31
 **/

@ControllerAdvice
public class AresExceptionHandler {

  private Logger logger = LoggerFactory.getLogger(AresExceptionHandler.class);

  @ResponseBody
  @ExceptionHandler(value = Exception.class)
  public NapiRespWrapper<String> exceptionHandler(Exception ex) {
    logger.info("ares request error", ex);
    return new NapiRespWrapper<>(NapiRespStatus.INNER_ERROR, ex.getMessage());
  }

}
