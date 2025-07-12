package com.pahanaedu.common.interfaces;

import java.util.List;

public interface IServicePrototype<T, R> {

    R findById(Long id);
    List<R> findAll();
    R create(T obj);
    R update(T obj);
    boolean delete(Long id);

}
