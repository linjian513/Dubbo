package com.study.DubboWithoutSpringConsumer.filter;


import com.alibaba.dubbo.rpc.RpcContext;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;


import java.util.UUID;
 
/**
 * traceId工具类
 * Created by gameloft9 on 2018/9/7.
 */
public class TraceUtil {
 
    public final static String TRACE_ID = "trace_id";
 
    /**
     * 初始化traceId,由consumer调用
     */
    public static void initTrace() {
        String traceId = generateTraceId();
        setTraceId(traceId);
    }
 
    /**
     * 从Dubbo中获取traceId，provider调用
     * @param context
     */
    public static void getTraceFrom(RpcContext context) {
        String traceId = (String) context.getAttachment(TRACE_ID);
        if (traceId == null) {
            traceId = generateTraceId();
        }
 
        setTraceId(traceId);
    }
 
    /**
     * 把traceId放入dubbo远程调用中，consumer调用
     * @param context
     */
    public static void putTraceInto(RpcContext context) {
        String traceId = getTraceId();
        if (traceId != null) {
            context.setAttachment(TRACE_ID, traceId);
        }
    }
 
    /**
     * 从MDC中清除traceId
     */
    public static void clearTrace() {
        MDC.remove(TRACE_ID);
    }
 
    /****************************私有方法区*********************************/
    
    /**
     * 从MDC中获取traceId
     * */
    private static String getTraceId() {
        return MDC.get(TRACE_ID);
    }
 
    /**
     * 将traceId放入MDC
     * @param traceId
     */
    private static void setTraceId(String traceId) {
        if (!StringUtils.isBlank(traceId)) {
            traceId = StringUtils.left(traceId, 36);
        }
        MDC.put(TRACE_ID, traceId);
    }
 
    /**
     * 生成traceId
     * @return
     */
    static private String generateTraceId() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
 
}

