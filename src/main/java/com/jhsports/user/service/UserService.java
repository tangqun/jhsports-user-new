package com.jhsports.user.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jhsports.user.config.ProjConfig;
import com.jhsports.user.entity.*;
import com.jhsports.user.entity.enums.*;
import com.jhsports.user.util.*;
import com.jhsports.user.mapper.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by TQ on 2017/11/22.
 */
@Service
public class UserService {
    @Autowired
    private SMSCodeMapper smsCodeMapper;

    @Autowired
    private UnionUserMapper unionUserMapper;

    @Autowired
    private AuthorizedUserMapper authorizedUserMapper;

    @Autowired
    private AppTokenMapper appTokenMapper;

    @Autowired
    private LoginLogMapper loginLogMapper;

    @Autowired
    private OperateLogMapper operateLogMapper;

    @Autowired
    private UpdateLogMapper updateLogMapper;

    @Autowired
    private List<App> appList;

    @Autowired
    private ProjConfig projConfig;

    private static Logger logger = Logger.getLogger(UserService.class);

    public RESTful getSMSCode(int appId, String mobileNum, String requestIP) {
         try {
             RESTful cv_MobileNum = clientValidateMobileNum(mobileNum);
             if (cv_MobileNum.getCode() != 0) {
                 return cv_MobileNum;
             }

             RESTful sv_AppId = serverValidateAppId(appId, requestIP);
             if (sv_AppId.getCode() != 0) {
                 return sv_AppId;
             }

             SMSCode smsCode = new SMSCode();
             smsCode.setAppId(appId);
             smsCode.setMobileNum(mobileNum);
             String codeValue = RandomHelper.getRandom(6);
             smsCode.setCodeValue(codeValue);
             Date date = new Date();
             smsCode.setCreateTime(date);

             smsCodeMapper.insert(smsCode);

             RESTful resTful = RESTful.Success(CodeEnum.Success);
             resTful.setSmsCode(codeValue);

             return resTful;
         } catch (Exception e) {
             logger.error(e.getMessage(), e);
             return RESTful.Fail(CodeEnum.SystemException);
         }
    }

    public RESTful validateSMSCode(int appId, String mobileNum, String code, String requestIP) {
        try {
            RESTful cv_MobileNum = clientValidateMobileNum(mobileNum);
            if (cv_MobileNum.getCode() != 0) {
                return cv_MobileNum;
            }

            RESTful cv_SMSCode = clientValidateSMSCode(code);
            if (cv_SMSCode.getCode() != 0) {
                return cv_SMSCode;
            }

            RESTful sv_AppId = serverValidateAppId(appId, requestIP);
            if (sv_AppId.getCode() != 0) {
                return sv_AppId;
            }

            Date dt = new Date();
            Date dt2 = new Date(dt.getTime() - 30 * 60 * 1000);

            return serverValidateSMSCode(appId, mobileNum, code, dt2);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return RESTful.Fail(CodeEnum.SystemException);
        }
    }

    public RESTful validateMobileNum(String mobileNum) {
        try {
            RESTful cv_MobileNum = clientValidateMobileNum(mobileNum);
            if (cv_MobileNum.getCode() != 0) {
                return cv_MobileNum;
            }

            UnionUser unionUser = unionUserMapper.selectByMobileNum(mobileNum);
            if (unionUser == null) {
                return new RESTful(CodeEnum.Success);
            }

            return new RESTful(CodeEnum.MobileNumHasBeenRegistered);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return RESTful.Fail(CodeEnum.SystemException);
        }
    }

    public RESTful validateUnionUserId(String unionUserId) {
        try {
            RESTful cv_UnionUserId = clientValidateUnionUserId(unionUserId);
            if (cv_UnionUserId.getCode() != 0) {
                return cv_UnionUserId;
            }

            UnionUser unionUser = unionUserMapper.selectById(unionUserId);
            if (unionUser != null) {
                return new RESTful(CodeEnum.Success);
            }

            return new RESTful(CodeEnum.UnionUserIdInvalid);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return RESTful.Fail(CodeEnum.SystemException);
        }
    }

    public RESTful validateToken(String token) {
        try {
            RESTful cv_Token = clientValidateToken(token);
            if (cv_Token.getCode() != 0) {
                return cv_Token;
            }

            Date dt = new Date();

            AppToken appToken = appTokenMapper.selectByTokenValue(token);
            return serverValidateToken(appToken);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new RESTful(CodeEnum.SystemException);
        }
    }

    public RESTful register(int appId, String mobileNum, String code, String password, String ip, int systemTypeId, String equipmentNum, String requestIP) {
        return new RESTful(CodeEnum.AppVersionTooLow);
    }

    public RESTful login(int appId, String mobileNum, String password, String ip, int systemTypeId, String equipmentNum, String requestIP) {
        try {
            RESTful cv_MobileNum = clientValidateMobileNum(mobileNum);
            if (cv_MobileNum.getCode() != 0) {
                return cv_MobileNum;
            }

            RESTful cv_Password = clientValidatePassword(password);
            if (cv_Password.getCode() != 0) {
                return cv_Password;
            }

            RESTful cv_IP = clientValidateIP(ip);
            if (cv_IP.getCode() != 0) {
                return cv_IP;
            }

            RESTful cv_SystemTypeId = clientValidateSystemTypeId(systemTypeId);
            if (cv_SystemTypeId.getCode() != 0) {
                return cv_SystemTypeId;
            }

            RESTful cv_EquipmentNum = clientValidateEquipmentNum(equipmentNum);
            if (cv_EquipmentNum.getCode() != 0) {
                return cv_EquipmentNum;
            }

            UnionUser unionUser = unionUserMapper.selectByMobileNum(mobileNum);
            if (unionUser == null) {
                return new RESTful(CodeEnum.MobileNumHasNotBeenRegistered);
            }

            if (unionUser.getState() != StateEnum.Normal.getState()) {
                return new RESTful(CodeEnum.AccountHasBeenFrozen);
            }

            password = PasswordHelper.encrypto(password, unionUser.getSalt(), unionUser.getIsOld());

            if (!unionUser.getPassword().equals(password)) {
                return new RESTful(CodeEnum.PasswordInvalid);
            }

            Date dt = new Date();

            String tokenValue = UUID.randomUUID().toString();

            Map<String, Object> map = new HashMap<>();
            map.put("appId", appId);
            map.put("unionUserId", unionUser.getId());
            AppToken appToken = appTokenMapper.selectByAppId_UnionUserId(map);
            if (appToken != null) {
                appToken.setAuthorizedTypeId(AuthorizedTypeEnum.Mobile.getAuthorizedType());
                appToken.setTokenValue(tokenValue);
                appToken.setCreateIP(ip);
                appToken.setSystemTypeId(systemTypeId);
                appToken.setEquipmentNum(equipmentNum);
                appToken.setUpdateTime(dt);
                // 解禁强制恢复
                appToken.setIsWork(true);

                appTokenMapper.update(appToken);
            } else {
                appToken = new AppToken();
                // +
                appToken.setAppId(appId);

                appToken.setAuthorizedTypeId(AuthorizedTypeEnum.Mobile.getAuthorizedType());

                // +
                appToken.setUnionUserId(unionUser.getId());

                appToken.setTokenValue(tokenValue);

                // +
                appToken.setCreateTime(dt);

                appToken.setCreateIP(ip);
                appToken.setSystemTypeId(systemTypeId);
                appToken.setEquipmentNum(equipmentNum);
                appToken.setUpdateTime(dt);
                // token是否有效只与 IsWork 和 UpdateTime 29天 相关，封禁 Token 并不会封禁该用户吗如需封禁用户，请设置 UnionUser表 State 为冻结状态
                appToken.setIsWork(true);

                appTokenMapper.insert(appToken);
            }

            LoginLog loginLog = new LoginLog();
            loginLog.setAppId(appId);
            loginLog.setUnionUserId(unionUser.getId());
            loginLog.setLoginTypeId(AuthorizedTypeEnum.Mobile.getAuthorizedType());
            loginLog.setLoginTime(dt);
            loginLog.setLoginIP(ip);
            loginLog.setSystemTypeId(systemTypeId);
            loginLog.setEquipmentNum(equipmentNum);
            loginLogMapper.insert(loginLog);

            return new RESTful(CodeEnum.Success);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new RESTful(CodeEnum.SystemException);
        }
    }

