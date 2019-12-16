package cn.ganzhiqiang.ares.people.service;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import cn.ganzhiqiang.ares.common.Converter;
import cn.ganzhiqiang.ares.common.UserBase;
import cn.ganzhiqiang.ares.common.helper.GConfigHelper;
import cn.ganzhiqiang.ares.count.model.BaseUserCount;
import cn.ganzhiqiang.ares.count.service.CounterService;
import cn.ganzhiqiang.ares.common.utils.CaptchaUtil;
import cn.ganzhiqiang.ares.common.utils.HttpUtil;
import cn.ganzhiqiang.ares.people.assembler.BaseUserAssembler;
import cn.ganzhiqiang.ares.people.dao.PersonDao;
import cn.ganzhiqiang.ares.people.dao.SessionDao;
import cn.ganzhiqiang.ares.people.domain.UserDO;
import cn.ganzhiqiang.ares.people.domain.UserInfoDO;
import cn.ganzhiqiang.ares.people.dto.UserInfoDTO;
import cn.ganzhiqiang.ares.people.dto.UserSimpleDto;
import cn.ganzhiqiang.ares.people.dto.UserVO;
import cn.ganzhiqiang.ares.people.dto.WxSessionDTO;
import cn.ganzhiqiang.ares.staticdata.service.StaticDataService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author nanxuan
 * @since 2018/1/21
 **/

@Service
public class PersonService {

    private Logger logger = LoggerFactory.getLogger(PersonService.class);

    private final static String AUTHORIZATION_CODE = "authorization_code";

    /**
     * 接口地址，使用登录凭证 code 获取 session_key 和 openid。
     */
    private final static String API_URL = "https://api.weixin.qq.com/sns/jscode2session";

    /**
     * session生存时间, 24小时
     */
//  private final static long SESSION_TTL = 24 * 60 * 60 * 1000;

    private final static long SESSION_TTL = 2 * 60 * 1000;

    @Resource
    private ObjectMapper objectMapper;
    @Resource
    private SessionDao sessionDao;
    @Resource
    private PersonDao personDao;
    @Resource
    private TransactionTemplate transactionTpl;
    @Resource
    private CounterService counterService;
    @Resource
    private StaticDataService staticDataService;
    @Resource
    private GConfigHelper configHelper;

    public WxSessionDTO doRequest(String jsCode) {
        // 微信小程序ID
        String wxAppId = configHelper.getValueByKey("wx.app.id", String.class);
        //  微信小程序 SECRET
        String wxAppSecret = configHelper.getValueByKey("wx.app.secret", String.class);

        Map<String, String> params = new HashMap<>();
        params.put("appid", wxAppId);
        params.put("secret", wxAppSecret);
        params.put("js_code", jsCode);
        params.put("grant_type", AUTHORIZATION_CODE);
        // API_URL：https://api.weixin.qq.com/sns/jscode2session
        String response = HttpUtil.doGet(API_URL, params);
        if (response == null) {
            return null;
        }
        try {
            return objectMapper.readValue(response, WxSessionDTO.class);
        } catch (Exception e) {
            logger.info("query jscode2session error", e);
            return null;
        }
    }

    public boolean changeSchool(Integer uid, Integer schoolIndex) {
//    Map<Integer,String> schoolMap = staticDataService.querySchoolMap();
//    if (schoolMap.get(schoolIndex) == null) {
//      return false;
//    }
        return personDao.updateUserSchool(uid, schoolIndex);
    }

    public boolean changeTelePhone(Integer uid, String telephone) {
        return personDao.updateTelephone(uid, telephone);
    }

    public boolean updateEmail(Integer uid, String email) {
        return personDao.updateEmail(uid, email);
    }

    public boolean updateAuthStatus(Integer uid, int status) {
        return personDao.updateAuthStatus(uid, status);
    }

    public boolean updateShipAddress(Integer uid, String shipAdress) {
        return personDao.updateShipAddress(uid, shipAdress);
    }

    public UserSimpleDto findSimpleUserByUid(Integer uid) {
        UserDO userDO = personDao.findUser(uid);
        if (userDO == null) {
            return null;
        }
        return Converter.toDto(userDO, UserSimpleDto.class);
    }

    public UserVO findUserDetailByUid(Integer uid) {
        UserDO userDO = personDao.findUser(uid);
        if (userDO == null) {
            return null;
        }
        BaseUserCount baseUserCount = counterService.findUserCount(uid);
        UserInfoDO userInfoDO = personDao.findUserProfile(uid);
        if (userInfoDO == null) {
            return BaseUserAssembler.toVo(userDO, new UserInfoDO(uid), baseUserCount);
        }
        return BaseUserAssembler.toVo(userDO, userInfoDO, baseUserCount);
    }

    public UserBase findUserByTelephone(String telephone) {
        UserDO userDO = personDao.findUserByTelephone(telephone);
        if (userDO == null) {
            return null;
        }
        Integer uid = userDO.getId();
        UserInfoDO userInfoDO = personDao.findUserProfile(uid);
        return BaseUserAssembler.tobaseUser(userInfoDO);
    }

    public UserBase findUserByEmail(String email) {
        UserDO userDO = personDao.findUserByEmail(email);
        if (userDO == null) {
            return null;
        }
        UserInfoDO userInfoDO = personDao.findUserProfile(userDO.getId());
        return BaseUserAssembler.tobaseUser(userInfoDO);
    }

    public List<UserBase> queryUserBaseByIds(List<Integer> uids) {
        List<UserInfoDO> userDOS = personDao.queryUserProfile(uids);
        if (CollectionUtils.isEmpty(userDOS)) {
            return new ArrayList<>();
        }
        return BaseUserAssembler.tobaseUserList(userDOS);
    }

    public boolean isRegsiter(String openId) {
        if (personDao.findUserIdByOpenId(openId) == 0) {
            return false;
        } else {
            return true;
        }
    }

    public int findUserIdByOpenId(String openId) {
        return personDao.findUserIdByOpenId(openId);
    }

    public int insert(String openId) {
        Integer userId = transactionTpl.execute(transactionStatus -> {
            try {
                UserDO userDO = new UserDO();
                userDO.setOpenId(openId);
                userDO.setIsAuth(0);

                return personDao.insertUser(userDO);
            } catch (Exception e) {
                transactionStatus.setRollbackOnly();
                logger.info("insert user error", e);
                return 0;
            }
        });
        return userId;
    }

    public boolean updateUserProfile(Integer userId, UserInfoDTO userInfoDTO) {
        UserInfoDO userInfoDO = Converter.toDo(userInfoDTO, UserInfoDO.class);
        userInfoDO.setId(userId);
        return personDao.updateUserProfile(userInfoDO);
    }

    public boolean saveSession(String sessionId, WxSessionDTO wxSessionDTO) {
        String value = wxSessionDTO.getOpenid() + "_" + wxSessionDTO.getSessionKey();
        return sessionDao.saveSession(sessionId, value, SESSION_TTL);
    }

    public UserInfoDTO mapperUserInfo(String userStr) {
        try {
            return objectMapper.readValue(userStr, UserInfoDTO.class);
        } catch (Exception e) {
            return null;
        }
    }

    public BufferedImage getCaptcha(HttpServletResponse response) {
        String code = CaptchaUtil.genCaptcha(5);
        //把校验码转为图像
        BufferedImage image = CaptchaUtil.genCaptchaImg(code);

        return image;
    }

    public int findMaxId() {
        return personDao.findMaxId();
    }

    public int findMinId() {
        return personDao.findMinId();
    }

}
