package com.rdpaas.task.serializer;

/**
 * jdk序列化抽象接口
 */
public interface ObjectSerializer<T> {

    byte[] serialize(T t);

    T deserialize(byte[] bytes);
}
