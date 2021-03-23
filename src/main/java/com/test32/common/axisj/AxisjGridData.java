package com.test32.common.axisj;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.linkFlow.manager.common.model.ReturnCode;
import com.linkFlow.manager.common.model.ReturnCodeSerialize;

import java.util.List;

public class AxisjGridData
{
    @JsonSerialize(using = ReturnCodeSerialize.class)
    private ReturnCode returnCode = ReturnCode.UNKNOWN_ERROR;
    private String description = ReturnCode.UNKNOWN_ERROR.getMessage();
    private String extraMessage;


    private AxisjGridPage page;
    
    /**
     * 상태 코드 (성공시 success, 실패 fail)
     */
    private String result;
    
    private Object list;

    public AxisjGridData(){}
    
    public AxisjGridData(AxisjGridPage page, String result, List<Object> list)
    {
        super();
        this.page = page;
        this.result = result;
        this.list = list;
    }

    public AxisjGridPage getPage()
    {
        return page;
    }

    public void setPage(AxisjGridPage page)
    {
        this.page = page;
    }

    public String getResult()
    {
        return result;
    }

    public void setResult(String result)
    {
        this.result = result;
    }

    public Object getList()
    {
        return list;
    }

    public void setList(Object list)
    {
        this.list = list;
    }

    public ReturnCode getReturnCode()
    {
        return returnCode;
    }

    public void setReturnCode(ReturnCode returnCode)
    {
        this.returnCode = returnCode;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getExtraMessage()
    {
        return extraMessage;
    }

    public void setExtraMessage(String extraMessage)
    {
        this.extraMessage = extraMessage;
    }

    @Override
    public String toString()
    {
        return "AxisjGridData [page=" + page + ", result=" + result + ", list=" + list + "]";
    }
    
}