package com.leiming.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.leiming.enums.PayMethod;
import com.leiming.pojo.bo.ShopcartBO;
import com.leiming.pojo.bo.SubmitOrderBO;
import com.leiming.service.AddressService;
import com.leiming.service.OrderService;
import com.leiming.utils.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static com.leiming.utils.CookieUtils.deleteCookie;

/**
 * @author Leiming
 */
@Api(value = "订单相关", tags = {"订单相关的api接口"})
@RestController
@RequestMapping("orders")
@Slf4j
public class OrdersController {
    @Resource
    private AddressService addressService;
    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private OrderService orderService;
    @ApiOperation(value = "用户下单，创建新订单", notes = "创建新订单", httpMethod = "POST")
    @PostMapping("/create")
    public JsonResult list(@RequestBody SubmitOrderBO submitOrderBO,
                           HttpServletRequest request,
                           HttpServletResponse response) {
        if (!submitOrderBO.getPayMethod().equals(PayMethod.ALIPAY.type) && !submitOrderBO.getPayMethod().equals(PayMethod.WEIXIN.type)) {

            return JsonResult.errorMsg("支付方式不能为空");
        }

        //尝试从redis里面取购物车信息
        String shopCart = (String) redisTemplate.opsForValue().get("shopCart:" + submitOrderBO.getUserId());
        if (StrUtil.isNotEmpty(shopCart)) {
            JSONArray jsonArray = JSONUtil.parseArray(shopCart);
            List<ShopcartBO> shopcartBOList = JSONUtil.toList(jsonArray, ShopcartBO.class);
            String orderId = orderService.createOrder(submitOrderBO, shopcartBOList);
            //从cookie删除购物车信息
            deleteCookie(request, response, "shopcart");
            //从redis删除购物车信息
            shopcartBOList.removeAll(submitOrderBO.getTobeRemoveFromRedis());
            redisTemplate.opsForValue().set("shopCart:" + submitOrderBO.getUserId(), JSONUtil.toJsonStr(shopcartBOList));
            log.info("订单信息：{}", JSONUtil.toJsonPrettyStr(submitOrderBO));
            return JsonResult.ok(orderId);
        } else {
            return JsonResult.errorMsg("购物车信息为空，无法提交订单");
        }
    }



}
