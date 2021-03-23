package com.test32.common.axisj;

public class AxisjGridPage
{
    /**
     * Page index
     */
    private int pageNo;
    
    /**
     * Row size per page
     */
    private int pageSize;
    
    /**
     * Total Page Count
     */
    private int pageCount;
    
    /**
     * Current Page Row Count
     */
    private int listCount;

    private Long totalRecordSize;
    
    public AxisjGridPage() {}
    
    public AxisjGridPage(int pageNo, int pageSize, int pageCount, int listCount)
    {
        super();
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.pageCount = pageCount;
        this.listCount = listCount;
    }

    public AxisjGridPage(int pageNo, int pageSize, int pageCount, int listCount, Long totalRecordSize)
    {
        super();
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.pageCount = pageCount;
        this.listCount = listCount;
        this.totalRecordSize = totalRecordSize;
    }

    public int getPageNo()
    {
        return pageNo;
    }

    public void setPageNo(int pageNo)
    {
        this.pageNo = pageNo;
    }

    public int getPageSize()
    {
        return pageSize;
    }

    public void setPageSize(int pageSize)
    {
        this.pageSize = pageSize;
    }

    public int getPageCount()
    {
        return pageCount;
    }

    public void setPageCount(int pageCount)
    {
        this.pageCount = pageCount;
    }

    public int getListCount()
    {
        return listCount;
    }

    public void setListCount(int listCount)
    {
        this.listCount = listCount;
    }

    public Long getTotalRecordSize()
    {
        return totalRecordSize;
    }

    public void setTotalRecordSize(Long totalRecordSize)
    {
        this.totalRecordSize = totalRecordSize;
    }

    @Override
    public String toString()
    {
        return "AxisjGridPage{" +
                "pageNo=" + pageNo +
                ", pageSize=" + pageSize +
                ", pageCount=" + pageCount +
                ", listCount=" + listCount +
                ", totalRecordSize=" + totalRecordSize +
                '}';
    }
}