package com.leiming.controller.center;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import com.leiming.pojo.User;
import com.leiming.pojo.bo.center.CenterUserBO;
import com.leiming.pojo.vo.UserVO;
import com.leiming.service.center.CenterUserService;
import com.leiming.utils.CookieUtils;
import com.leiming.utils.JsonResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.redis.core.RedisTemplate;
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
    private final static String TOKEN_NAME = "lovelylm_token";

    @Resource
    private CenterUserService centerUserService;

    @Resource
    private RedisTemplate redisTemplate;

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
        //生成token并存入redis和cookie
        String token = IdUtil.randomUUID();
        redisTemplate.opsForValue().set(TOKEN_NAME + ":" + userInfo.getId(), token);
        UserVO userVO = new UserVO();
        BeanUtil.copyProperties(userInfo, userVO);
        userVO.setUserToken(token);
        return JsonResult.ok(userVO);
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
