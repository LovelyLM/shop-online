package com.leiming.controller.center;

import cn.hutool.json.JSONUtil;
import com.leiming.pojo.User;
import com.leiming.pojo.bo.center.CenterUserBO;
import com.leiming.service.center.CenterUserService;
import com.leiming.utils.CookieUtils;
import com.leiming.utils.JsonResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Leiming
 */
@RestController
@RequestMapping("userInfo")
public class CenterUserController {
    public final static String avatarImage = "";

    @Resource
    private CenterUserService centerUserService;

    @ApiOperation(value = "修改用户信息", notes = "修改用户信息", httpMethod = "POST")
    @PostMapping("/update")
    public JsonResult update(@RequestParam String userId,
                             @RequestBody @Valid CenterUserBO centerUserBO,
                             BindingResult bindingResult,
                             HttpServletResponse response,
                             HttpServletRequest request){
        if (bindingResult.hasErrors()){
            return JsonResult.errorMap(getErrors(bindingResult));
        }
        User userInfo = centerUserService.updateUserInfo(userId, centerUserBO);
        //修改cookie信息
        CookieUtils.setCookie(request, response, "user", JSONUtil.toJsonStr(userInfo), true);
        return JsonResult.ok();
    }

    @PostMapping("avatar")

    private Map<String, String> getErrors(BindingResult result){
        List<FieldError> fieldErrors = result.getFieldErrors();
        Map<String, String> map = new HashMap<>();
        for (FieldError error : fieldErrors) {
            map.put(error.getField(),error.getDefaultMessage());
        }
        return map;
    }
}
