package org.timchen.live.user.provider.service;

import org.timchen.live.user.constants.UserTagsEnum;

/**
 * @Author: Tim Chen
 * @Date: 11:22 2024-07-19
 * @Description:
 */
public interface IUserTagService {

    /**
     * Set tag
     *
     * @param userId
     * @param userTagsEnum
     * @return
     */
    boolean setTag(Long userId, UserTagsEnum userTagsEnum);

    /**
     * Cancel tag
     *
     * @param userId
     * @param userTagsEnum
     * @return
     */
    boolean cancelTag(Long userId, UserTagsEnum userTagsEnum);

    /**
     * Check if contain any of the tags
     *
     * @param userId
     * @param userTagsEnum
     * @return
     */
    boolean containTag(Long userId, UserTagsEnum userTagsEnum);
}

