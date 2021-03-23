package com.linkFlow.manager.common.service.impl;

import com.linkFlow.manager.common.dao.CommonQueryDao;
import com.linkFlow.manager.common.service.CommonQueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CommonQueryServiceImpl implements CommonQueryService
{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CommonQueryDao commonQueryDao;

    @Override
    public Boolean isTableExist(String schemeName, String tableName)
    {
        Map<String, Object> searchMap = new HashMap<>();
        searchMap.put("schemeName", "'" +  schemeName + "'");
        searchMap.put("tableName", "'" + tableName + "'");
        Integer count = commonQueryDao.countTable(searchMap);
        return count > 0;
    }

    @Override
    public Boolean createTable(String queryString)
    {
        Map<String, Object> searchMap = new HashMap<>();
        searchMap.put("queryString", queryString);
        return commonQueryDao.directQuery(searchMap);
    }

    @Override
    public String selectNowString()
    {
        return commonQueryDao.selectNowString();
    }

    @Override
    public Boolean truncateAccessIpTable()
    {
        String queryString = "TRUNCATE `z_access_ip`";
        Map<String, Object> searchMap = new HashMap<>();
        searchMap.put("queryString", queryString);
        return commonQueryDao.directQuery(searchMap);
    }
}