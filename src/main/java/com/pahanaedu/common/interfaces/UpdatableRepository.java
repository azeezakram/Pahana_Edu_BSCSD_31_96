package com.pahanaedu.common.interfaces;

public interface UpdatableRepository<T> {
    T update(T obj);
}
