package org.timchen.live.id.generate.provider.service.impl;

import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.timchen.live.id.generate.provider.dao.mapper.IdGenerateMapper;
import org.timchen.live.id.generate.provider.dao.po.IdGeneratePO;
import org.timchen.live.id.generate.provider.service.IdGenerateService;
import org.timchen.live.id.generate.provider.service.bo.LocalSeqIdBO;
import org.timchen.live.id.generate.provider.service.bo.LocalUnSeqIdBO;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @Author: Tim Chen
 * @Date: 13:43 2024-07-15
 * @Description:
 */
@Service
public class IdGenerateServiceImpl implements IdGenerateService, InitializingBean {

    @Resource
    private IdGenerateMapper idGenerateMapper;

    private static final Logger LOGGER = LoggerFactory.getLogger(IdGenerateServiceImpl.class);

    private static final float UPDATE_RATE = 0.75f;

    private static final int SEQ_ID = 1;

    private static Map<Integer, LocalSeqIdBO> localSeqIdBOMap = new ConcurrentHashMap<>();

    private static Map<Integer, LocalUnSeqIdBO> localUnSeqIdBOMap = new ConcurrentHashMap<>();

    private static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(8, 16, 3, TimeUnit.SECONDS, new ArrayBlockingQueue<>(1000),
            new ThreadFactory() {
                @Override
                public Thread newThread(Runnable r) {
                    Thread thread = new Thread(r);
                    thread.setName("id-generate-thread-" + ThreadLocalRandom.current().nextInt(1000));
                    return thread;
                }
            });

    private static Map<Integer, Semaphore> semaphoreMap = new ConcurrentHashMap<>();

    @Override
    public Long getSeqId(Integer id) {
        if (id == null) {
            LOGGER.error("[getSeqId] id is error, id is {}", id);
            return null;
        }

        LocalSeqIdBO localSeqIdBO = localSeqIdBOMap.get(id);
        if (localSeqIdBO == null) {
            LOGGER.error("[getSeqId] localSeqIdBO is null, id is {}", id);
            return null;
        }

        this.refreshLocalSeqId(localSeqIdBO);
        long returnId = localSeqIdBO.getCurrentNum().getAndIncrement();
        if (returnId > localSeqIdBO.getNextThreshold()) {
            LOGGER.error("[getSeqId] localSeqIdBO is over limit, id is {}", id);
            return null;
        }
        return returnId;
    }

    @Override
    public Long getUnSeqId(Integer id) {
        if (id == null) {
            LOGGER.error("[getUnSeqId] id is error, id is {}", id);
            return null;
        }

        LocalUnSeqIdBO localUnSeqIdBO = localUnSeqIdBOMap.get(id);
        if (localUnSeqIdBO == null) {
            LOGGER.error("[getUnSeqId] localUnSeqIdBO is null, id is {}", id);
            return null;
        }

        Long returnId = localUnSeqIdBO.getIdQueue().poll();

        if (returnId == null) {
            LOGGER.error("[getUnSeqId] returnId is null, id is {}", id);
            return null;
        }

        this.refreshLocalUnSeqId(localUnSeqIdBO);
        return returnId;
    }


