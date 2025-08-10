package com.pahanaedu.common.interfaces;

public interface UpdatableService<T, R> {
    R update(T obj);
}
