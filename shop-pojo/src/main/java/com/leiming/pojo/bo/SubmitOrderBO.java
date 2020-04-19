package com.leiming.pojo.bo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 创建订单的BO对象
 * @author Leiming
 */
@Getter
@Setter
@ToString
public class SubmitOrderBO {

    private String userId;
    private String itemSpecIds;
    private String addressId;
    private Integer payMethod;
    private String leftMsg;
}
