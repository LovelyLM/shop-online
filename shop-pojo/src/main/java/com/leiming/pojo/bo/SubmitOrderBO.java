package com.leiming.pojo.bo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 创建订单的BO对象
 * @author Leiming
 */
@Getter
@Setter
@ToString
public class SubmitOrderBO {

    /**
     * 用户id
     */
    private String userId;

    private String itemSpecIds;
    private String addressId;
    private Integer payMethod;
    private String leftMsg;

    private List<ShopcartBO> tobeRemoveFromRedis;
}
