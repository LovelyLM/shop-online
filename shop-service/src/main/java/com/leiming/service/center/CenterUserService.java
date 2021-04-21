package com.leiming.service.center;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.leiming.mapper.UsersMapper;
import com.leiming.pojo.User;
import com.leiming.pojo.bo.center.CenterUserBO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 用户中心相关service
 * @author Leiming
 */
@Service
public class CenterUserService {

    @Resource
    private UsersMapper usersMapper;

    /**
     * 根据用户id查询用户信息
     * @param userId 用户id
     * @return 返回值
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public User queryUserInfo(String userId){
        User user = usersMapper.selectByPrimaryKey(userId);
        if(ObjectUtil.isEmpty(user)){
            return null;
        }

        //将密码置空
        user.setPassword(null);
        return user;
    }


    /**
     * 修改用户信息
     * @param userId 用户id
     * @param centerUserBO 用户信息
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public User updateUserInfo(String userId, CenterUserBO centerUserBO){
        User user = new User();
        user.setUpdatedTime(new Date());
        user.setId(userId);
        BeanUtil.copyProperties(centerUserBO, user);
        usersMapper.updateByPrimaryKeySelective(user);
        return queryUserInfo(userId);
    }
}
