package cn.ganzhiqiang.ares.people.dao;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import cn.ganzhiqiang.ares.people.domain.UserDO;
import cn.ganzhiqiang.ares.people.domain.UserInfoDO;

import javax.annotation.Resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author nanxuan
 * @since 2018/1/28
 **/

@Repository
public class PersonDao {

    private static final String MAPPING_PREFIX = "user.";

    @Resource
    private SqlSessionTemplate readTpl;

    @Resource
    private SqlSessionTemplate writeTpl;

    public boolean isRegister(String openId) {
        return false;
    }

    public int findUserIdByOpenId(String openId) {
        Map<String, Object> params = new HashMap<>();
        params.put("openId", openId);
        Integer result = readTpl.selectOne(MAPPING_PREFIX + "findUserIdByOpenId", params);
        return result == null ? 0 : result;
    }

    public int insertUser(UserDO userDO) {
        int result = readTpl.insert(MAPPING_PREFIX + "insertUser", userDO);
        return result == 1 ? userDO.getId() : 0;
    }

    public boolean updateUserProfile(UserInfoDO userInfoDO) {
        if (findUserProfile(userInfoDO.getId()) == null) {
            return insertUserProfile(userInfoDO) > 0;
        }
        return writeTpl.update(MAPPING_PREFIX + "updateUserProfile", userInfoDO) > 0;
    }

    public int insertUserProfile(UserInfoDO userInfoDO) {
        return writeTpl.insert(MAPPING_PREFIX + "insertUserProfile", userInfoDO);
    }

    public UserDO findUser(int userId) {
        Map params = new HashMap();
        params.put("userId", userId);
        return readTpl.selectOne(MAPPING_PREFIX + "findUser", params);
    }

    public UserDO findUserByTelephone(String telephone) {
        Map params = new HashMap();
        params.put("telephone", telephone);
        return readTpl.selectOne(MAPPING_PREFIX + "findUserByTelephone", params);
    }

    public UserDO findUserByEmail(String email) {
        Map params = new HashMap();
        params.put("email", email);
        return readTpl.selectOne(MAPPING_PREFIX + "findUserByEmail", params);
    }

    public UserInfoDO findUserProfile(int userId) {
        Map params = new HashMap();
        params.put("userId", userId);
        return readTpl.selectOne(MAPPING_PREFIX + "findUserProfile", params);
    }

    public List<UserInfoDO> queryUserProfile(List<Integer> uids) {
        Map params = new HashMap();
        params.put("userIds", uids);
        return readTpl.selectList(MAPPING_PREFIX + "queryUserProfiles", params);
    }

    public boolean updateUserSchool(Integer userId, Integer schoolIndex) {
        Map params = new HashMap();
        params.put("schoolIndex", schoolIndex);
        params.put("userId", userId);
        writeTpl.update(MAPPING_PREFIX + "updateSchool", params);
        return true;
    }

    public boolean updateTelephone(Integer userId, String telephone) {
        Map<String, Object> params = new HashMap<>();
        params.put("telephone", telephone);
        params.put("userId", userId);
        writeTpl.update(MAPPING_PREFIX + "updateTelephone", params);
        return true;
    }

    public boolean updateEmail(Integer userId, String email) {
        Map<String, Object> params = new HashMap<>();
        params.put("email", email);
        params.put("userId", userId);
        writeTpl.update(MAPPING_PREFIX + "updateEmail", params);
        return true;
    }

    public boolean updateAuthStatus(Integer userId, int status) {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("authStatus", status);
        writeTpl.update(MAPPING_PREFIX + "updateAuthStatus", params);
        return true;
    }

    public boolean updateShipAddress(Integer userId, String shipAddress) {
        Map<String, Object> params = new HashMap<>();
        params.put("shipAddress", shipAddress);
        params.put("userId", userId);
        writeTpl.update(MAPPING_PREFIX + "updateShipAddress", params);
        return true;
    }

    public int findMinId() {
        return readTpl.selectOne(MAPPING_PREFIX + "findMinId");
    }

    public int findMaxId() {
        return readTpl.selectOne(MAPPING_PREFIX + "findMaxId");
    }

}
