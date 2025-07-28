package com.rdpaas.task.common;

/**
 * 节点状态枚举类
 */
public enum NodeStatus {

    //待执行
    DISABLE(0),
    //执行中
    ENABLE(1);

    final int id;

    NodeStatus(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static NodeStatus valueOf(int id) {
        if (id == 1)
            return ENABLE;

        return DISABLE;
    }

}
