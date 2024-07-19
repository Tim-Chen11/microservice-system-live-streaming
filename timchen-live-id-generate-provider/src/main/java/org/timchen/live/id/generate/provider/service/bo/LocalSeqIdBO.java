package org.timchen.live.id.generate.provider.service.bo;

import lombok.Data;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @Author: Tim Chen
 * @Date: 13:50 2024-07-15
 * @Description: Sequenced id BO
 */
@Data
public class LocalSeqIdBO {

    private int id;

    /**
     * The sequenced id now storing in the cache
     * user atomic class to ensure the thread safety
     */
    private AtomicLong currentNum;

    /**
     * The current id phase starting value
     */
    private Long currentStart;

    /**
     * The current id phase ending value
     */
    private Long nextThreshold;

}
