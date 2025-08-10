package com.pahanaedu.common.interfaces;

import java.util.List;

public interface Repository<T> {

    T findById(Long id);
    List<T> findAll();
    T save(T obj);
    boolean delete(Long id);

}
