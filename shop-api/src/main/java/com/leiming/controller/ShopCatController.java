package com.leiming.controller;

import cn.hutool.core.util.StrUtil;
import com.leiming.pojo.bo.ShopcartBO;
import com.leiming.utils.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Leiming
 */
@Api(value = "购物车接口controller", tags = {"购物车接口相关"})
@RestController
@RequestMapping("shopcart")
@Slf4j
public class ShopCatController {

    @ApiOperation(value = "添加商品到购物车", notes = "添加商品到购物车", httpMethod = "POST")
    @PostMapping("/add")
    public JsonResult add(@RequestParam String userId,
                          @RequestBody ShopcartBO shopcartBO,
                          HttpServletRequest request,
                          HttpServletResponse response){
        if (StrUtil.isBlank(userId)){
            return JsonResult.errorMsg("用户id为空");
        }

        //TODO 前端用户在登录的情况下添加商品苹到购物车，会同时在后端同步购物车到redis缓存
        log.info("购物车信息：{}",shopcartBO);

        return JsonResult.ok();

    }

    @ApiOperation(value = "从购物车中删除商品", notes = "添加商品到购物车", httpMethod = "POST")
    @PostMapping("/del")
    public JsonResult del(@RequestParam String userId,
                          @RequestParam String itemSpecId,
                          HttpServletRequest request,
                          HttpServletResponse response){
        if (StrUtil.isBlank(userId) || StrUtil.isBlank(itemSpecId)){
            return JsonResult.errorMsg("用户id或者商品id为空");
        }

        return JsonResult.ok();

    }
}
