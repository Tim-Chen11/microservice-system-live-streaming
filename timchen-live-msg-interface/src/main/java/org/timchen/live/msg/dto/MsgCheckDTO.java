package org.timchen.live.msg.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author: Tim Chen
 * @Date: 2:06 2024-07-29
 * @Description:
 */
@Data
public class MsgCheckDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 3394248744287019717L;

    private boolean checkStatus;

    private String desc;

    public MsgCheckDTO(boolean checkStatus, String desc) {
        this.checkStatus = checkStatus;
        this.desc = desc;
    }

}
