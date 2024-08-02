package org.timchen.live.msg.provider.dao.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @Author: Tim Chen
 * @Date: 2:17 2024-07-29
 * @Description:
 */
@Data
@TableName("t_sms")
public class SmsPO {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Integer code;
    private String phone;
    private Date sendTime;
    private Date updateTime;
}
