package com.leiming.service;

import com.leiming.enums.Sex;
import com.leiming.mapper.UsersMapper;
import com.leiming.pojo.User;
import com.leiming.pojo.bo.UserBo;
import com.leiming.utils.DateUtil;
import com.leiming.utils.MD5Utils;
import org.n3r.idworker.Sid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;


/**
 * @author Leiming
 */
@Service
public class UserService {
    @Resource
    private UsersMapper usersMapper;
    @Resource
    private Sid sid;
    private static final String USER_FACE = "https://s1.ax1x.com/2020/03/31/GlMvMq.jpg";
    private static final String USER_BIRTHDAY = "1970-01-01";

    /**
     * 查询用户名是否存在
     * @param username 用户名
     * @return boolean
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public boolean queryUsernameIsExit(String username) {

        //构造查询条件，与mybatis-plus类似
        Example userExample = new Example(User.class);
        Example.Criteria criteria = userExample.createCriteria();
        criteria.andEqualTo("username", username);
        //根据用户名查询用户
        User user = usersMapper.selectOneByExample(userExample);
        return user != null;
    }

    /**
     * 用户注册,保存用户
     * @param  userBo 前端传入的用户注册信息
     * @return 用户model对象
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public User createUser(UserBo userBo) {

        User user = new User();
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
     * @return Users 用户model对象
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public User queryUserForLogin(UserBo userBo) {
        //创造查询条件，根据用户名查询用户是否存在
        Example userExample = new Example(User.class);
        Example.Criteria criteria = userExample.createCriteria();
        criteria.andEqualTo("username", userBo.getUsername());
        //判断加密的密码是否相同
        try {
            criteria.andEqualTo("password", MD5Utils.getMD5Str(userBo.getPassword()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return usersMapper.selectOneByExample(userExample);
    }
}
