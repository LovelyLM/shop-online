package com.leiming.pojo.bo.center;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.util.Date;
/**
 * @author Leiming
 */
@Data
@ApiModel(value="用户对象", description="从客户端，由用户传入的数据封装在此entity中")
public class CenterUserBO {

    @ApiModelProperty(value = "用户名", name = "username", example = "json")
    private String username;
    @ApiModelProperty(value = "密码", name = "password", example = "123456")
    private String password;
    @ApiModelProperty(value = "确认密码", name = "confirmPassword", example = "123456")
    private String confirmPassword;


    @NotBlank(message = "用户昵称不能为空")
    @Length(max = 12, message = "用户昵称不能超过12位")
    @ApiModelProperty(value = "用户昵称", name = "nickname", example = "杰森")
    private String nickname;

    @Length(max = 12, message = "用户真实姓名不能超过12位")
    @ApiModelProperty(value = "真实姓名", name = "realName", example = "杰森")
    private String realname;

    @Pattern(regexp = "^(((13[0-9])|(15[0-9])|(18[0-9]))+\\d{8})$", message = "手机号格式不正确")
    @ApiModelProperty(value = "手机号", name = "mobile", example = "13999999999")
    private String mobile;

    @Email
    @ApiModelProperty(value = "邮箱地址", name = "email", example = "imooc@imooc.com")
    private String email;

    @Min(value = 0, message = "性别选择不正确")
    @Max(value = 2, message = "性别选择不正确")
    @ApiModelProperty(value = "性别", name = "sex", example = "0:女 1:男 2:保密")
    private Integer sex;
    @ApiModelProperty(value = "生日", name = "birthday", example = "1900-01-01")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    private Date birthday;
}