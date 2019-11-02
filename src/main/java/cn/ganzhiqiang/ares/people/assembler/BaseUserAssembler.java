package cn.ganzhiqiang.ares.people.assembler;

import cn.ganzhiqiang.ares.common.UserBase;
import cn.ganzhiqiang.ares.count.model.BaseUserCount;
import cn.ganzhiqiang.ares.people.domain.UserDO;
import cn.ganzhiqiang.ares.people.domain.UserInfoDO;
import cn.ganzhiqiang.ares.people.dto.UserVO;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author nanxuan
 * @since 2018/2/5
 **/

public class BaseUserAssembler {


  public static UserVO toVo(UserDO userDO, UserInfoDO userInfoDO, BaseUserCount baseUserCount) {

    return UserVO.builder()
        .userBase(tobaseUser(userInfoDO))
        .telephone(userDO.getTelephone())
        .email(userDO.getEmail())
        .integral(userInfoDO.getIntegral() == null ? 0 : userInfoDO.getIntegral())
        .publishCount(baseUserCount.getPublishCount())
        .likeCount(baseUserCount.getLikeCount())
        .favoriteCount(baseUserCount.getFavoriteCount())
        .lookCount((long)(Math.random() * 100))
        .school(Optional.ofNullable(userInfoDO.getSchool()).orElse(1))
        .shipAddress(Optional.ofNullable(userInfoDO.getShipAddress()).orElse(""))
        .build();
  }

  public static UserBase tobaseUser(UserInfoDO userInfoDO) {
    if (userInfoDO == null) {
      return null;
    }
    return UserBase.builder()
        .id(userInfoDO.getId())
        .nickname(Optional.ofNullable(userInfoDO.getNickName()).orElse(""))
        .avatar(Optional.ofNullable(userInfoDO.getAvatarUrl()).orElse(""))
        .gender(Integer.parseInt(Optional.ofNullable(userInfoDO.getGender()).orElse("1")))
        .address(String.format("%s %s", Optional.ofNullable(userInfoDO.getProvince()).orElse(""),
            Optional.ofNullable(userInfoDO.getCity()).orElse("")))
        .build();
  }

  public static List<UserBase> tobaseUserList(List<UserInfoDO> userInfoDOS) {
    return userInfoDOS.stream()
        .map(BaseUserAssembler::tobaseUser)
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
  }

}
