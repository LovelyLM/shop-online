package com.leiming.controller;

import cn.hutool.json.JSONUtil;
import com.leiming.enums.PayMethod;
import com.leiming.pojo.bo.SubmitOrderBO;
import com.leiming.service.AddressService;
import com.leiming.service.OrderService;
import com.leiming.utils.CookieUtils;
import com.leiming.utils.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Leiming
 */
@Api(value = "订单相关", tags = {"订单相关的api接口"})
@RestController
@RequestMapping("orders")
@Slf4j
public class OrdersController {
    @Autowired
    private AddressService addressService;

    @Autowired
    private OrderService orderService;
    @ApiOperation(value = "用户下单，创建新订单", notes = "创建新订单", httpMethod = "POST")
    @PostMapping("/create")
    public JsonResult list(@RequestBody SubmitOrderBO submitOrderBO,
                           HttpServletRequest request,
                           HttpServletResponse response){
        if (!submitOrderBO.getPayMethod().equals(PayMethod.ALIPAY.type) && !submitOrderBO.getPayMethod().equals(PayMethod.WEIXIN.type)){

            return JsonResult.errorMsg("支付方式不能为空");
        }
        String orderId = orderService.createOrder(submitOrderBO);
//        CookieUtils.deleteCookie(request, response, "shopCart");
        log.info("订单信息：{}", JSONUtil.toJsonPrettyStr(submitOrderBO));
        return JsonResult.ok(orderId);
    }


}
