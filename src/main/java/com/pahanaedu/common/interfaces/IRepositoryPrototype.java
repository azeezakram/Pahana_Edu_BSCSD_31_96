package com.pahanaedu.common.interfaces;

import java.util.List;

public interface IRepositoryPrototype<R> {

    R findById(Long id);
    List<R> findAll();
    R save(R obj);
    R update(R obj);
    boolean delete(Long id);

}
