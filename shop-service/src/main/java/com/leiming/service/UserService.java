package com.leiming.service;

import com.leiming.pojo.Users;
import com.leiming.pojo.bo.UserBo;

public interface UserService {

    /**
     * 判断用户名是否存在
     * @param id
     * @return boolean
     */
    boolean queryUsernameIsExit(String id);

    /**
     * 创建用户
     * @param  userBo
     * @return createUser
     */
    Users createUser(UserBo userBo);

    Users queryUserForLogin(UserBo userBo);




}
