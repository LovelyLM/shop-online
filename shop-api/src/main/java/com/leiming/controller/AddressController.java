package com.leiming.controller;

import cn.hutool.core.util.StrUtil;
import com.leiming.pojo.UserAddress;
import com.leiming.pojo.bo.AddressBO;
import com.leiming.service.AddressService;
import com.leiming.utils.JsonResult;
import com.leiming.utils.MobileEmailUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "地址相关", tags = {"地址相关api接口"})
@RestController
@RequestMapping("address")
public class AddressController {
    @Autowired
    private AddressService addressService;

    @ApiOperation(value = "根据用户id查询收获地址", notes = "根据用户id查询收获地址", httpMethod = "POST")
    @PostMapping("/list")
    public JsonResult list(@RequestParam String userId){
        if (StrUtil.isBlank(userId)){
            return JsonResult.errorMsg("用户id为空！");
        }
        List<UserAddress> userAddressList = addressService.queryAll(userId);
        return JsonResult.ok(userAddressList);
    }

    @ApiOperation(value = "新增用户地址", notes = "新增用户地址", httpMethod = "POST")
    @PostMapping("/add")
    public JsonResult add(@RequestBody AddressBO addressBO){
        JsonResult checkAddress = checkAddress(addressBO);
        if (checkAddress.getStatus() != 200){
            return checkAddress;
        }
        addressService.addNewUserAddress(addressBO);
        return JsonResult.ok();
    }

    @ApiOperation(value = "修改用户地址", notes = "修改用户地址", httpMethod = "POST")
    @PostMapping("/update")
    public JsonResult update(@RequestBody AddressBO addressBO){
        JsonResult checkAddress = checkAddress(addressBO);
        if (checkAddress.getStatus() != 200){
            return checkAddress;
        }
        addressService.addNewUserAddress(addressBO);
        return JsonResult.ok();
    }

    @ApiOperation(value = "用户设置默认地址", notes = "用户设置默认地址", httpMethod = "POST")
    @PostMapping("/setDefalut")
    public JsonResult serDefault(@RequestParam String userId,
                                 @RequestParam String addressId){
        if (StrUtil.isBlank(userId) || StrUtil.isBlank(addressId)){
            return JsonResult.errorMsg("用户id或地址id不能为空");
        }
        addressService.updateUserAddressToBeDefault(userId, addressId);
        return JsonResult.ok();
    }


    /**
     * 检验地址合法性
     * @param addressBO
     * @return
     */
    private JsonResult checkAddress(AddressBO addressBO) {
        String receiver = addressBO.getReceiver();
        if (StringUtils.isBlank(receiver)) {
            return JsonResult.errorMsg("收货人不能为空");
        }
        if (receiver.length() > 12) {
            return JsonResult.errorMsg("收货人姓名不能太长");
        }

        String mobile = addressBO.getMobile();
        if (StringUtils.isBlank(mobile)) {
            return JsonResult.errorMsg("收货人手机号不能为空");
        }
        if (mobile.length() != 11) {
            return JsonResult.errorMsg("收货人手机号长度不正确");
        }
        boolean isMobileOk = MobileEmailUtils.checkMobileIsOk(mobile);
        if (!isMobileOk) {
            return JsonResult.errorMsg("收货人手机号格式不正确");
        }

        String province = addressBO.getProvince();
        String city = addressBO.getCity();
        String district = addressBO.getDistrict();
        String detail = addressBO.getDetail();
        if (StringUtils.isBlank(province) ||
                StringUtils.isBlank(city) ||
                StringUtils.isBlank(district) ||
                StringUtils.isBlank(detail)) {
            return JsonResult.errorMsg("收货地址信息不能为空");
        }
        return JsonResult.ok();
    }
}
