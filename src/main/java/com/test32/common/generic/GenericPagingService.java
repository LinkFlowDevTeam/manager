package com.test32.common.generic;

import com.test32.common.paging.PagingData;

import java.util.List;
import java.util.Map;

public interface GenericPagingService<T>
{
	boolean insert(T entity);
	T select(Integer index);
	List<T> selectAll();
	PagingData selectBySearch(Map<String, Object> parameter);
	boolean update(T entity);
	boolean delete(Integer index);
}