    /**
     * Refresh local sequenced id phase in advance
     *
     * @param localSeqIdBO
     */
    private void refreshLocalSeqId(LocalSeqIdBO localSeqIdBO) {
        long step = localSeqIdBO.getNextThreshold() - localSeqIdBO.getCurrentStart();
        // when the rest space is less than 25%
        if (localSeqIdBO.getCurrentNum().get() - localSeqIdBO.getCurrentStart() > step * UPDATE_RATE) {
            Semaphore semaphore = semaphoreMap.get(localSeqIdBO.getId());
            if (semaphore == null) {
                LOGGER.error("semaphore is null, id is {}", localSeqIdBO.getId());
                return;
            }

            boolean acquireStatus = semaphore.tryAcquire();
            if (acquireStatus) {
                LOGGER.info("开始尝试进行本地id段的同步操作");
                //doing the sync id phase operation using async
                threadPoolExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            IdGeneratePO idGeneratePO = idGenerateMapper.selectById(localSeqIdBO.getId());
                            tryUpdateMySQLRecord(idGeneratePO);
                        } catch (Exception e) {
                            LOGGER.error("[refreshLocalSeqId] error is ", e);
                        } finally {
                            semaphoreMap.get(localSeqIdBO.getId()).release();
                            LOGGER.info("本地有序id段的同步完成，id is {}", localSeqIdBO.getId());
                        }



                    }
                });
            }
        }
    }


    /**
     * Refresh local unsequenced id phase in advance
     *
     * @param localUnSeqIdBO
     */
    private void refreshLocalUnSeqId(LocalUnSeqIdBO localUnSeqIdBO) {
        long step = localUnSeqIdBO.getNextThreshold() - localUnSeqIdBO.getCurrentStart();
        // when the rest space is less than 25%
        if (step * 0.25 > localUnSeqIdBO.getIdQueue().size()) {
            Semaphore semaphore = semaphoreMap.get(localUnSeqIdBO.getId());
            if (semaphore == null) {
                LOGGER.error("semaphore is null, id is {}", localUnSeqIdBO.getId());
                return;
            }

            boolean acquireStatus = semaphore.tryAcquire();
            if (acquireStatus) {
                threadPoolExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            IdGeneratePO idGeneratePO = idGenerateMapper.selectById(localUnSeqIdBO.getId());
                            tryUpdateMySQLRecord(idGeneratePO);
                        } catch (Exception e) {
                            LOGGER.error("[refreshLocalUnSeqId] error is ", e);
                        } finally {
                            semaphoreMap.get(localUnSeqIdBO.getId()).release();
                            LOGGER.info("本地无序id段的同步完成，id is {}", localUnSeqIdBO.getId());
                        }
                    }
                });

            }
        }
    }


    /**
     * Will call the method when initializing bean at the building stage of the project
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        List<IdGeneratePO> idGeneratePOList = idGenerateMapper.selectAll();
        for (IdGeneratePO idGeneratePO : idGeneratePOList) {
            LOGGER.info("服务器启动，抢占新的id段");
            tryUpdateMySQLRecord(idGeneratePO);
            semaphoreMap.put(idGeneratePO.getId(), new Semaphore(1));
        }
    }

    /**
     * Update mysql distributed system id config info, occupy corresponding id phase
     * sync implementation, plenty of network IO
     *
     * @param idGeneratePO
     */
    private void tryUpdateMySQLRecord(IdGeneratePO idGeneratePO) {
        int updateResult = idGenerateMapper.updateNewIdCountAndVersion(idGeneratePO.getId(), idGeneratePO.getVersion());
        if (updateResult > 0) {
            localIdBOHandler(idGeneratePO);
            return;
        }

        // Retry update
        for (int i = 0; i < 3; i++) {
            idGeneratePO = idGenerateMapper.selectById(idGeneratePO.getId());
            updateResult = idGenerateMapper.updateNewIdCountAndVersion(idGeneratePO.getId(), idGeneratePO.getVersion());
            if (updateResult > 0) {
                localIdBOHandler(idGeneratePO);
                return;
            }
        }

        throw new RuntimeException("表id段占用失败，竞争过于激烈，id is " + idGeneratePO.getId());
    }


    /**
     * Used for put into ID object into map and initialization
     *
     * @param idGeneratePO
     */
    private void localIdBOHandler(IdGeneratePO idGeneratePO) {

        long currentStart = idGeneratePO.getCurrentStart();
        long nextThreshold = idGeneratePO.getNextThreshold();
        long currentNum = currentStart;

        if (idGeneratePO.getIsSeq() == SEQ_ID) {
            LocalSeqIdBO localSeqIdBO = new LocalSeqIdBO();

            localSeqIdBO.setId((idGeneratePO.getId()));
            AtomicLong atomicLong = new AtomicLong(currentNum);
            localSeqIdBO.setCurrentNum(atomicLong);
            localSeqIdBO.setCurrentStart(currentStart);
            localSeqIdBO.setNextThreshold(nextThreshold);

            localSeqIdBOMap.put(localSeqIdBO.getId(), localSeqIdBO);
        } else {
            LocalUnSeqIdBO localUnSeqIdBO = new LocalUnSeqIdBO();
            localUnSeqIdBO.setCurrentStart(currentStart);
            localUnSeqIdBO.setNextThreshold(nextThreshold);
            localUnSeqIdBO.setId(idGeneratePO.getId());
            long begin = localUnSeqIdBO.getCurrentStart();
            long end = localUnSeqIdBO.getNextThreshold();
            List<Long> idList = new ArrayList<>();
            for (long i = begin; i <= end; i++) {
                idList.add(i);
            }
            //Shuffle the ids in if phase and put into the queue
            Collections.shuffle(idList);
            ConcurrentLinkedQueue<Long> idQueue = new ConcurrentLinkedQueue<>(idList);
            localUnSeqIdBO.setIdQueue(idQueue);

            localUnSeqIdBOMap.put(localUnSeqIdBO.getId(), localUnSeqIdBO);
        }
    }
}
