package org.timchen.live.user.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author: Tim Chen
 * @Date: 17:14 2024-07-22
 * @Description:
 */
@Data
public class UserCacheAsyncDeleteDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -8324963765684379119L;
    /**
     * different scene code, distinguish delay message
     */
    private int code;
    
    private String json;
}
