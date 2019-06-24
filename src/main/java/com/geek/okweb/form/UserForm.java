package com.geek.okweb.form;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;


@Data
public class UserForm implements Serializable {
    @NotBlank(message = "原密码不能为空")
    private String oldPassword;
    @Length(min = 6,message = "密码不少于6位")
    @NotBlank(message = "新密码不能为空")
    private String newPassword;
    @Length(min = 6,message = "密码不少于6位")
    @NotBlank(message = "确认密码不能为空")
    private String confirmPassword;

}