    public RESTful registerLogin(int appId, String mobileNum, String code, String password, String ip, int systemTypeId, String equipmentNum, String requestIP) {
        try {
            RESTful cv_MobileNum = clientValidateMobileNum(mobileNum);
            if (cv_MobileNum.getCode() != 0) {
                return cv_MobileNum;
            }

            RESTful cv_SMSCode = clientValidateSMSCode(code);
            if (cv_SMSCode.getCode() != 0) {
                return cv_SMSCode;
            }

            RESTful cv_Password = clientValidatePassword(password);
            if (cv_Password.getCode() != 0) {
                return cv_Password;
            }

            RESTful cv_IP = clientValidateIP(ip);
            if (cv_IP.getCode() != 0) {
                return cv_IP;
            }

            RESTful cv_SystemTypeId = clientValidateSystemTypeId(systemTypeId);
            if (cv_SystemTypeId.getCode() != 0) {
                return cv_SystemTypeId;
            }

            RESTful cv_EquipmentNum = clientValidateEquipmentNum(equipmentNum);
            if (cv_EquipmentNum.getCode() != 0) {
                return cv_EquipmentNum;
            }

            Date dt = new Date();
            Date dt2 = new Date(dt.getTime() - 30 * 60 * 1000);

            UnionUser unionUser = unionUserMapper.selectByMobileNum(mobileNum);
            if (unionUser != null) {
                return new RESTful(CodeEnum.MobileNumHasBeenRegistered);
            }

            RESTful sv_SMSCode = serverValidateSMSCode(appId, mobileNum, code, dt2);
            if (sv_SMSCode.getCode() != 0) {
                return sv_SMSCode;
            }

            password = PasswordHelper.encrypto(password, "9hxyz", false);

            UnionUser unionUser2 = new UnionUser();
            String unionUserId = UUID.randomUUID().toString();
            unionUser2.setId(unionUserId);
            unionUser2.setAppId(appId);
            unionUser2.setMobileNum(mobileNum);
            unionUser2.setPassword(password);
            unionUser2.setSalt("9hxyz");
            unionUser2.setIsOld(false);
            unionUser2.setAuthorizedTypeId(AuthorizedTypeEnum.Mobile.getAuthorizedType());
            unionUser2.setBindState(BindStateEnum.HasBeenBound.getBindState());
            unionUser2.setState(StateEnum.Normal.getState());
            unionUser2.setCreateTime(dt);
            unionUserMapper.insert(unionUser2);

            AppToken appToken = new AppToken();
            String tokenValue = UUID.randomUUID().toString();
            appToken.setAppId(appId);
            appToken.setAuthorizedTypeId(AuthorizedTypeEnum.Mobile.getAuthorizedType());
            appToken.setUnionUserId(unionUserId);
            appToken.setTokenValue(tokenValue);
            appToken.setCreateTime(dt);
            appToken.setCreateIP(ip);
            appToken.setSystemTypeId(systemTypeId);
            appToken.setEquipmentNum(equipmentNum);
            appToken.setUpdateTime(dt);
            appToken.setIsWork(true);
            appTokenMapper.insert(appToken);

            LoginLog loginLog = new LoginLog();
            loginLog.setAppId(appId);
            loginLog.setUnionUserId(unionUserId);
            loginLog.setLoginTypeId(AuthorizedTypeEnum.Mobile.getAuthorizedType());
            loginLog.setLoginTime(dt);
            loginLog.setLoginIP(ip);
            loginLog.setSystemTypeId(systemTypeId);
            loginLog.setEquipmentNum(equipmentNum);
            loginLogMapper.insert(loginLog);

            RESTful resTful = new RESTful(CodeEnum.Success);
            resTful.setUnionUserId(unionUserId);
            resTful.setToken(tokenValue);
            return resTful;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new RESTful(CodeEnum.SystemException);
        }
    }

    public RESTful authorizedLogin(int appId, String openId, int authorizedTypeId, String ip, int systemTypeId, String equipmentNum) {
        return new RESTful(CodeEnum.AppVersionTooLow);
    }

