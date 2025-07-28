package com.rdpaas.task.common;

import lombok.Data;

import java.util.Date;

/**
 * 任务实体详情类
 */
@Data
public class TaskDetail {

    private Long id;

    /**
     * 任务id
     */
    private Long taskId;

    /**
     * 所属父明细ID
     */
    private Long pid;

    /**
     * 当前执行的节点id
     */
    private Long nodeId;

    /**
     * 重试次数
     */
    private Integer retryCount;

    /**
     * 状态，0表示待执行，1表示执行中，2表示异常中，3表示已完成
     * 添加了任务明细说明就开始执行了
     */
    private TaskStatus status = TaskStatus.DOING;

    /**
     * 开始时间
     */
    private Date startTime = new Date();

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 乐观锁标识
     */
    private Integer version;

    /**
     * 错误信息
     */
    private String errorMsg;

    public TaskDetail() {
    }

    public TaskDetail(Long taskId) {
        this.taskId = taskId;
    }
}
