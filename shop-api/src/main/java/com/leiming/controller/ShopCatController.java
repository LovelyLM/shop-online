package com.leiming.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.leiming.pojo.bo.ShopcartBO;
import com.leiming.utils.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Leiming
 */
@Api(value = "购物车接口controller", tags = {"购物车接口相关"})
@RestController
@RequestMapping("shopcart")
@Slf4j
@Validated
public class ShopCatController {
    @Resource
    private RedisTemplate redisTemplate;

    @ApiOperation(value = "添加商品到购物车", notes = "添加商品到购物车", httpMethod = "POST")
    @PostMapping("/add")
    public JsonResult add(@Valid @NotEmpty(message = "用户id不能为空！") @RequestParam String userId,
                          @RequestBody ShopcartBO shopcartBO,
                          HttpServletRequest request,
                          HttpServletResponse response){
        //尝试从redis里面取购物车信息
        String shopCart = (String)redisTemplate.opsForValue().get("shopCart:" + userId);
        //预先new一个List
        List<ShopcartBO> shopCartList = new ArrayList<>();
        //如果redis不存在购物车，就将信息存入List
        if (StrUtil.isEmpty(shopCart)){
            shopCartList.add(shopcartBO);
        }
        //如果redis存在购物车信息
        else {
            //先将String转换为jsonArray，再将jsonArray转换为java List对象
            JSONArray jsonArray = JSONUtil.parseArray(shopCart);
            shopCartList = JSONUtil.toList(jsonArray, ShopcartBO.class);
            boolean flag = true;
            for (ShopcartBO bo : shopCartList) {
                //如果redis里面的购物车信息包含新加入的，只需要在原有基础上的数量加一就行了
                if (bo.getSpecId().equals(shopcartBO.getSpecId())) {
                    bo.setBuyCounts(bo.getBuyCounts() + 1);
                    flag = false;
                }
            }
            //如果flag为true，说明redis里面购物车信息不包含新加入的，需要重新加入
            if (flag){
                shopCartList.add(shopcartBO);
            }
        }

        //统一更新redis购物车信息
        redisTemplate.opsForValue().set("shopCart:" + userId, JSONUtil.toJsonStr(shopCartList));

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
        //尝试从redis里面取购物车信息
        String shopCart = (String)redisTemplate.opsForValue().get("shopCart:" + userId);
        //如果redis里面有购物车信息
        if (StrUtil.isNotEmpty(shopCart)){
            JSONArray jsonArray = JSONUtil.parseArray(shopCart);
            List<ShopcartBO> shopcartBOList = JSONUtil.toList(jsonArray, ShopcartBO.class);
            //过滤掉需要删除的信息
            List<ShopcartBO> newShopCartBoList = shopcartBOList.stream().filter(shopcartBO -> !shopcartBO.getSpecId().equals(itemSpecId)).collect(Collectors.toList());
            //更新redis购物车信息
            redisTemplate.opsForValue().set("shopCart:" + userId, JSONUtil.toJsonStr(newShopCartBoList));
        }
        return JsonResult.ok();

    }
}
