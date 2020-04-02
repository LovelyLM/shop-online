package com.leiming.controller;
import cn.hutool.json.JSONUtil;
import com.leiming.pojo.Users;
import com.leiming.pojo.bo.UserBo;
import com.leiming.service.UserService;
import com.leiming.utils.CookieUtils;
import com.leiming.utils.JsonResult;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("passport")
public class PassportController {
    @Autowired
    private UserService userService;

    @ApiOperation(value = "判断用户名重复", notes = "判断用户名是否存在", httpMethod = "GET")
    @GetMapping("/usernameIsExist")
    public JsonResult usernameIsExist(@RequestParam String username){

        if (StringUtils.isBlank(username)) {
            return JsonResult.errorMsg("用户名不能为空！");
        }

        boolean isExit = userService.queryUsernameIsExit(username);
        if (isExit) {
            return JsonResult.errorMsg("用户名已存在！");
        }

        return JsonResult.ok();
    }

    @ApiOperation(value = "用户注册", notes = "用户注册", httpMethod = "POST")
    @PostMapping("/register")
    public JsonResult register(@RequestBody UserBo userBo){
        String password = StringUtils.deleteWhitespace(userBo.getPassword());
        String confirmPassword = StringUtils.deleteWhitespace(userBo.getConfirmPassword());
        String username = userBo.getUsername();


        //用户名和密码不能为空
        if (StringUtils.isBlank(username) ||
                StringUtils.isBlank(password)||
                StringUtils.isBlank(confirmPassword)){
            return JsonResult.errorMsg("密码或用户名不能为空");
        }

        //判断用户名是否存在
        boolean isExit = userService.queryUsernameIsExit(username);
        if (isExit) {
            return JsonResult.errorMsg("用户名已存在");
        }

        //判断密码是否一致
        if (!StringUtils.equals(password, confirmPassword)) {
            return JsonResult.errorMsg("两次密码不一致");
        }

        Users user = userService.createUser(userBo);


        return JsonResult.ok(user);
    }

    @ApiOperation(value = "用户登录", notes = "用户登录", httpMethod = "POST")
    @PostMapping("/login")
    public JsonResult login(@RequestBody UserBo userBo,
                            HttpServletRequest request,
                            HttpServletResponse response) {
        String password = userBo.getPassword();
        String username = userBo.getUsername();
        if (StringUtils.isBlank(username) ||
                StringUtils.isBlank(password)){
            return JsonResult.errorMsg("用户名或密码不能为空");
        }

        Users user = userService.queryUserForLogin(userBo);
        if (user == null) {
            return JsonResult.errorMsg("用户名或密码错误");

        }


        String userJson = JSONUtil.toJsonStr(user);

        CookieUtils.setCookie(request, response, "user", userJson, true);

        return JsonResult.ok(user);

    }

    @PostMapping("/logout")
    public JsonResult logout(@RequestParam String userId,
                             HttpServletResponse response,
                             HttpServletRequest request) {
        CookieUtils.deleteCookie(request, response, "user");

        //TODO

        return JsonResult.ok();
    }

}
