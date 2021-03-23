package com.test32.common.paging;

public class PagingManager
{
    public static PagingData getPagingList(int currentPage, Long totalRecordSize, int pageSize, int pageGroupSize)
    {
        int startPage = 0;
        int endPage = 0;
        
        PagingData paging = new PagingData(totalRecordSize, currentPage, pageSize, pageGroupSize);

        int totalPageGroupSize = (int)Math.ceil((float)totalRecordSize / (float)(pageSize * pageGroupSize));

        int totalPageSize = (int)Math.ceil( (float)totalRecordSize / (float) pageSize );
        
        int currentPageGroup = (int)Math.ceil((float) currentPage / (float) pageGroupSize);

        if (currentPageGroup == totalPageGroupSize)
        {
            startPage = (currentPageGroup * pageGroupSize) -  (pageGroupSize - 1);
            endPage = totalPageSize;
        }
        else if(currentPageGroup < totalPageGroupSize)
        {
            startPage = (currentPageGroup * pageGroupSize) - (pageGroupSize - 1);
            endPage = currentPageGroup * pageGroupSize;
        }
        
        int startRow = (currentPage * pageSize) - (pageSize - 1) - 1;
        int endRow = currentPage * pageSize - 1;
        
        paging.setTotalPageGroupSize((int)Math.ceil((float)totalRecordSize / (float)(pageSize * pageGroupSize)));
        paging.setTotalPageSize(totalPageSize);
        paging.setCurrentPageGroup(currentPageGroup);
        paging.setStartPage(startPage);
        paging.setEndPage(endPage);
        paging.setStartRow(startRow);
        paging.setEndRow(endRow);
        
        return paging;
    }
}