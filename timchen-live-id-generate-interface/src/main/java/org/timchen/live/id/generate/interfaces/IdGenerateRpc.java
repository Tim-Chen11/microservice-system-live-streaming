package org.timchen.live.id.generate.interfaces;

/**
 * @Author: Tim Chen
 * @Date: 13:38 2024-07-15
 * @Description:
 */
public interface IdGenerateRpc {

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
