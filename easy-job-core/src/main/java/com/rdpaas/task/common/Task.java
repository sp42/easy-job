package com.rdpaas.task.common;

import lombok.Data;

import java.util.Date;

/**
 * 任务实体
 */
@Data
public class Task {
    private Long id;

    /**
     * 父任务id
     */
    private Long pid;

    /**
     * 调度名称
     */
    private String name;

    /**
     * cron表达式
     */
    private String cronExpr;

    /**
     * 当前执行的节点id
     */
    private Long nodeId;

    /**
     * 状态，0表示未开始，1表示待执行，2表示执行中，3表示已完成
     */
    private TaskStatus status = TaskStatus.NOT_STARTED;

    /**
     * 成功次数
     */
    private Integer successCount;

    /**
     * 失败次数
     */
    private Integer failCount;

    /**
     * 执行信息
     */
    private byte[] invokeInfo;

    /**
     * 乐观锁标识
     */
    private Integer version;

    /**
     * 首次开始时间
     */
    private Date firstStartTime;

    /**
     * 下次开始时间
     */
    private Date nextStartTime;

    /**
     * 创建时间
     */
    private Date createTime = new Date();

    /**
     * 更新时间
     */
    private Date updateTime = new Date();

    /**
     * 任务的执行者
     */
    private Invocation invokor;

    public Task() {
    }

    public Task(String name, String cronExpr, Invocation invokor) {
        this.name = name;
        this.cronExpr = cronExpr;
        this.invokor = invokor;
    }
}
