package org.timchen.live.user.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * @Author: Tim Chen
 * @Date: 10:44 2024-07-29
 * @Description:
 */
@Data
public class UserPhoneDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 4502843195713255060L;

    private Long id;
    private Long userId;
    private String phone;
    private Integer status;
    private Date createTime;
    private Date updateTime;
}
