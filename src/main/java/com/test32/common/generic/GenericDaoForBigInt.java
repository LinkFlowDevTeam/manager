package com.test32.common.generic;

import java.util.List;
import java.util.Map;

public interface GenericDaoForBigInt<T>
{
    boolean insert(T entity);
    T selectByIndex(Long index);
    List<T> selectBySearch(Map<String, Object> parameter);
    List<T> selectAll();
    Long countBySearch(Map<String, Object> parameter);
    Long countAll();
    boolean update(T entity);
    boolean deleteByIndex(Long index);
}
