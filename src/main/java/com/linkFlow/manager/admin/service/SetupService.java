package com.linkFlow.manager.admin.service;

import com.linkFlow.manager.common.config.ConfigDataManager;
import com.linkFlow.manager.common.model.config.ConfigData;
import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("setupService")
public class SetupService
{
    @Autowired
    private ConfigDataManager configDataManager;

    @Autowired
    private BasicDataSource dataSource;

    public boolean setupConfigureData(ConfigData configData, String configEncodeMode)
    {
        configDataManager.setConfigData(configData);
        configDataManager.changeDataSource(this.dataSource);
        configDataManager.setConfigData(configData);

        configDataManager.saveConfig(configEncodeMode);
        return true;
    }
}