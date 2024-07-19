package org.timchen.live.common.interfaces;

import java.lang.reflect.Constructor;

import org.springframework.beans.BeanInstantiationException;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
/**
 * @Author: Tim Chen
 * @Date: 17:22 2024-06-12
 * @Description:
 */
public class ConvertBeanUtils {
    /**
     * 将一个对象转成目标对象
     *
     * @param source 源对象
     * @param targetClass 目标类的Class对象
     * @param <T> 目标对象的类型
     * @return 转换后的目标对象，如果源对象为null，则返回null
     */
    public static <T> T convert(Object source, Class<T> targetClass) {
        if (source == null) {
            return null;
        }
        T t = null;
        try {
            Constructor<T> constructor = targetClass.getConstructor();
            t = constructor.newInstance();
        } catch (Exception e) {
            // 处理异常，可以选择记录日志或者抛出运行时异常
            throw new RuntimeException("Instance creation failed", e);
        }
        BeanUtils.copyProperties(source, t);
        return t;
    }

    /**
     * 将List 对象转换成目标对象，注意实现是ArrayList
     *
     * @param targetClass
     * @param <K>
     * @param <T>
     * @return
     */
    public static <K, T> List<T> convertList(List<K> sourceList, Class<T> targetClass) {
        if (sourceList == null) {
            return null;
        }
        List targetList = new ArrayList((int)(sourceList.size()/0.75) + 1);
        for (K source : sourceList) {
            targetList.add(convert(source, targetClass));
        }
        return targetList;
    }

    private static <T> T newInstance(Class<T> targetClass) {
        try {
            return targetClass.newInstance();
        } catch (Exception e) {
            throw new BeanInstantiationException(targetClass,
                    "instantiation error", e);
        }
    }
}


