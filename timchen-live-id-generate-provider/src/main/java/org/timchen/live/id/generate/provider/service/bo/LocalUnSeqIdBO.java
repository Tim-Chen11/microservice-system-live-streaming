package org.timchen.live.id.generate.provider.service.bo;

import lombok.Data;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @Author: Tim Chen
 * @Date: 15:38 2024-07-18
 * @Description: Unsequenced id BO
 */
@Data
public class LocalUnSeqIdBO {

    private int id;

    /**
     * Put the unsequenced id in this queue in advance
     */
    private ConcurrentLinkedQueue<Long> idQueue;

    /**
     * The current id phase starting value
     */
    private Long currentStart;

    /**
     * The current id phase ending value
     */
    private Long nextThreshold;
}
