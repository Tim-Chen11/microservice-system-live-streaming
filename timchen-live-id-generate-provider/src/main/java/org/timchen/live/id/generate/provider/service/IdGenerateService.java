package org.timchen.live.id.generate.provider.service;

/**
 * @Author: Tim Chen
 * @Date: 13:42 2024-07-15
 * @Description:
 */

public interface IdGenerateService {
    /**
     * get sequenced id
     *
     * @param id
     * @return
     */
    Long getSeqId(Integer id);


    /**
     * get non-sequenced id
     *
     * @param id
     * @return
     */
    Long getUnSeqId(Integer id);
}
