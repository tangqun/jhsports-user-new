package com.jhsports.user.controller;

import com.jhsports.user.entity.RESTful;
import com.jhsports.user.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by TQ on 2017/11/22.
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @ApiOperation("获取短信码")
    @RequestMapping(value = "/getsmscode", method = RequestMethod.GET)
    public RESTful getSMSCode(int appId, String mobileNum) {
        return userService.getSMSCode(appId, mobileNum, httpServletRequest.getHeader("X-Forwarded-For"));
    }

    @ApiOperation("验证短信码")
    @RequestMapping(value = "/validatesmscode", method = RequestMethod.GET)
    public RESTful validateSMSCode(int appId, String mobileNum, String smsCode) {
        return userService.validateSMSCode(appId, mobileNum, smsCode, httpServletRequest.getHeader("X-Forwarded-For"));
    }

    @ApiOperation("验证手机号")
    @RequestMapping(value = "/validatemobilenum", method = RequestMethod.GET)
    public RESTful ValidateMobileNum(String mobileNum) {
        return userService.validateMobileNum(mobileNum);
    }

    @ApiOperation("验证九合Id")
    @RequestMapping(value = "/validateunionuserid", method = RequestMethod.GET)
    public RESTful validateUnionUserId(String unionUserId) {
        return userService.validateUnionUserId(unionUserId);
    }

    @ApiOperation("验证Token")
    @RequestMapping(value = "/validatetoken", method = RequestMethod.GET)
    public RESTful validateToken(String token) {
        return userService.validateToken(token);
    }

    @ApiOperation("注册")
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public RESTful register(int appId, String mobileNum, String smsCode, String password, String ip, int systemTypeId, String equipmentNum) {
        return userService.register(appId, mobileNum, smsCode, password, ip, systemTypeId, equipmentNum, httpServletRequest.getHeader("X-Forwarded-For"));
    }

    @ApiOperation("登陆")
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public RESTful login(int appId, String mobileNum, String password, String ip, int systemTypeId, String equipmentNum) {
        return userService.login(appId, mobileNum, password, ip, systemTypeId, equipmentNum, httpServletRequest.getHeader("X-Forwarded-For"));
    }

    @ApiOperation("注册并登陆")
    @RequestMapping(value = "/registerlogin", method = RequestMethod.GET)
    public RESTful registerLogin(int appId, String mobileNum, String smsCode, String password, String ip, int systemTypeId, String equipmentNum) {
        return userService.registerLogin(appId, mobileNum, smsCode, password, ip, systemTypeId, equipmentNum, httpServletRequest.getHeader("X-Forwarded-For"));
    }

    @ApiOperation("三方授权登陆")
    @RequestMapping(value = "/authorizedlogin", method = RequestMethod.GET)
    public RESTful authorizedLogin(int appId, String openId, int authorizedTypeId, String ip, int systemTypeId, String equipmentNum) {
        return userService.authorizedLogin(appId, openId, authorizedTypeId, ip, systemTypeId, equipmentNum);
    }

    @ApiOperation("三方授权登陆")
    @RequestMapping(value = "/authorizedlogin2", method = RequestMethod.GET)
    public RESTful authorizedLogin2(int appId, String openId, String unionId, int authorizedTypeId, String ip, int systemTypeId, String equipmentNum) {
        return userService.authorizedLogin2(appId, openId, unionId, authorizedTypeId, ip, systemTypeId, equipmentNum, httpServletRequest.getHeader("X-Forwarded-For"));
    }

    @ApiOperation("三方绑定手机号")
    @RequestMapping(value = "/binduser", method = RequestMethod.GET)
    public RESTful bindUser(String token, String mobileNum, String smsCode, String password) {
        return userService.bindUser(token, mobileNum, smsCode, password);
    }

    @ApiOperation("Token登陆")
    @RequestMapping(value = "/tokenlogin", method = RequestMethod.GET)
    public RESTful tokenLogin(String token, @RequestParam(value = "appId", required = false, defaultValue = "0" /* 可选不可null参数加默认值 */) int appId, String ip, @RequestParam(value = "systemTypeId", required = false, defaultValue = "0") int systemTypeId, String equipmentNum) {
        return userService.tokenLogin(token, appId, ip, systemTypeId, equipmentNum);
    }

    @ApiOperation("登出")
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public RESTful logOut(String token) {
        return userService.logOut(token);
    }

    @ApiOperation("还绑手机号")
    @RequestMapping(value = "/resetmobilenum", method = RequestMethod.GET)
    public RESTful resetMobileNum(String token, String password, String mobileNum, String smsCode) {
        return userService.resetMobileNum(token, password, mobileNum, smsCode);
    }

    @ApiOperation("重置密码")
    @RequestMapping(value = "/resetpassword", method = RequestMethod.GET)
    public RESTful resetPassword(int appId, String mobileNum, String smsCode, String password, String ip, int systemTypeId, String equipmentNum) {
        return userService.resetPassword(appId, mobileNum, smsCode, password, ip, systemTypeId, equipmentNum, httpServletRequest.getHeader("X-Forwarded-For"));
    }

    @ApiOperation("修改密码")
    @RequestMapping(value = "/modifypassword", method = RequestMethod.GET)
    public RESTful modifyPassword(String token, String oldPassword, String newPassword) {
        return userService.modifyPassword(token, oldPassword, newPassword);
    }

    @ApiOperation("通过Token获取用户信息")
    @RequestMapping(value = "/getbytoken", method = RequestMethod.GET)
    public RESTful getByToken(String token) {
        return userService.getByToken(token);
    }
}
