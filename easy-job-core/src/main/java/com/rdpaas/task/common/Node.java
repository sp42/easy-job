package com.rdpaas.task.common;

import lombok.Data;

import java.util.Date;

/**
 * 执行节点对象
 */
@Data
public class Node {
    private Long id;

    /**
     * 节点ID，必须唯一
     */
    private Long nodeId;

    /**
     * 节点状态，0表示不可用，1表示可用
     */
    private NodeStatus status;

    /**
     * 节点序号
     */
    private Long rownum;

    /**
     * 执行任务次数
     */
    private Long counts;

    /**
     * 权重，默认都是1
     */
    private Integer weight = 1;

    /**
     * 通知指令
     */
    private NotifyCmd notifyCmd = NotifyCmd.NO_NOTIFY;

    /**
     * 通知值
     */
    private String notifyValue;

    /**
     * 节点创建时间
     */
    private Date createTime = new Date();

    /**
     * 更新时间
     */
    private Date updateTime = new Date();

    public Node(Long nodeId) {
        this.nodeId = nodeId;
    }

    public Node() {
    }
}