    public RESTful authorizedLogin2(int appId, String openId, String unionId, int authorizedTypeId, String ip, int systemTypeId, String equipmentNum, String requestIP) {
        try {
            RESTful cv_OpenId = clientValidateOpenId(openId);
            if (cv_OpenId.getCode() != 0) {
                return cv_OpenId;
            }

            RESTful cv_UnionId = clientValidateUnionId(unionId);
            if (cv_UnionId.getCode() != 0) {
                return cv_UnionId;
            }

            RESTful cv_AuthorizedTypeId = clientValidateAuthorizedTypeId(authorizedTypeId);
            if (cv_AuthorizedTypeId.getCode() != 0) {
                return cv_AuthorizedTypeId;
            }

            RESTful cv_IP = clientValidateIP(ip);
            if (cv_IP.getCode() != 0) {
                return cv_IP;
            }

            RESTful cv_SystemTypeId = clientValidateSystemTypeId(systemTypeId);
            if (cv_SystemTypeId.getCode() != 0) {
                return cv_SystemTypeId;
            }

            RESTful cv_EquipmentNum = clientValidateEquipmentNum(equipmentNum);
            if (cv_EquipmentNum.getCode() != 0) {
                return cv_EquipmentNum;
            }

            RESTful sv_AppId = serverValidateAppId(appId, requestIP);
            if (sv_AppId.getCode() != 0) {
                return sv_AppId;
            }

            Date dt = new Date();

            UnionUser unionUser = unionUserMapper.selectByUnionId(unionId);
            if (unionUser != null) {
                // 【存在】UnionUser
                Map<String, Object> map = new HashMap<>();
                map.put("appId", appId);
                map.put("openId", openId);
                AuthorizedUser authorizedUser = authorizedUserMapper.selectByAppId_OpenId(map);
                if (authorizedUser != null) {
                    // 【存在】UnionUser，【存在】AuthorizedUser
                    if (!ValidateHelper.isNullOrEmpty(authorizedUser.getUnionId())) {
                        //region【存在】UnionUser，【存在】AuthorizedUser，【存在】AuthorizedUser -> UnionId
                        String tokenValue = UUID.randomUUID().toString();
                        Map<String, Object> map_AppToken = new HashMap<>();
                        map_AppToken.put("appId", appId);
                        map_AppToken.put("unionUserId", unionUser.getId());
                        map_AppToken.put("authorizedTypeId", authorizedTypeId);
                        map_AppToken.put("tokenValue", tokenValue);
                        map_AppToken.put("createIP", ip);
                        map_AppToken.put("systemTypeId", systemTypeId);
                        map_AppToken.put("equipmentNum", equipmentNum);
                        map_AppToken.put("updateTime", dt);
                        map_AppToken.put("isWork", true);
                        appTokenMapper.updateByAppId_UnionUserId(map_AppToken);

                        LoginLog loginLog = new LoginLog();
                        loginLog.setAppId(appId);
                        loginLog.setUnionUserId(unionUser.getId());
                        loginLog.setLoginTypeId(authorizedTypeId);
                        loginLog.setLoginTime(dt);
                        loginLog.setLoginIP(ip);
                        loginLog.setSystemTypeId(systemTypeId);
                        loginLog.setEquipmentNum(equipmentNum);
                        loginLogMapper.insert(loginLog);

                        RESTful resTful = new RESTful(CodeEnum.Success);
                        resTful.setUnionUserId(unionUser.getId());
                        resTful.setToken(tokenValue);
                        return resTful;
                        //endregion
                    } else {
                        //region【存在】UnionUser，【存在】AuthorizedUser，【不存在】AuthorizedUser -> UnionId
                        UnionUser oldUnionUser = unionUserMapper.selectByAuthorizedUserId(authorizedUser.getId());
                        if (oldUnionUser == null) {
                            logger.info("【存在】UnionUser，【存在】AuthorizedUser，【不存在】OpenId -> UnionId。数据异常AppId: " + appId + ", OpenId: " + openId + ", UnionId: " + unionId + "（不存在九合Id，多表插入失败）");
                            return new RESTful(CodeEnum.SystemException);
                        }

                        if (!ValidateHelper.isNullOrEmpty(unionUser.getMobileNum())) {
                            // 【存在】UnionUser，【存在】AuthorizedUser，【不存在】AuthorizedUser -> UnionId，【已绑定】UnionUser
                            if (!ValidateHelper.isNullOrEmpty(oldUnionUser.getMobileNum())) {
                                //region 【存在】UnionUser，【存在】AuthorizedUser，【不存在】AuthorizedUser -> UnionId，【已绑定】UnionUser，【已绑定】OldUnionUser
                                Map<String, Object> map_AuthorizedUser = new HashMap<>();
                                map_AuthorizedUser.put("unionId", unionId);
                                map_AuthorizedUser.put("id", authorizedUser.getId());
                                authorizedUserMapper.update(map_AuthorizedUser);

                                Map<String, Object> map_UnionUser = new HashMap<>();
                                map_UnionUser.put("bindState", BindStateEnum.HasBeenBound.getBindState());
                                map_UnionUser.put("updateTime", dt);
                                map_UnionUser.put("id", oldUnionUser.getId());
                                unionUserMapper.update(map_UnionUser);

                                String tokenValue = UUID.randomUUID().toString();
                                Map<String, Object> map_AppToken = new HashMap<>();
                                map_AppToken.put("unionUserId", unionUser.getId());
                                map_AppToken.put("authorizedTypeId", authorizedTypeId);
                                map_AppToken.put("tokenValue", tokenValue);
                                map_AppToken.put("createIP", ip);
                                map_AppToken.put("systemTypeId", systemTypeId);
                                map_AppToken.put("equipmentNum", equipmentNum);
                                map_AppToken.put("updateTime", dt);
                                map_AppToken.put("isWork", true);
                                map_AppToken.put("oldUnionUserId", oldUnionUser.getId());
                                map_AppToken.put("appId", appId);
                                appTokenMapper.updateByAppId_UnionUserId(map_AppToken);

                                Map<String, Object> map_AppToken2 = new HashMap<>();
                                map_AppToken2.put("unionUserId", oldUnionUser.getId());
                                map_AppToken2.put("appId", appId);
                                appTokenMapper.delete(map_AppToken2);

                                LoginLog loginLog = new LoginLog();
                                loginLog.setAppId(appId);
                                loginLog.setUnionUserId(unionUser.getId());
                                loginLog.setLoginTypeId(authorizedTypeId);
                                loginLog.setLoginTime(dt);
                                loginLog.setLoginIP(ip);
                                loginLog.setSystemTypeId(systemTypeId);
                                loginLog.setEquipmentNum(equipmentNum);
                                loginLogMapper.insert(loginLog);

                                RESTful resTful = new RESTful(CodeEnum.Success);
                                resTful.setUnionUserId(unionUser.getId());
                                resTful.setToken(tokenValue);
                                return resTful;
                                //endregion
                            } else {
                                //region【存在】UnionUser，【存在】AuthorizedUser，【不存在】AuthorizedUser -> UnionId，【已绑定】UnionUser，【未绑定】OldUnionUser
                                Map<String, Object> map_AuthorizedUser = new HashMap<>();
                                map_AuthorizedUser.put("unionId", unionId);
                                map_AuthorizedUser.put("id", authorizedUser.getId());
                                authorizedUserMapper.update(map_AuthorizedUser);

                                unionUserMapper.delete(oldUnionUser.getId());

                                String tokenValue = UUID.randomUUID().toString();
                                Map<String, Object> map_AppToken = new HashMap<>();
                                map_AppToken.put("unionUserId", unionUser.getId());
                                map_AppToken.put("authorizedTypeId", authorizedTypeId);
                                map_AppToken.put("tokenValue", tokenValue);
                                map_AppToken.put("createIP", ip);
                                map_AppToken.put("systemTypeId", systemTypeId);
                                map_AppToken.put("equipmentNum", equipmentNum);
                                map_AppToken.put("updateTime", dt);
                                map_AppToken.put("isWork", true);
                                map_AppToken.put("oldUnionUserId", oldUnionUser.getId());
                                map_AppToken.put("appId", appId);
                                appTokenMapper.updateByAppId_UnionUserId(map_AppToken);

                                LoginLog loginLog = new LoginLog();
                                loginLog.setAppId(appId);
                                loginLog.setUnionUserId(unionUser.getId());
                                loginLog.setLoginTypeId(authorizedTypeId);
                                loginLog.setLoginTime(dt);
                                loginLog.setLoginIP(ip);
                                loginLog.setSystemTypeId(systemTypeId);
                                loginLog.setEquipmentNum(equipmentNum);
                                loginLogMapper.insert(loginLog);

                                if (sendCallBack(oldUnionUser, unionUser, dt)) {
                                    RESTful resTful = new RESTful(CodeEnum.Success);
                                    resTful.setUnionUserId(unionUser.getId());
                                    resTful.setToken(tokenValue);
                                    return resTful;
                                } else {
                                    return new RESTful(CodeEnum.UpdateUnionUserIdFail);
                                }
                                //endregion
                            }
                        } else {
                            // 【存在】UnionUser，【存在】AuthorizedUser，【不存在】AuthorizedUser -> UnionId，【未绑定】UnionUser
                            if (!ValidateHelper.isNullOrEmpty(oldUnionUser.getMobileNum())) {
                                //region【存在】UnionUser，【存在】AuthorizedUser，【不存在】AuthorizedUser -> UnionId，【未绑定】UnionUser，【已绑定】OldUnionUser
                                Map<String, Object> map_AuthorizedUser = new HashMap<>();
                                map_AuthorizedUser.put("unionId", unionId);
                                map_AuthorizedUser.put("id", authorizedUser.getId());
                                authorizedUserMapper.update(map_AuthorizedUser);

                                unionUserMapper.delete(oldUnionUser.getId());

                                Map map_UnionUser = new HashMap();
                                map_UnionUser.put("mobileNum", oldUnionUser.getMobileNum());
                                map_UnionUser.put("password", oldUnionUser.getPassword());
                                map_UnionUser.put("salt", oldUnionUser.getSalt());
                                map_UnionUser.put("isOld", oldUnionUser.getIsOld());
                                map_UnionUser.put("bindState", BindStateEnum.HasBeenBound.getBindState());
                                map_UnionUser.put("updateTime", dt);
                                map_UnionUser.put("id", unionUser.getId());
                                unionUserMapper.bindUser(map_UnionUser);

                                String tokenValue = UUID.randomUUID().toString();
                                Map<String, Object> map_AppToken = new HashMap<>();
                                map_AppToken.put("unionUserId", unionUser.getId());
                                map_AppToken.put("authorizedTypeId", authorizedTypeId);
                                map_AppToken.put("tokenValue", tokenValue);
                                map_AppToken.put("createIP", ip);
                                map_AppToken.put("systemTypeId", systemTypeId);
                                map_AppToken.put("equipmentNum", equipmentNum);
                                map_AppToken.put("updateTime", dt);
                                map_AppToken.put("isWork", true);
                                map_AppToken.put("oldUnionUserId", oldUnionUser.getId());
                                map_AppToken.put("appId", appId);
                                appTokenMapper.updateByAppId_UnionUserId(map_AppToken);

                                Map<String, Object> map_AppToken2 = new HashMap<>();
                                map_AppToken2.put("unionUserId", oldUnionUser.getId());
                                map_AppToken2.put("appId", appId);
                                appTokenMapper.delete(map_AppToken2);

                                LoginLog loginLog = new LoginLog();
                                loginLog.setAppId(appId);
                                loginLog.setUnionUserId(unionUser.getId());
                                loginLog.setLoginTypeId(authorizedTypeId);
                                loginLog.setLoginTime(dt);
                                loginLog.setLoginIP(ip);
                                loginLog.setSystemTypeId(systemTypeId);
                                loginLog.setEquipmentNum(equipmentNum);
                                loginLogMapper.insert(loginLog);

                                if (sendCallBack(oldUnionUser, unionUser, dt)) {
                                    RESTful resTful = new RESTful(CodeEnum.Success);
                                    resTful.setUnionUserId(unionUser.getId());
                                    resTful.setToken(tokenValue);
                                    return resTful;
                                } else {
                                    return new RESTful(CodeEnum.UpdateUnionUserIdFail);
                                }
                                //endregion
                            } else {
                                //region【存在】UnionUser，【存在】AuthorizedUser，【不存在】AuthorizedUser -> UnionId，【未绑定】UnionUser，【未绑定】OldUnionUser
                                Map<String, Object> map_AuthorizedUser = new HashMap<>();
                                map_AuthorizedUser.put("unionId", unionId);
                                map_AuthorizedUser.put("id", authorizedUser.getId());
                                authorizedUserMapper.update(map_AuthorizedUser);

                                unionUserMapper.delete(oldUnionUser.getId());

                                String tokenValue = UUID.randomUUID().toString();
                                Map<String, Object> map_AppToken = new HashMap<>();
                                map_AppToken.put("unionUserId", unionUser.getId());
                                map_AppToken.put("authorizedTypeId", authorizedTypeId);
                                map_AppToken.put("tokenValue", tokenValue);
                                map_AppToken.put("createIP", ip);
                                map_AppToken.put("systemTypeId", systemTypeId);
                                map_AppToken.put("equipmentNum", equipmentNum);
                                map_AppToken.put("updateTime", dt);
                                map_AppToken.put("isWork", true);
                                map_AppToken.put("oldUnionUserId", oldUnionUser.getId());
                                map_AppToken.put("appId", appId);
                                appTokenMapper.updateByAppId_UnionUserId(map_AppToken);

                                LoginLog loginLog = new LoginLog();
                                loginLog.setAppId(appId);
                                loginLog.setUnionUserId(unionUser.getId());
                                loginLog.setLoginTypeId(authorizedTypeId);
                                loginLog.setLoginTime(dt);
                                loginLog.setLoginIP(ip);
                                loginLog.setSystemTypeId(systemTypeId);
                                loginLog.setEquipmentNum(equipmentNum);
                                loginLogMapper.insert(loginLog);

                                if (sendCallBack(oldUnionUser, unionUser, dt)) {
                                    RESTful resTful = new RESTful(CodeEnum.Success);
                                    resTful.setUnionUserId(unionUser.getId());
                                    resTful.setToken(tokenValue);
                                    return resTful;
                                } else {
                                    return new RESTful(CodeEnum.UpdateUnionUserIdFail);
                                }
                                //endregion
                            }
                        }
                        //endregion
                    }
                } else {
                    //region【存在】UnionUser，【不存在】AuthorizedUser
                    String authorizedUserId = UUID.randomUUID().toString();
                    authorizedUser = new AuthorizedUser();
                    authorizedUser.setId(authorizedUserId);
                    authorizedUser.setOpenId(openId);
                    authorizedUser.setUnionId(unionId);
                    authorizedUser.setAppId(appId);
                    authorizedUser.setAuthorizedTypeId(authorizedTypeId);
                    authorizedUser.setCreateTime(dt);
                    authorizedUser.setCreateIP(ip);
                    authorizedUser.setSystemTypeId(systemTypeId);
                    authorizedUser.setEquipmentNum(equipmentNum);
                    authorizedUserMapper.insert(authorizedUser);

                    AppToken appToken = new AppToken();
                    String tokenValue = UUID.randomUUID().toString();
                    appToken.setAppId(appId);
                    appToken.setAuthorizedTypeId(authorizedTypeId);
                    appToken.setUnionUserId(unionUser.getId());
                    appToken.setTokenValue(tokenValue);
                    appToken.setCreateTime(dt);
                    appToken.setCreateIP(ip);
                    appToken.setSystemTypeId(systemTypeId);
                    appToken.setEquipmentNum(equipmentNum);
                    appToken.setUpdateTime(dt);
                    appToken.setIsWork(true);
                    appTokenMapper.insert(appToken);

                    LoginLog loginLog = new LoginLog();
                    loginLog.setAppId(appId);
                    loginLog.setUnionUserId(unionUser.getId());
                    loginLog.setLoginTypeId(authorizedTypeId);
                    loginLog.setLoginTime(dt);
                    loginLog.setLoginIP(ip);
                    loginLog.setSystemTypeId(systemTypeId);
                    loginLog.setEquipmentNum(equipmentNum);
                    loginLogMapper.insert(loginLog);

                    RESTful resTful = new RESTful(CodeEnum.Success);
                    resTful.setUnionUserId(unionUser.getId());
                    resTful.setToken(tokenValue);
                    return resTful;
                    //endregion
                }
            } else {
                // 【不存在】UnionUser
                Map<String, Object> map = new HashMap<>();
                map.put("appId", appId);
                map.put("openId", openId);
                AuthorizedUser authorizedUser = authorizedUserMapper.selectByAppId_OpenId(map);
                if (authorizedUser != null) {
                    //region【不存在】UnionUser，【存在】AuthorizedUser
                    UnionUser unionUser2 = unionUserMapper.selectByAuthorizedUserId(authorizedUser.getId());
                    if (unionUser2 == null) {
                        String msg = "【不存在】UnionUser，【存在】AuthorizedUser。数据异常AppId: " + appId + ", OpenId: " + openId + ", UnionId: " + unionId + "（不存在九合Id，多表插入失败）";
                        logger.info(msg);

                        return new RESTful(CodeEnum.SystemException);
                    }

                    // 旧转新账号不存在被封禁的账号

                    Map<String, Object> map_AuthorizedUser = new HashMap<>();
                    map_AuthorizedUser.put("unionId", unionId);
                    map_AuthorizedUser.put("id", authorizedUser.getId());
                    authorizedUserMapper.update(map_AuthorizedUser);

                    Map<String, Object> map_UnionUser = new HashMap<>();
                    map_UnionUser.put("unionId", unionId);
                    map_UnionUser.put("updateTime", dt);
                    map_UnionUser.put("id", unionUser2.getId());
                    unionUserMapper.oldToNew(map_UnionUser);

                    String tokenValue = UUID.randomUUID().toString();
                    Map<String, Object> map_AppToken = new HashMap<>();
                    map_AppToken.put("appId", appId);
                    map_AppToken.put("unionUserId", unionUser2.getId());
                    map_AppToken.put("authorizedTypeId", authorizedTypeId);
                    map_AppToken.put("tokenValue", tokenValue);
                    map_AppToken.put("createIP", ip);
                    map_AppToken.put("systemTypeId", systemTypeId);
                    map_AppToken.put("equipmentNum", equipmentNum);
                    map_AppToken.put("updateTime", dt);
                    map_AppToken.put("isWork", true);
                    appTokenMapper.updateByAppId_UnionUserId(map_AppToken);

                    LoginLog loginLog = new LoginLog();
                    loginLog.setAppId(appId);
                    loginLog.setUnionUserId(unionUser2.getId());
                    loginLog.setLoginTypeId(authorizedTypeId);
                    loginLog.setLoginTime(dt);
                    loginLog.setLoginIP(ip);
                    loginLog.setSystemTypeId(systemTypeId);
                    loginLog.setEquipmentNum(equipmentNum);
                    loginLogMapper.insert(loginLog);

                    RESTful resTful = new RESTful(CodeEnum.Success);
                    resTful.setUnionUserId(unionUser2.getId());
                    resTful.setToken(tokenValue);
                    return resTful;
                    //endregion
                } else {
                    //region【不存在】UnionUser，【不存在】AuthorizedUser
                    authorizedUser = new AuthorizedUser();
                    String authorizedUserId = UUID.randomUUID().toString();
                    authorizedUser.setId(authorizedUserId);
                    authorizedUser.setOpenId(openId);
                    authorizedUser.setUnionId(unionId);
                    authorizedUser.setAppId(appId);
                    authorizedUser.setAuthorizedTypeId(authorizedTypeId);
                    authorizedUser.setCreateIP(ip);
                    authorizedUser.setCreateTime(dt);
                    authorizedUser.setSystemTypeId(systemTypeId);
                    authorizedUser.setEquipmentNum(equipmentNum);
                    authorizedUserMapper.insert(authorizedUser);

                    UnionUser unionUser2 = new UnionUser();
                    String unionUserId = UUID.randomUUID().toString();
                    unionUser2.setId(unionUserId);
                    unionUser2.setAppId(appId);
                    unionUser2.setIsOld(false);
                    unionUser2.setAuthorizedTypeId(authorizedTypeId);
                    unionUser2.setOpenId(openId);
                    // 新注册的用户不会再为AuthorizedUserId赋值
                    unionUser2.setUnionId(unionId);
                    unionUser2.setBindState(BindStateEnum.NotBind.getBindState());
                    unionUser2.setState(StateEnum.Normal.getState());
                    unionUser2.setCreateTime(dt);
                    unionUserMapper.insert(unionUser2);

                    AppToken appToken = new AppToken();
                    String tokenValue = UUID.randomUUID().toString();
                    appToken.setAppId(appId);
                    appToken.setAuthorizedTypeId(authorizedTypeId);
                    appToken.setUnionUserId(unionUserId);
                    appToken.setTokenValue(tokenValue);
                    appToken.setCreateTime(dt);
                    appToken.setCreateIP(ip);
                    appToken.setSystemTypeId(systemTypeId);
                    appToken.setEquipmentNum(equipmentNum);
                    appToken.setUpdateTime(dt);
                    appToken.setIsWork(true);
                    appTokenMapper.insert(appToken);

                    LoginLog loginLog = new LoginLog();
                    loginLog.setAppId(appId);
                    loginLog.setUnionUserId(unionUserId);
                    loginLog.setLoginTypeId(authorizedTypeId);
                    loginLog.setLoginTime(dt);
                    loginLog.setLoginIP(ip);
                    loginLog.setSystemTypeId(systemTypeId);
                    loginLog.setEquipmentNum(equipmentNum);
                    loginLogMapper.insert(loginLog);

                    RESTful resTful = new RESTful(CodeEnum.Success);
                    resTful.setUnionUserId(unionUserId);
                    resTful.setToken(tokenValue);
                    return resTful;
                    //endregion
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new RESTful(CodeEnum.SystemException);
        }
    }

    private boolean sendCallBack(UnionUser oldUnionUser, UnionUser unionUser, Date dt) {
        boolean isOk1 = false;
        boolean isOk2 = false;

        try {
            String resp = HttpHelper.post(projConfig.getCallbackCSL(), "{ \"oldNhId\": \"" + oldUnionUser.getId() + "\", \"newNhId\": \"" + unionUser.getId() + "\" }");
            if (!ValidateHelper.isNullOrEmpty(resp)) {
                ObjectMapper objectMapper = new ObjectMapper();
                ZhongChaoResp zhongChaoResp = objectMapper.readValue(resp, ZhongChaoResp.class);
                if (zhongChaoResp.getData() >= 0) {
                    UpdateLog updateLog = new UpdateLog();
                    updateLog.setOldUnionUserId(oldUnionUser.getId());
                    updateLog.setUnionUserId(unionUser.getId());
                    updateLog.setState(1);
                    updateLog.setCreateTime(dt);
                    updateLog.setRemark("此次更新 " + zhongChaoResp.getData() + " 条");
                    updateLogMapper.insert(updateLog);

                    isOk1 = true;
                } else {
                    UpdateLog updateLog = new UpdateLog();
                    updateLog.setOldUnionUserId(oldUnionUser.getId());
                    updateLog.setUnionUserId(unionUser.getId());
                    updateLog.setState(2);
                    updateLog.setCreateTime(dt);
                    updateLog.setRemark("中超 " + oldUnionUser.getId() + " 更新到 " + unionUser.getId() + " 失败，刘峰选服务器已返回，但提示此次更新条数为0（失败）");
                    updateLogMapper.insert(updateLog);
                }
            } else {
                UpdateLog updateLog = new UpdateLog();
                updateLog.setOldUnionUserId(oldUnionUser.getId());
                updateLog.setUnionUserId(unionUser.getId());
                updateLog.setState(2);
                updateLog.setCreateTime(dt);
                updateLog.setRemark("中超 " + oldUnionUser.getId() + " 更新到 " + unionUser.getId() + " 失败，刘峰选服务器未正确返回");
                updateLogMapper.insert(updateLog);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        try {
            String resp = HttpHelper.post(projConfig.getCallbackCSL() + oldUnionUser.getId() + "/" + unionUser.getId(), "{}");
            if (!ValidateHelper.isNullOrEmpty(resp)) {
                ObjectMapper objectMapper = new ObjectMapper();
                GuoAnResp guoAnResp = objectMapper.readValue(resp, GuoAnResp.class);
                if (guoAnResp.getErrCode() == 0) {
                    UpdateLog updateLog = new UpdateLog();
                    updateLog.setOldUnionUserId(oldUnionUser.getId());
                    updateLog.setUnionUserId(unionUser.getId());
                    updateLog.setState(1);
                    updateLog.setCreateTime(dt);
                    updateLog.setRemark("成功");
                    updateLogMapper.insert(updateLog);

                    isOk2 = true;
                } else {
                    UpdateLog updateLog = new UpdateLog();
                    updateLog.setOldUnionUserId(oldUnionUser.getId());
                    updateLog.setUnionUserId(unionUser.getId());
                    updateLog.setState(2);
                    updateLog.setCreateTime(dt);
                    updateLog.setRemark("国安 " + oldUnionUser.getId() + " 更新到 " + unionUser.getId() + " 失败，吉喆服务器已返回，但提示更新失败");
                    updateLogMapper.insert(updateLog);
                }
            } else {
                UpdateLog updateLog = new UpdateLog();
                updateLog.setOldUnionUserId(oldUnionUser.getId());
                updateLog.setUnionUserId(unionUser.getId());
                updateLog.setState(2);
                updateLog.setCreateTime(dt);
                updateLog.setRemark("国安 " + oldUnionUser.getId() + " 更新到 " + unionUser.getId() + " 失败，吉喆服务器未正确返回");
                updateLogMapper.insert(updateLog);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return isOk1 && isOk2;
    }

    public RESTful bindUser(String token, String mobileNum, String code, String password) {
        try {
            RESTful cv_Token = clientValidateToken(token);
            if (cv_Token.getCode() != 0) {
                return cv_Token;
            }

            RESTful cv_MobileNum = clientValidateMobileNum(mobileNum);
            if (cv_MobileNum.getCode() != 0) {
                return cv_MobileNum;
            }

            RESTful cv_SMSCode = clientValidateSMSCode(code);
            if (cv_SMSCode.getCode() != 0) {
                return cv_SMSCode;
            }

            RESTful cv_Password = clientValidatePassword(password);
            if (cv_Password.getCode() != 0) {
                return cv_Password;
            }

            AppToken appToken = appTokenMapper.selectByTokenValue(token);
            RESTful sv_AppToken = serverValidateToken(appToken);
            if (sv_AppToken.getCode() != 0) {
                return sv_AppToken;
            }

            Date dt = new Date();
            Date dt2 = new Date(dt.getTime() - 30 * 60 * 1000);

            RESTful sv_SMSCode = serverValidateSMSCode(appToken.getAppId(), mobileNum, code, dt2);
            if (sv_SMSCode.getCode() != 0) {
                return sv_SMSCode;
            }

            UnionUser unionUser = unionUserMapper.selectByMobileNum(mobileNum);
            if (unionUser != null) {
                return new RESTful(CodeEnum.MobileNumHasBeenRegistered);
            }

            UnionUser unionUser2 = unionUserMapper.selectById(appToken.getUnionUserId());
            if (!ValidateHelper.isNullOrEmpty(unionUser2.getMobileNum())) {
                return new RESTful(CodeEnum.WeChatHasBeenBoundToMobileNum);
            }

            if (ValidateHelper.isNullOrEmpty(unionUser2.getUnionId())) {
                return new RESTful(CodeEnum.AppVersionDoesNotSupportBindingMobileNum);
            }

            password = PasswordHelper.encrypto(password, "9hxyz", false);

            Map map_UnionUser = new HashMap();
            map_UnionUser.put("mobileNum", mobileNum);
            map_UnionUser.put("password", password);
            map_UnionUser.put("salt", "9hxyz");
            map_UnionUser.put("isOld", false);
            map_UnionUser.put("bindState", BindStateEnum.HasBeenBound.getBindState());
            map_UnionUser.put("updateTime", dt);
            map_UnionUser.put("id", appToken.getUnionUserId());
            unionUserMapper.bindUser(map_UnionUser);

            return new RESTful(CodeEnum.Success);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new RESTful(CodeEnum.SystemException);
        }
    }

    public RESTful tokenLogin(String token, int appId, String ip, int systemTypeId, String equipmentNum) {
        try {
            RESTful cv_Token = clientValidateToken(token);
            if (cv_Token.getCode() != 0) {
                return cv_Token;
            }

            Date dt = new Date();

            if ("00000000-0000-0000-0000-000000000000" == token) {
                // 匿名
                RESTful cv_IP = clientValidateIP(ip);
                if (cv_IP.getCode() != 0) {
                    return cv_IP;
                }

                RESTful cv_SystemTypeId = clientValidateSystemTypeId(systemTypeId);
                if (cv_SystemTypeId.getCode() != 0) {
                    return cv_SystemTypeId;
                }

                RESTful cv_EquipmentNum = clientValidateEquipmentNum(equipmentNum);
                if (cv_EquipmentNum.getCode() != 0) {
                    return cv_EquipmentNum;
                }

                LoginLog loginLog = new LoginLog();
                loginLog.setAppId(appId);
                loginLog.setUnionUserId("00000000-0000-0000-0000-000000000000");
                loginLog.setLoginTypeId(AuthorizedTypeEnum.Mobile.getAuthorizedType());
                loginLog.setLoginTime(dt);
                loginLog.setLoginIP(ip);
                loginLog.setSystemTypeId(systemTypeId);
                loginLog.setEquipmentNum(equipmentNum);
                loginLogMapper.insert(loginLog);

                return new RESTful(CodeEnum.Success);
            } else  {
                AppToken appToken = appTokenMapper.selectByTokenValue(token);
                RESTful sv_Token = serverValidateToken(appToken);
                if (sv_Token.getCode() != 0) {
                    return sv_Token;
                }

                Map<String, Object> map = new HashMap<>();
                map.put("updateTime", dt);
                map.put("tokenValue", appToken.getTokenValue());
                // 不检查用户是否被冻结，必须先提出token，再冻结用户，让用户重新登陆的时候提示被冻结
                appTokenMapper.renewal(map);

                LoginLog loginLog = new LoginLog();
                loginLog.setAppId(appToken.getAppId());
                loginLog.setUnionUserId(appToken.getUnionUserId());
                loginLog.setLoginTypeId(appToken.getAuthorizedTypeId());
                loginLog.setLoginTime(dt);
                loginLog.setLoginIP(ip);
                loginLog.setSystemTypeId(systemTypeId);
                loginLog.setEquipmentNum(equipmentNum);
                loginLogMapper.insert(loginLog);

                RESTful resTful = new RESTful(CodeEnum.Success);
                resTful.setUnionUserId(appToken.getUnionUserId());
                resTful.setToken(token);
                return resTful;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new RESTful(CodeEnum.SystemException);
        }
    }

    public RESTful logOut(String token) {
        try {
            RESTful cv_Token = clientValidateToken(token);
            if (cv_Token.getCode() != 0) {
                return cv_Token;
            }

            Date dt = new Date();

            AppToken appToken = appTokenMapper.selectByTokenValue(token);
            RESTful sv_AppToken = serverValidateToken(appToken);
            if (sv_AppToken.getCode() != 0) {
                return sv_AppToken;
            }

            Map<String, Object> map = new HashMap<>();
            map.put("updateTime", dt);
            map.put("tokenValue", token);
            appTokenMapper.expire(map);

            return new RESTful(CodeEnum.Success);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new RESTful(CodeEnum.SystemException);
        }
    }

    public RESTful resetMobileNum(String token, String password, String mobileNum, String code) {
        try {
            RESTful cv_Token = clientValidateToken(token);
            if (cv_Token.getCode() != 0) {
                return cv_Token;
            }

            RESTful cv_Password = clientValidatePassword(password);
            if (cv_Password.getCode() != 0) {
                return cv_Password;
            }

            RESTful cv_MobileNum = clientValidateMobileNum(mobileNum);
            if (cv_MobileNum.getCode() != 0) {
                return cv_MobileNum;
            }

            RESTful cv_SMSCode = clientValidateSMSCode(code);
            if (cv_SMSCode.getCode() != 0) {
                return cv_SMSCode;
            }

            AppToken appToken = appTokenMapper.selectByTokenValue(token);
            RESTful sv_Token = serverValidateToken(appToken);
            if (sv_Token.getCode() != 0) {
                // token失效将会被踢出
                return sv_Token;
            }

            Date dt = new Date();
            Date dt2 = new Date(dt.getTime() - 30 * 60 * 1000);

            RESTful sv_SMSCode = serverValidateSMSCode(appToken.getAppId(), mobileNum, code, dt2);
            if (sv_SMSCode.getCode() != 0) {
                return sv_SMSCode;
            }

            UnionUser unionUser = unionUserMapper.selectById(appToken.getUnionUserId());
            // 无需判断用户是否被冻结，全部交由登录时判断
            password = PasswordHelper.encrypto(password, "9hxyz", false);

            if (!unionUser.getPassword().equals(password)) {
                return new RESTful(CodeEnum.PasswordInvalid);
            }

            Map<String, Object> map = new HashMap<>();
            map.put("mobileNum", mobileNum);
            map.put("updateTime", dt);
            map.put("id", appToken.getUnionUserId());
            unionUserMapper.resetMobileNum(map);

            OperateLog operateLog = new OperateLog();
            operateLog.setAppId(appToken.getAppId());
            operateLog.setUnionUserId(appToken.getUnionUserId());
            operateLog.setOperateTypeId(OperateTypeEnum.ResetMobileNum.getOperateType());
            operateLog.setOperateTime(dt);
            operateLog.setOperateIP(appToken.getCreateIP());
            operateLog.setSystemTypeId(appToken.getSystemTypeId());
            operateLog.setEquipmentNum(appToken.getEquipmentNum());
            operateLogMapper.insert(operateLog);

            return new RESTful(CodeEnum.Success);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new RESTful(CodeEnum.SystemException);
        }
    }

    public RESTful resetPassword(int appId, String mobileNum, String code, String password, String ip, int systemTypeId, String equipmentNum, String requestIP) {
        try {
            RESTful cv_MobileNum = clientValidateMobileNum(mobileNum);
            if (cv_MobileNum.getCode() != 0) {
                return cv_MobileNum;
            }

            RESTful cv_SMSCode = clientValidateSMSCode(code);
            if (cv_SMSCode.getCode() != 0) {
                return cv_SMSCode;
            }

            RESTful cv_Password = clientValidatePassword(password);
            if (cv_Password.getCode() != 0) {
                return cv_Password;
            }

            RESTful cv_IP = clientValidateIP(ip);
            if (cv_IP.getCode() != 0) {
                return cv_IP;
            }

            RESTful cv_SystemTypeId = clientValidateSystemTypeId(systemTypeId);
            if (cv_SystemTypeId.getCode() != 0) {
                return cv_SystemTypeId;
            }

            RESTful cv_EquipmentNum = clientValidateEquipmentNum(equipmentNum);
            if (cv_EquipmentNum.getCode() != 0) {
                return cv_EquipmentNum;
            }

            RESTful sv_AppId = serverValidateAppId(appId, requestIP);
            if (sv_AppId.getCode() != 0) {
                return sv_AppId;
            }

            Date dt = new Date();
            Date dt2 = new Date(dt.getTime() - 30 * 60 * 1000);

            RESTful sv_SMSCode = serverValidateSMSCode(appId, mobileNum, code, dt2);
            if (sv_SMSCode.getCode() != 0) {
                return sv_SMSCode;
            }

            UnionUser unionUser = unionUserMapper.selectByMobileNum(mobileNum);
            if (unionUser == null) {
                return new RESTful(CodeEnum.MobileNumHasNotBeenRegistered);
            }

            password = PasswordHelper.encrypto(password, "9hxyz", false);

            Map<String, Object> map = new HashMap<>();
            map.put("password", password);
            map.put("updateTime", dt);
            map.put("id", unionUser.getId());
            unionUserMapper.resetPassword(map);

            OperateLog operateLog = new OperateLog();
            operateLog.setAppId(appId);
            operateLog.setUnionUserId(unionUser.getId());
            operateLog.setOperateTypeId(OperateTypeEnum.ResetPassword.getOperateType());
            operateLog.setOperateTime(dt);
            operateLog.setOperateIP(ip);
            operateLog.setSystemTypeId(systemTypeId);
            operateLog.setEquipmentNum(equipmentNum);
            operateLogMapper.insert(operateLog);

            return new RESTful(CodeEnum.Success);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new RESTful(CodeEnum.SystemException);
        }
    }

    public RESTful modifyPassword(String token, String oldPassword, String newPassword) {
        try {
            RESTful cv_Token = clientValidateToken(token);
            if (cv_Token.getCode() != 0) {
                return cv_Token;
            }

            RESTful cv_OldPassword = clientValidateOldPassword(oldPassword);
            if (cv_OldPassword.getCode() != 0) {
                return cv_OldPassword;
            }

            RESTful cv_NewPassword = clientValidateNewPassword(newPassword);
            if (cv_NewPassword.getCode() != 0) {
                return cv_NewPassword;
            }

            AppToken appToken = appTokenMapper.selectByTokenValue(token);
            RESTful sv_Token = serverValidateToken(appToken);
            if (sv_Token.getCode() != 0) {
                return sv_Token;
            }

            UnionUser unionUser = unionUserMapper.selectById(appToken.getUnionUserId());
            oldPassword = PasswordHelper.encrypto(oldPassword, unionUser.getSalt(), unionUser.getIsOld());
            if (!unionUser.getPassword().equals(oldPassword)) {
                return new RESTful(CodeEnum.PasswordInvalid);
            }

            Date dt = new Date();

            newPassword = PasswordHelper.encrypto(newPassword, "9hxyz", false);
            Map<String, Object> map = new HashMap<>();
            map.put("password", newPassword);
            map.put("updateTime", dt);
            map.put("id", unionUser.getId());
            unionUserMapper.resetPassword(map);

            return new RESTful(CodeEnum.Success);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new RESTful(CodeEnum.SystemException);
        }
    }

    public RESTful getByToken(String token) {
        try {
            RESTful cv_Token = clientValidateToken(token);
            if (cv_Token.getCode() != 0) {
                return cv_Token;
            }

            Date dt = new Date();

            AppToken appToken = appTokenMapper.selectByTokenValue(token);
            RESTful sv_AppToken = serverValidateToken(appToken);
            if (sv_AppToken.getCode() != 0) {
                return sv_AppToken;
            }

            UnionUser unionUser = unionUserMapper.selectById(appToken.getUnionUserId());

            RESTful resTful = new RESTful(CodeEnum.Success);
            resTful.setUnionUserId(unionUser.getId());
            resTful.setMobileNum(unionUser.getMobileNum());
            return resTful;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new RESTful(CodeEnum.SystemException);
        }
    }

    private RESTful serverValidateAppId(int appId, String requestIP) {
        // java lambda表达式
        App app = appList.stream().filter(o->o.getId()==appId).findFirst().get();
        if (app == null) {
            return new RESTful(CodeEnum.AppIdError);
        }
        if (app.getIpSet().contains(requestIP == null ? "127.0.0.1" : requestIP)) {
            return new RESTful(CodeEnum.Success);
        }
        return new RESTful(CodeEnum.ServerRefuseThisIPRequest);
    }

    private RESTful serverValidateSMSCode(int appId, String mobileNum, String code, Date dt2) {
        Map<String, Object> map = new HashMap<>();
        map.put("appId", appId);
        map.put("mobileNum", mobileNum);
        map.put("createTime", dt2);

        SMSCode smsCode = smsCodeMapper.selectByMobileNum(map);
        if (smsCode == null) {
            return new RESTful(CodeEnum.SMSCodeInvalid);
        }
        if (smsCode.getCodeValue().equals(code)) {
            return new RESTful(CodeEnum.Success);
        }
        return new RESTful(CodeEnum.SMSCodeInvalid);
    }

    private RESTful serverValidateToken(AppToken appToken) throws ParseException {
        if (appToken == null || !appToken.getIsWork())
        {
            return new RESTful(CodeEnum.TokenInvalid);
        }

        SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        String fromDate = simpleFormat.format(appToken.getUpdateTime());
        String toDate = simpleFormat.format(new Date());
        long from = simpleFormat.parse(fromDate).getTime();
        long to = simpleFormat.parse(toDate).getTime();
        int days = (int) ((to - from)/(1000 * 60 * 60 * 24));

        if (days < 29)
        {
            return new RESTful(CodeEnum.Success);
        }
        return new RESTful(CodeEnum.TokenInvalid);
    }

    private RESTful clientValidateSMSCode(String smsCode) {
        if (smsCode == null || smsCode.isEmpty()) {
            return RESTful.Fail(CodeEnum.SMSCodeCannotBeNULLOrEmpty);
        }

        if (ValidateHelper.SMSCode(smsCode)) {
            return RESTful.Success(CodeEnum.Success);
        }

        return RESTful.Fail(CodeEnum.SMSCodeFormatError);
    }

    private RESTful clientValidateMobileNum(String mobileNum) {
        if (mobileNum == null || mobileNum.isEmpty()) {
            return RESTful.Fail(CodeEnum.MobileNumCannotNULLOrEmpty);
        }

        if (ValidateHelper.MobileNum(mobileNum)) {
            return RESTful.Success(CodeEnum.Success);
        }

        return RESTful.Fail(CodeEnum.MobileNumFormatError);
    }

    private RESTful clientValidateUnionUserId(String unionUserId) {
        if (unionUserId == null || unionUserId.isEmpty()) {
            return RESTful.Fail(CodeEnum.UnionUserIdCannotBeNULLOrEmpty);
        }

        if (ValidateHelper.UnionUserId(unionUserId)) {
            return RESTful.Success(CodeEnum.Success);
        }

        return RESTful.Fail(CodeEnum.UnionUserIdFormatError);
    }

    private RESTful clientValidateToken(String token) {
        if (token == null || token.isEmpty()) {
            return RESTful.Fail(CodeEnum.TokenCannotBeNULLOrEmpty);
        }

        if (ValidateHelper.Token(token)) {
            return RESTful.Success(CodeEnum.Success);
        }

        return RESTful.Fail(CodeEnum.TokenFormatError);
    }

    private RESTful clientValidatePassword(String password) {
        if (password == null || password.isEmpty()) {
            return RESTful.Fail(CodeEnum.PasswordCannotBeNULLOrEmpty);
        }

        if (password.trim().length() == 32) {
            return RESTful.Success(CodeEnum.Success);
        }

        return RESTful.Fail(CodeEnum.PasswordFormatError);
    }

    private RESTful clientValidateOldPassword(String password) {
        if (password == null || password.isEmpty()) {
            return RESTful.Fail(CodeEnum.OldPasswordCannotBeNULLOrEmpty);
        }

        if (password.trim().length() == 32) {
            return RESTful.Success(CodeEnum.Success);
        }

        return RESTful.Fail(CodeEnum.OldPasswordFormatError);
    }

    private RESTful clientValidateNewPassword(String password) {
        if (password == null || password.isEmpty()) {
            return RESTful.Fail(CodeEnum.NewPasswordCannotBeNULLOrEmpty);
        }

        if (password.trim().length() == 32) {
            return RESTful.Success(CodeEnum.Success);
        }

        return RESTful.Fail(CodeEnum.NewPasswordFormatError);
    }

    private RESTful clientValidateIP(String ip) {
        if (ip == null || ip.isEmpty()) {
            return RESTful.Fail(CodeEnum.IPCannotBeNULLOrEmpty);
        }

        if (ValidateHelper.IP(ip)) {
            return RESTful.Success(CodeEnum.Success);
        }

        return RESTful.Fail(CodeEnum.IPFormatError);
    }

    private RESTful clientValidateSystemTypeId(int systemTypeId) {
        if (systemTypeId >= 1 && systemTypeId <= 4) {
            return RESTful.Success(CodeEnum.Success);
        }

        return RESTful.Fail(CodeEnum.SystemTypeIdError);
    }

    private RESTful clientValidateEquipmentNum(String equipmentNum) {
        if (equipmentNum == null) {
            equipmentNum = "";
        }

        if (equipmentNum.trim().length() <= 128) {
            return RESTful.Success(CodeEnum.Success);
        }

        return RESTful.Fail(CodeEnum.EquipmentNumFormatError);
    }

    private RESTful clientValidateOpenId(String openId) {
        if (openId == null || openId.isEmpty()) {
            return RESTful.Fail(CodeEnum.OpenIdCannotBeNULLOrEmpty);
        }

        return RESTful.Success(CodeEnum.Success);
    }

    private RESTful clientValidateUnionId(String unionId) {
        if (unionId == null || unionId.isEmpty()) {
            return RESTful.Fail(CodeEnum.UnionIdCannotBeNULLOrEmpty);
        }

        return RESTful.Success(CodeEnum.Success);
    }

    private RESTful clientValidateAuthorizedTypeId(int authorizedTypeId) {
        if (authorizedTypeId >= 1 && authorizedTypeId <= 3) {
            return RESTful.Success(CodeEnum.Success);
        }

        return RESTful.Fail(CodeEnum.AuthorizedTypeIdError);
    }
}
