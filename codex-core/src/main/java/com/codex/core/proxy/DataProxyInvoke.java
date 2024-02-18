package com.codex.core.proxy;

import cn.hutool.extra.spring.SpringUtil;
import com.codex.core.scan.CodexModel;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * @author YuePeng
 * date 2021/3/16 13:37
 */
public class DataProxyInvoke {

    public static <T> void invoke(CodexModel codexModel, Consumer<DataProxy<T>> consumer) {
        //父类及接口 @PreDataProxy
        findClassExtendStack(codexModel.getClazz()).forEach(clazz -> DataProxyInvoke.actionInvokePreDataProxy(clazz, consumer));
        //本类及接口 @PreDataProxy
        DataProxyInvoke.actionInvokePreDataProxy(codexModel.getClazz(), consumer);
        //@Codex → DataProxy
        Stream.of(codexModel.getCodex().dataProxy()).forEach(proxy -> consumer.accept(getInstanceBean(proxy)));
    }

    private static <T> void actionInvokePreDataProxy(Class<?> clazz, Consumer<DataProxy<T>> consumer) {
        //接口
        Stream.of(clazz.getInterfaces()).forEach(it -> Optional.ofNullable(it.getAnnotation(PreDataProxy.class))
                .ifPresent(dataProxy -> consumer.accept(getInstanceBean(dataProxy.value()))));
        //类
        Optional.ofNullable(clazz.getAnnotation(PreDataProxy.class))
                .ifPresent(dataProxy -> consumer.accept(getInstanceBean(dataProxy.value())));
    }

    private static <T> DataProxy<T> getInstanceBean(Class<? extends DataProxy<?>> dataProxy) {
        return (DataProxy) getBean(dataProxy);
    }

    public static List<Class<?>> findClassExtendStack(Class<?> clazz) {
        List<Class<?>> list = new ArrayList<>();
        Class<?> tempClass = clazz;
        while (null != tempClass) {
            tempClass = tempClass.getSuperclass();
            if (tempClass != null && tempClass != Object.class) {
                list.add(tempClass);
            }
        }
        if (list.size() > 1) {
            Collections.reverse(list);
        }
        return list;
    }

    @SneakyThrows
    public static <T> T getBean(Class<T> clazz) {
        if (null != clazz.getDeclaredAnnotation(Component.class)
                || null != clazz.getDeclaredAnnotation(Service.class)
                || null != clazz.getDeclaredAnnotation(Repository.class)
                || null != clazz.getDeclaredAnnotation(RestController.class)
                || null != clazz.getDeclaredAnnotation(Controller.class)) {
            return SpringUtil.getBean(clazz);
        } else {
            return clazz.newInstance();
        }
    }

}
