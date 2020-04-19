package com.leiming.service;

import cn.hutool.core.bean.BeanUtil;
import com.leiming.enums.YesOrNo;
import com.leiming.mapper.UserAddressMapper;
import com.leiming.pojo.UserAddress;
import com.leiming.pojo.bo.AddressBO;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author Leiming
 */
@Service
public class AddressService {
    @Autowired
    private UserAddressMapper userAddressMapper;
    @Autowired
    private Sid sid;
    /**
     * 根据用户id查询用户所有地址
     * @param userId 用户id
     * @return List
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<UserAddress> queryAll(String userId){
        UserAddress userAddress = UserAddress.builder().userId(userId).build();
        return userAddressMapper.select(userAddress);
    }

    /**
     * 新增用户收货地址
     * @param addressBO 地址bo：前台传来的数据
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void addNewUserAddress(AddressBO addressBO){
        int isDefault = 0;
        List<UserAddress> userAddresses = queryAll(addressBO.getUserId());
        if (userAddresses == null || userAddresses.isEmpty()){
            isDefault = 1;
        }

        UserAddress newAddress = UserAddress.builder().id(sid.nextShort())
                                                        .isDefault(isDefault)
                                                        .createdTime(new Date())
                                                        .updatedTime(new Date()).build();
        BeanUtil.copyProperties(addressBO, newAddress);
        userAddressMapper.insert(newAddress);
    }

    /**
     * 将其他设为非默认地址，将选择的设为默认地址
     * @param userId 用户id
     * @param addressId 选择的地址id
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updateUserAddressToBeDefault(String userId, String addressId){
        UserAddress userAddress = UserAddress.builder()
                .userId(userId)
                .isDefault(YesOrNo.YSE.type)
                .build();
        //查询默认地址，设置为默认，保险起见可能有多个，所以用“select”方法，返回list
        List<UserAddress> userAddressList = userAddressMapper.select(userAddress);
        for (UserAddress ua : userAddressList) {
            ua.setIsDefault(YesOrNo.NO.type);
            userAddressMapper.updateByPrimaryKey(ua);
        }
        UserAddress defaultAddress = UserAddress.builder()
                .id(addressId)
                .userId(userId)
                .isDefault(YesOrNo.YSE.type).build();
        userAddressMapper.updateByPrimaryKeySelective(defaultAddress);
    }

    /**
     * 根据地址Id查询地址
     * @param addressId 地址id
     * @return UserAddress
     */
    public UserAddress queryUserAddress(String addressId){
        return userAddressMapper.selectByPrimaryKey(addressId);
    }

}
