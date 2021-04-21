package com.leiming.controller.center;

import cn.hutool.core.util.StrUtil;
import com.leiming.service.center.CenterUserService;
import com.leiming.utils.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Leiming
 */
@Api(value = "用户中心", tags = {"用户相关相关api接口"})
@RestController
@RequestMapping("center")
public class CenterController {
    @Resource
    private CenterUserService centerUserService;

    @ApiOperation(value = "根据用户id获取用户信息", notes = "根据用户id获取用户信息", httpMethod = "GET")
    @GetMapping("/userInfo")
    public JsonResult getUserInfo(String userId){
        if (StrUtil.isEmpty(userId)){
            return JsonResult.errorMsg("用户id不能为空");
        }
        return JsonResult.ok(centerUserService.queryUserInfo(userId));
    }
}
