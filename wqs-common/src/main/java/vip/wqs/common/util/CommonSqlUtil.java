/*
 * Copyright [2022] [https://www.xiaonuo.vip]
 *
 * Snowy采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改Snowy源码头部的版权声明。
 * 3.本项目代码可免费商业使用，商业使用请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://www.xiaonuo.vip
 * 5.不可二次分发开源参与同类竞品，如有想法可联系团队xiaonuobase@qq.com商议合作。
 * 6.若您的项目无法满足以上几点，需要更多功能代码，获取Snowy商业授权许可，请在官网购买授权，地址为 https://www.xiaonuo.vip
 */
package vip.wqs.common.util;

import cn.hutool.core.util.ObjectUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 通用SQL工具类
 *
 * @author wqs
 * @date 2025/01/27
 **/
@Slf4j
@Component
public class CommonSqlUtil {

    @Resource
    private JdbcTemplate jdbcTemplate;

    /**
     * 执行SQL查询（无参数）
     *
     * @param sql SQL语句
     * @return 查询结果
     */
    public List<Map<String, Object>> execSql(String sql) {
        try {
            log.debug("执行SQL: {}", sql);
            return jdbcTemplate.queryForList(sql);
        } catch (Exception e) {
            log.error("执行SQL失败: {}, 错误: {}", sql, e.getMessage());
            throw new RuntimeException("SQL执行失败: " + e.getMessage(), e);
        }
    }

    /**
     * 执行SQL查询（带参数）
     *
     * @param sql SQL语句
     * @param params 参数
     * @return 查询结果
     */
    public List<Map<String, Object>> execSql(String sql, Object... params) {
        try {
            log.debug("执行SQL: {}, 参数: {}", sql, params);
            if (ObjectUtil.isEmpty(params)) {
                return jdbcTemplate.queryForList(sql);
            }
            return jdbcTemplate.queryForList(sql, params);
        } catch (Exception e) {
            log.error("执行SQL失败: {}, 参数: {}, 错误: {}", sql, params, e.getMessage());
            throw new RuntimeException("SQL执行失败: " + e.getMessage(), e);
        }
    }

    /**
     * 执行更新SQL（INSERT、UPDATE、DELETE）
     *
     * @param sql SQL语句
     * @param params 参数
     * @return 影响行数
     */
    public int execUpdate(String sql, Object... params) {
        try {
            log.debug("执行更新SQL: {}, 参数: {}", sql, params);
            if (ObjectUtil.isEmpty(params)) {
                return jdbcTemplate.update(sql);
            }
            return jdbcTemplate.update(sql, params);
        } catch (Exception e) {
            log.error("执行更新SQL失败: {}, 参数: {}, 错误: {}", sql, params, e.getMessage());
            throw new RuntimeException("SQL执行失败: " + e.getMessage(), e);
        }
    }

    /**
     * 执行批量更新SQL
     *
     * @param sql SQL语句
     * @param batchArgs 批量参数
     * @return 影响行数数组
     */
    public int[] execBatchUpdate(String sql, List<Object[]> batchArgs) {
        try {
            log.debug("执行批量更新SQL: {}, 批量参数数量: {}", sql, batchArgs.size());
            return jdbcTemplate.batchUpdate(sql, batchArgs);
        } catch (Exception e) {
            log.error("执行批量更新SQL失败: {}, 错误: {}", sql, e.getMessage());
            throw new RuntimeException("批量SQL执行失败: " + e.getMessage(), e);
        }
    }

    /**
     * 查询单个值
     *
     * @param sql SQL语句
     * @param requiredType 返回类型
     * @param params 参数
     * @return 查询结果
     */
    public <T> T queryForObject(String sql, Class<T> requiredType, Object... params) {
        try {
            log.debug("查询单个值SQL: {}, 参数: {}", sql, params);
            if (ObjectUtil.isEmpty(params)) {
                return jdbcTemplate.queryForObject(sql, requiredType);
            }
            return jdbcTemplate.queryForObject(sql, requiredType, params);
        } catch (Exception e) {
            log.error("查询单个值失败: {}, 参数: {}, 错误: {}", sql, params, e.getMessage());
            throw new RuntimeException("SQL查询失败: " + e.getMessage(), e);
        }
    }
}