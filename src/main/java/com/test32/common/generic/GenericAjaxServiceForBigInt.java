package com.test32.common.generic;


import com.test32.common.axisj.AjaxResultMessage;
import com.test32.common.paging.PagingData;

import java.util.Map;

public interface GenericAjaxServiceForBigInt<T>
{
	AjaxResultMessage insert(T entity);
	AjaxResultMessage select(Long index);
	AjaxResultMessage selectAll();
	PagingData selectBySearch(Map<String, Object> parameter);
	AjaxResultMessage update(T entity);
	AjaxResultMessage delete(Long index);
}