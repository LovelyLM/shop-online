package com.leiming.controller;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.leiming.pojo.User;
import com.leiming.pojo.bo.UserBo;
import com.leiming.service.UserService;
import com.leiming.utils.CookieUtils;
import com.leiming.utils.JsonResult;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;

/**
 * @author LeiMing
 */
@RestController
@RequestMapping("passport")
@Validated
public class PassportController {
    @Resource
    private UserService userService;


    /**
     * 判断用户名是否存在
     * @param username 用户名
     * @return 统一返回对象
     */
    @ApiOperation(value = "判断用户名重复", notes = "判断用户名是否存在", httpMethod = "GET")
    @GetMapping("/usernameIsExist")
    public JsonResult usernameIsExist(@RequestParam @NotEmpty(message = "用户名不能为空") String username){

        boolean isExit = userService.queryUsernameIsExit(username);
        if (isExit) {
            return JsonResult.errorMsg("用户名已存在！");
        }

        return JsonResult.ok();
    }

    /**
     * 用户注册
     * @param userBo 用户注册信息
     * @return 统一返回对象
     */
    @ApiOperation(value = "用户注册", notes = "用户注册", httpMethod = "POST")
    @PostMapping("/register")
    public JsonResult register(@RequestBody @Validated UserBo userBo){
        //获取用户输入信息
        String password = StringUtils.deleteWhitespace(userBo.getPassword());
        String confirmPassword = StringUtils.deleteWhitespace(userBo.getConfirmPassword());
        String username = userBo.getUsername();

        //判断用户名是否存在
        boolean isExit = userService.queryUsernameIsExit(username);
        if (isExit) {
            return JsonResult.errorMsg("用户名已存在");
        }

        //判断两次输入密码是否一致
        if (!StringUtils.equals(password, confirmPassword)) {
            return JsonResult.errorMsg("两次密码不一致");
        }

        //创建用户
        User user = userService.createUser(userBo);


        return JsonResult.ok(user);
    }

    /**
     * 用户登录
     * @param userBo 用户登录信息
     * @return 统一返回对象
     */
    @ApiOperation(value = "用户登录", notes = "用户登录", httpMethod = "POST")
    @PostMapping("/login")
    public JsonResult login(@RequestBody @Validated UserBo userBo,
                            HttpServletRequest request,
                            HttpServletResponse response) {
        User user = userService.queryUserForLogin(userBo);
        if (ObjectUtil.isAllEmpty(user)) {
            return JsonResult.errorMsg("用户名或密码错误");
        }
        String userJson = JSONUtil.toJsonStr(user);
        //存入cookie
        CookieUtils.setCookie(request, response, "user", userJson, true);
        return JsonResult.ok(user);

    }

    /**
     * 注销登录
     * @return 统一返回对象
     */
    @PostMapping("/logout")
    public JsonResult logout(HttpServletResponse response,
                             HttpServletRequest request) {
        //清楚cookie
        CookieUtils.deleteCookie(request, response, "user");

        //TODO 生成用户token，存入redis会话
        //TODO 同步购物车数据

        return JsonResult.ok();
    }

}
