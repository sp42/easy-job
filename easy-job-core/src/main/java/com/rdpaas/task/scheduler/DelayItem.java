package com.rdpaas.task.scheduler;

import lombok.Data;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * 延时队列中的元素
 */
@Data
public class DelayItem<T> implements Delayed {
    private final long delay;

    private final long expire;

    private final T t;

    private final long now;

    public DelayItem(long delay, T t) {
        this.delay = delay;
        this.t = t;
        //到期时间 = 当前时间+延迟时间
        expire = System.currentTimeMillis() + delay;
        now = System.currentTimeMillis();
    }


    /**
     * 需要实现的接口，获得延迟时间   用过期时间-当前时间
     *
     * @param unit
     * @return
     */
    public long getDelay(TimeUnit unit) {
        return unit.convert(this.expire - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    }

    /**
     * 用于延迟队列内部比较排序   当前时间的延迟时间 - 比较对象的延迟时间
     *
     * @param o
     * @return
     */
    public int compareTo(Delayed o) {
        return (int) (this.getDelay(TimeUnit.MILLISECONDS) - o.getDelay(TimeUnit.MILLISECONDS));
    }

    public T getItem() {
        return t;
    }
}
