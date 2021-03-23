package com.test32.common.generic;

import com.test32.common.axisj.AjaxResultMessage;
import com.test32.common.paging.PagingData;

import java.util.Map;

public interface GenericAjaxService<T>
{
	AjaxResultMessage insert(T entity);
	AjaxResultMessage select(Integer index);
	AjaxResultMessage selectAll();
	PagingData selectBySearch(Map<String, Object> parameter);
	AjaxResultMessage update(T entity);
	AjaxResultMessage delete(Integer index);
}
