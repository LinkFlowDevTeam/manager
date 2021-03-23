package com.linkFlow.manager.common.service;

public interface CommonQueryService
{
    Boolean isTableExist(String schemeName, String tableName);
    Boolean createTable(String queryString);
    String selectNowString();

    Boolean truncateAccessIpTable();
}