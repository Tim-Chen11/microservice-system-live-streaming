package org.timchen.live.user.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * @Author: Tim Chen
 * @Date: 16:19 2024-07-22
 * @Description:
 */
@Data
public class UserTagDTO implements Serializable {


    @Serial
    private static final long serialVersionUID = -4254846555091175049L;

    private Long userId;

    private Long tagInfo01;

    private Long tagInfo02;

    private Long tagInfo03;

    private Date createTime;

    private Date updateTime;
}
