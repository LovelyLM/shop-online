package com.leiming.pojo.bo;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

/**
 * 2020/3/31 22:31
 * @author LeiMing
 */
@Getter
@Setter
public class UserBo {
    /**
     * 用户名
     */
    @NotEmpty(message = "用户名不能为空")
    private String username;
    /**
     * 密码
     */
    @NotEmpty(message = "密码不能为空")
    @Length(min = 6, max = 50, message = "密码位数至少为6位且不能超过50")
    private String password;

    /**
     * 确认密码
     */
    private String confirmPassword;
}
