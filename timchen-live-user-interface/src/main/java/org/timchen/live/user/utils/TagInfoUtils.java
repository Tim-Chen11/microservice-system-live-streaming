package org.timchen.live.user.utils;

/**
 * @Author: Tim Chen
 * @Date: 14:51 2024-07-19
 * @Description:
 */
public class TagInfoUtils {

    /**
     * check if contain any of the tag
     *
     * @param tagInfo
     * @param matchTag
     * @return
     */
    public static boolean isContain(Long tagInfo, Long matchTag) {
        return tagInfo != null && matchTag != null && matchTag > 0 && (tagInfo & matchTag) == matchTag;
    }

}
