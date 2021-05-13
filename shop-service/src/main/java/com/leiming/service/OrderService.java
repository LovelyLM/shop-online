package com.leiming.service;

import cn.hutool.core.util.StrUtil;
import com.leiming.enums.OrderStatusEnum;
import com.leiming.enums.YesOrNo;
import com.leiming.mapper.OrderItemsMapper;
import com.leiming.mapper.OrderStatusMapper;
import com.leiming.mapper.OrdersMapper;
import com.leiming.pojo.*;
import com.leiming.pojo.bo.ShopcartBO;
import com.leiming.pojo.bo.SubmitOrderBO;
import org.n3r.idworker.Sid;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Leiming
 */
@Service
public class OrderService {

    @Resource
    private OrdersMapper ordersMapper;
    @Resource
    private AddressService addressService;
    @Resource
    private ItemService itemService;
    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private Sid sid;
    @Resource
    private OrderItemsMapper orderItemsMapper;
    @Resource
    private OrderStatusMapper orderStatusMapper;
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public String createOrder(SubmitOrderBO submitOrderBO,  List<ShopcartBO> shopCartBOList){
        //获取id数组
        String itemSpecIds = submitOrderBO.getItemSpecIds();

        //初始化金额
        Integer postAmount = 0;

        //初始化订单id
        String orderId = sid.nextShort();

        //查找用户地址
        UserAddress userAddress = addressService.queryUserAddress(submitOrderBO.getAddressId());

        //初始化订单相关信息
        Orders newOrder = Orders.builder()
                .id(orderId)
                .userId(submitOrderBO.getUserId())
                .receiverName(userAddress.getReceiver())
                .receiverMobile(userAddress.getMobile())
                .receiverAddress(userAddress.getProvince() + " " +userAddress.getCity() + " " + userAddress.getDistrict() + " " + userAddress.getDetail())
                .postAmount(postAmount)
                .payMethod(submitOrderBO.getPayMethod())
                .isComment(YesOrNo.NO.type)
                .leftMsg(submitOrderBO.getLeftMsg())
                .isDelete(YesOrNo.NO.type)
                .createdTime(new Date()).build();
        // 2. 循环根据itemSpecIds保存订单商品信息表
        String[] splitItemSpecIds = StrUtil.split(itemSpecIds, ",");
        int totalAmount = 0;
        int realPayAmount = 0;
        List<ShopcartBO> tobeRemoveFromRedis = new ArrayList<>();
        for (String itemSpecId : splitItemSpecIds) {
            int buyCounts = 0;
            //商品购买的数量重新从redis的购物车中获取
            List<ShopcartBO> list = shopCartBOList.stream().filter(shopCartBO -> shopCartBO.getSpecId().equals(itemSpecId)).collect(Collectors.toList());
            if (list.size() == 1){
                buyCounts =list.get(0).getBuyCounts();
                tobeRemoveFromRedis.add(list.get(0));
            }


            // 2.1 根据规格id，查询规格的具体信息，主要获取价格
            ItemsSpec itemSpec = itemService.queryItemSpecById(itemSpecId);
            totalAmount += itemSpec.getPriceNormal() * buyCounts;
            realPayAmount += itemSpec.getPriceDiscount() * buyCounts;

            // 2.2 根据商品id，获得商品信息以及商品图片
            String itemId = itemSpec.getItemId();
            Items item = itemService.queryItemById(itemId);
            String imgUrl = itemService.queryItemMainImgById(itemId);

            // 2.3 循环保存子订单数据到数据库
            String subOrderId = sid.nextShort();
            OrderItems subOrderItem = new OrderItems();
            subOrderItem.setId(subOrderId);
            subOrderItem.setOrderId(orderId);
            subOrderItem.setItemId(itemId);
            subOrderItem.setItemName(item.getItemName());
            subOrderItem.setItemImg(imgUrl);
            subOrderItem.setBuyCounts(buyCounts);
            subOrderItem.setItemSpecId(itemSpecId);
            subOrderItem.setItemSpecName(itemSpec.getName());
            subOrderItem.setPrice(itemSpec.getPriceDiscount());
            orderItemsMapper.insert(subOrderItem);

            // 2.4 在用户提交订单以后，规格表中需要扣除库存
            itemService.decreaseItemSpecStock(itemSpecId, buyCounts);
        }

        //设置付款金额信息
        newOrder.setTotalAmount(totalAmount);
        newOrder.setRealPayAmount(realPayAmount);
        //设置时间
        newOrder.setUpdatedTime(new Date());
        newOrder.setCreatedTime(new Date());
        ordersMapper.insert(newOrder);

        OrderStatus waitOrderStatus = OrderStatus.builder()
                .orderId(orderId)
                .orderStatus(OrderStatusEnum.WAIT_APY.type)
                .createdTime(new Date()).build();
        orderStatusMapper.insert(waitOrderStatus);
        //设置需要删除的购物车信息
        submitOrderBO.setTobeRemoveFromRedis(tobeRemoveFromRedis);
        return orderId;

    }

}
