package com.leiming.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.leiming.pojo.User;
import com.leiming.pojo.bo.ShopcartBO;
import com.leiming.pojo.bo.UserBo;
import com.leiming.pojo.vo.UserVO;
import com.leiming.service.UserService;
import com.leiming.utils.CookieUtils;
import com.leiming.utils.JsonResult;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author LeiMing
 */
@RestController
@RequestMapping("passport")
@Validated
public class PassportController extends BaseController{
    private final static String TOKEN_NAME = "lovelylm_token";
    @Resource
    private UserService userService;
    @Resource
    private RedisTemplate redisTemplate;


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
    @PostMapping("/regist")
    @Transactional(rollbackFor = Exception.class)
    public JsonResult register(@RequestBody @Validated UserBo userBo,
                               HttpServletRequest request,
                               HttpServletResponse response) throws UnsupportedEncodingException {
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
        //生成token并存入redis和cookie
        String token = IdUtil.randomUUID();
        redisTemplate.opsForValue().set(TOKEN_NAME + ":" + user.getId(), token);
        UserVO userVO = new UserVO();
        BeanUtil.copyProperties(user, userVO);
        userVO.setUserToken(token);
        CookieUtils.setCookie(request, response, "user", JSONUtil.toJsonStr(userVO), true);
        //同步购物车信息
        synchronizedShopData(user.getId(), request, response);

        return JsonResult.ok(userVO);
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
                            HttpServletResponse response) throws UnsupportedEncodingException {
        User user = userService.queryUserForLogin(userBo);
        if (ObjectUtil.isAllEmpty(user)) {
            return JsonResult.errorMsg("用户名或密码错误");
        }
        //生成token并存入redis和cookie
        String token = IdUtil.randomUUID();
        redisTemplate.opsForValue().set(TOKEN_NAME + ":" + user.getId(), token);
        UserVO userVO = new UserVO();
        BeanUtil.copyProperties(user, userVO);
        userVO.setUserToken(token);
        String userJson = JSONUtil.toJsonStr(user);
        //存入用户信息到cookie
        CookieUtils.setCookie(request, response, "user", userJson, true);
        //同步购物车信息
        synchronizedShopData(user.getId(), request, response);
        return JsonResult.ok(user);

    }

    /**
     * 注销登录
     * @return 统一返回对象
     */
    @PostMapping("/logout")
    public JsonResult logout(@RequestParam("userId") @NotEmpty(message = "用户id不能为空") String userId,
                             HttpServletResponse response,
                             HttpServletRequest request) {
        //清除cookie
        CookieUtils.deleteCookie(request, response, "user");
        //清除redis
        redisTemplate.delete(TOKEN_NAME + ":" + userId);


        return JsonResult.ok();
    }

    /**
     * 同步redis和cookie购物车信息
     * @param userId
     * @param request
     * @param response
     */
    private void synchronizedShopData(String userId, HttpServletRequest request,
                                      HttpServletResponse response) throws UnsupportedEncodingException {
        //尝试从redis里面取购物车信息
        List<ShopcartBO> redisShopCartBOList = (List<ShopcartBO>) redisTemplate.opsForValue().get("shopCart:" + userId);
        String cookieShopCart = CookieUtils.getCookieValue(request, "shopcart", true);
        JSONArray cookieJsonArray = JSONUtil.parseArray(cookieShopCart);
        List<ShopcartBO> cookieShopCartBOList = JSONUtil.toList(cookieJsonArray, ShopcartBO.class);
        //但redis为空，cookie不为空，直接将cookie信息存入redis
        if (ObjectUtil.isEmpty(redisShopCartBOList) && StrUtil.isNotEmpty(cookieShopCart)){
            redisTemplate.opsForValue().set("shopCart:" + userId, redisShopCartBOList);
        }
        //当都不为空，以cookie为准
        else if (ObjectUtil.isNotEmpty(redisShopCartBOList) && StrUtil.isNotEmpty(cookieShopCart)){
            List<ShopcartBO> shopCartBOList = cookieShopCartBOList.stream().peek(shopCart -> redisShopCartBOList.forEach(e -> {
                if (e.getSpecId().equals(shopCart.getSpecId())) {
                    shopCart.setBuyCounts(e.getBuyCounts() + shopCart.getBuyCounts());
                }
            })).collect(Collectors.toList());
            //同时更新cookie和redis
            String jsonStr = JSONUtil.toJsonStr(redisShopCartBOList);
            String encodeJson = URLEncoder.encode(jsonStr, "utf-8");
            CookieUtils.setCookie(request, response, "shopcart", encodeJson);
            redisTemplate.opsForValue().set("shopCart:" + userId, shopCartBOList);
        }
        //redis不为空，cookie为空，以redis为准
        else if (ObjectUtil.isNotEmpty(redisShopCartBOList) && StrUtil.isEmpty(cookieShopCart)){
            String jsonStr = JSONUtil.toJsonStr(redisShopCartBOList);
            String encodeJson = URLEncoder.encode(jsonStr, "utf-8");
            CookieUtils.setCookie(request, response, "shopcart", encodeJson);
        }


    }

}
