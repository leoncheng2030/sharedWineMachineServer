package vip.wqs.common.util;

import lombok.extern.slf4j.Slf4j;

/**
 * 控制器时间戳生成工具类
 * 
 * 控制器要求：
 * - 时间戳不能重复使用
 * - 后续指令的时间戳必须大于上一次启动成功的数值
 * - 最小值：102401，最大值：2147483647
 * 
 * @author wqs
 * @date 2024/01/20
 */
@Slf4j
public class ControllerTimestampUtil {

    /**
     * 控制器时间戳范围限制
     */
    public static final long MIN_TIMESTAMP = 102401L;
    public static final long MAX_TIMESTAMP = 2147483647L;
    
    /**
     * 上一次生成的控制器时间戳，确保递增
     */
    private static volatile long lastControllerTimestamp = MIN_TIMESTAMP;
    
    /**
     * 生成控制器兼容的时间戳
     * 
     * @return 控制器时间戳
     */
    public static synchronized long generateControllerTimestamp() {
        // 获取当前时间戳
        long currentTimestamp = System.currentTimeMillis();
        
        // 生成控制器兼容的时间戳
        long controllerTimestamp;
        
        if (currentTimestamp > MAX_TIMESTAMP) {
            // 如果当前时间戳超过最大值，使用递增策略
            controllerTimestamp = Math.max(lastControllerTimestamp + 1, MIN_TIMESTAMP);
            
            // 如果递增后仍超过最大值，则重置到一个安全范围内的值
            if (controllerTimestamp > MAX_TIMESTAMP) {
                controllerTimestamp = MIN_TIMESTAMP + (currentTimestamp % 1000000);
            }
        } else {
            // 当前时间戳在有效范围内，确保大于上一次的值
            controllerTimestamp = Math.max(currentTimestamp, Math.max(lastControllerTimestamp + 1, MIN_TIMESTAMP));
            
            // 确保不超过最大值
            if (controllerTimestamp > MAX_TIMESTAMP) {
                controllerTimestamp = lastControllerTimestamp + 1;
                if (controllerTimestamp > MAX_TIMESTAMP) {
                    controllerTimestamp = MIN_TIMESTAMP;
                }
            }
        }
        
        // 更新上一次生成的时间戳
        lastControllerTimestamp = controllerTimestamp;
        
        log.debug("生成控制器时间戳：{}", controllerTimestamp);
        
        return controllerTimestamp;
    }
    
    /**
     * 生成带随机后缀的唯一时间戳
     * 用于订单号等需要唯一性的场景
     * 
     * @return 时间戳字符串
     */
    public static String generateUniqueTimestampString() {
        long timestamp = generateControllerTimestamp();
        int randomSuffix = (int)(Math.random() * 10000);
        return timestamp + String.format("%04d", randomSuffix);
    }
    
    /**
     * 验证时间戳是否在有效范围内
     * 
     * @param timestamp 时间戳
     * @return 是否有效
     */
    public static boolean isValidTimestamp(long timestamp) {
        return timestamp >= MIN_TIMESTAMP && timestamp <= MAX_TIMESTAMP;
    }
    
    /**
     * 获取当前的最后时间戳
     * 
     * @return 最后时间戳
     */
    public static long getLastTimestamp() {
        return lastControllerTimestamp;
    }
    
    /**
     * 从订单号中提取时间戳
     * 订单号格式：WO + 时间戳 + 随机后缀
     * 
     * @param orderNo 订单号
     * @return 时间戳，如果格式不正确返回null
     */
    public static Long extractTimestampFromOrderNo(String orderNo) {
        if (orderNo == null || !orderNo.startsWith("WO") || orderNo.length() < 8) {
            return null;
        }
        
        try {
            // 去掉前缀"WO"
            String timestampPart = orderNo.substring(2);
            
            // 如果包含随机后缀，去掉后4位
            if (timestampPart.length() > 10) {
                timestampPart = timestampPart.substring(0, timestampPart.length() - 4);
            }
            
            long timestamp = Long.parseLong(timestampPart);
            return isValidTimestamp(timestamp) ? timestamp : null;
        } catch (NumberFormatException e) {
            log.warn("从订单号中提取时间戳失败，订单号：{}", orderNo);
            return null;
        }
    }
} 