package org.timchen.live.user.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;


/**
 * @Author: Tim Chen
 * @Date: 16:47 2024-07-29
 * @Description:
 */
@Data
public class UserLoginDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -4290788036479984698L;

    private boolean isLoginSuccess;
    private String desc;
    private Long userId;

    public static UserLoginDTO loginError(String desc) {
        UserLoginDTO userLoginDTO = new UserLoginDTO();
        userLoginDTO.setLoginSuccess(false);
        userLoginDTO.setDesc(desc);
        return userLoginDTO;
    }

    public static UserLoginDTO loginSuccess(Long userId) {
        UserLoginDTO userLoginDTO = new UserLoginDTO();
        userLoginDTO.setLoginSuccess(true);
        userLoginDTO.setUserId(userId);
        return userLoginDTO;
    }
}