package com.test32.common.axisj;


import com.linkFlow.manager.common.model.ReturnCode;
import com.test32.common.paging.PagingData;

public class AxisjGridDataConverter
{
	public static AxisjGridData Convert(PagingData convertingData)
	{
		AxisjGridData gridData = null;

		if (convertingData != null)
		{
			AxisjGridPage page = new AxisjGridPage();
			page.setPageNo(convertingData.getCurrentPage());
			page.setPageSize(convertingData.getPageSize());
			page.setPageCount(convertingData.getTotalPageSize());
			page.setListCount(convertingData.getCurrentPageRowCount());
			page.setTotalRecordSize(convertingData.getTotalRecordSize());
			gridData = new AxisjGridData();
			gridData.setPage(page);
			gridData.setResult("ok");
			gridData.setList(convertingData.getObject());

			gridData.setReturnCode(ReturnCode.SUCCESS);
			gridData.setDescription(gridData.getReturnCode().getMessage());
		}


		return gridData;
	}

	public static AxisjGridData Convert(PagingData convertingData, String message)
	{
		AxisjGridData gridData = null;

		if (convertingData != null)
		{
			AxisjGridPage page = new AxisjGridPage();
			page.setPageNo(convertingData.getCurrentPage());
			page.setPageSize(convertingData.getPageSize());
			page.setPageCount(convertingData.getTotalPageSize());
			page.setListCount(convertingData.getCurrentPageRowCount());
			page.setTotalRecordSize(convertingData.getTotalRecordSize());
			gridData = new AxisjGridData();
			gridData.setPage(page);
			gridData.setResult(message);
			gridData.setList(convertingData.getObject());

			gridData.setReturnCode(ReturnCode.SUCCESS);
			gridData.setDescription(gridData.getReturnCode().getMessage());
		}

		return gridData;
	}
}