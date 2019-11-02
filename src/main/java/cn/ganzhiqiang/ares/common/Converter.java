package cn.ganzhiqiang.ares.common;

import org.apache.commons.collections.CollectionUtils;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author nanxuan
 * @since 2017/12/16
 **/

public class Converter {

  private static ModelMapper modelMapper = new ModelMapper();

  public static <R> R toDo(Object dto, Class<R> clazz) {
    if (dto == null) {
      return null;
    }
    return modelMapper.map(dto, clazz);
  }

  public static <R> R toDto(Object ddo, Class<R> clazz) {
    if (ddo == null) {
      return null;
    }
    return modelMapper.map(ddo, clazz);
  }

  public static <R> List<R> toDtos(List<?> dos, Class<R> clazz) {
    if (CollectionUtils.isEmpty(dos)) {
      return new ArrayList<>();
    }

    return dos.stream()
        .map(obj -> toDto(obj, clazz))
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
  }

  public static <R> List<R> toDos(List<?> dtos, Class<R> clazz) {
    if (CollectionUtils.isEmpty(dtos)) {
      return new ArrayList<>();
    }

    return dtos.stream()
        .map(obj -> toDto(obj, clazz))
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
  }
}
