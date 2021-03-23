package com.test32.common.generic;

import java.util.List;
import java.util.Map;

public interface GenericDao<T>
{
    boolean insert(T entity);
    T selectByIndex(Integer index);
    List<T> selectBySearch(Map<String, Object> parameter);
    List<T> selectAll();
    Integer countBySearch(Map<String, Object> parameter);
    Integer countAll();
    boolean update(T entity);
    boolean deleteByIndex(Integer index);
}