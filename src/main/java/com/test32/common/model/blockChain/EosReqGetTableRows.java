package com.test32.common.model.blockChain;

public class EosReqGetTableRows
{
    private String code;
    private String scope;
    private String table;
    private String table_key;
    private String lower_bound;
    private String upper_bound;
    private Integer limit;
    private String key_type;
    private String index_position;

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public String getScope()
    {
        return scope;
    }

    public void setScope(String scope)
    {
        this.scope = scope;
    }

    public String getTable()
    {
        return table;
    }

    public void setTable(String table)
    {
        this.table = table;
    }

    public String getTable_key()
    {
        return table_key;
    }

    public void setTable_key(String table_key)
    {
        this.table_key = table_key;
    }

    public String getLower_bound()
    {
        return lower_bound;
    }

    public void setLower_bound(String lower_bound)
    {
        this.lower_bound = lower_bound;
    }

    public String getUpper_bound()
    {
        return upper_bound;
    }

    public void setUpper_bound(String upper_bound)
    {
        this.upper_bound = upper_bound;
    }

    public Integer getLimit()
    {
        return limit;
    }

    public void setLimit(Integer limit)
    {
        this.limit = limit;
    }

    public String getKey_type()
    {
        return key_type;
    }

    public void setKey_type(String key_type)
    {
        this.key_type = key_type;
    }

    public String getIndex_position()
    {
        return index_position;
    }

    public void setIndex_position(String index_position)
    {
        this.index_position = index_position;
    }
}