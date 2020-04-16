package com.leiming.service;

import com.leiming.enums.Sex;
import com.leiming.mapper.UsersMapper;
import com.leiming.pojo.Users;
import com.leiming.pojo.bo.UserBo;
import com.leiming.utils.DateUtil;
import com.leiming.utils.MD5Utils;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import java.util.Date;

@Service
public class UserService {
    @Autowired
    private UsersMapper usersMapper;
    @Autowired
    private Sid sid;
    private static final String USER_FACE = "https://s1.ax1x.com/2020/03/31/GlMvMq.jpg";
    private static final String USER_BIRTHDAY = "1970-01-01";

    /**
     * 查询用户名是否存在
     * @param username
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public boolean queryUsernameIsExit(String username) {

        Example userExample = new Example(Users.class);
        Example.Criteria criteria = userExample.createCriteria();
        criteria.andEqualTo("username", username);

        Users user = usersMapper.selectOneByExample(userExample);
        return user != null;
    }

    /**
     * 用户注册
     * @param  userBo 前端传入的用户注册信息
     * @return 用户model对象
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Users createUser(UserBo userBo) {

        Users user = new Users();
        user.setUsername(userBo.getUsername());
        try {
            user.setPassword(MD5Utils.getMD5Str(userBo.getPassword()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        user.setFace(USER_FACE);

        user.setBirthday(DateUtil.stringToDate(USER_BIRTHDAY));

        user.setSex(Sex.SECRET.type);

        user.setUpdatedTime(new Date());

        user.setCreatedTime(new Date());

        user.setId(sid.nextShort());

        user.setNickname(userBo.getUsername());

        usersMapper.insert(user);
        return user;
    }

    /**
     * 用户登录
     * @param userBo 前端传入的用户登录信息
     * @return 用户model对象
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public Users queryUserForLogin(UserBo userBo) {
        Example userExample = new Example(Users.class);
        Example.Criteria criteria = userExample.createCriteria();
        criteria.andEqualTo("username", userBo.getUsername());
        try {
            criteria.andEqualTo("password", MD5Utils.getMD5Str(userBo.getPassword()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Users user = usersMapper.selectOneByExample(userExample);
        return user;
    }
}
