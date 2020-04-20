package com.leiming.service.center;

import cn.hutool.core.bean.BeanUtil;
import com.leiming.mapper.UsersMapper;
import com.leiming.pojo.Users;
import com.leiming.pojo.bo.center.CenterUserBO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 用户中心相关service
 * @author Leiming
 */
@Service
public class CenterUserService {

    @Autowired
    private UsersMapper usersMapper;

    /**
     * 根据用户id查询用户信息
     * @param userId
     * @return
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public Users queryUserInfo(String userId){
        Users user = usersMapper.selectByPrimaryKey(userId);
        /**
         * 将密码置空
         */
        user.setPassword(null);
        return user;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)

    public Users updateUserInfo(String userId, CenterUserBO centerUserBO){
        Users users = new Users();
        users.setUpdatedTime(new Date());
        users.setId(userId);
        BeanUtil.copyProperties(centerUserBO, users);
        usersMapper.updateByPrimaryKeySelective(users);
        return queryUserInfo(userId);
    }
}
