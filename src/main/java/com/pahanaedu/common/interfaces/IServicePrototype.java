package com.pahanaedu.common.interfaces;

import java.util.List;

public interface IServicePrototype<T> {

    T findById(Long id);
    List<T> findAll();
    T create(T obj);
    T update(T obj);
    boolean delete(int id);

}
