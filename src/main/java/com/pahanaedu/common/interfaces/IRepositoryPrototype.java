package com.pahanaedu.common.interfaces;

import java.util.List;

public interface IRepositoryPrototype<T, R extends T> {

    R findById(Long id);
    List<R> findAll();
    R save(R obj);
    boolean delete(int id);

}
