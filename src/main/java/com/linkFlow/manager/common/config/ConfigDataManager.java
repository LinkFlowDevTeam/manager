package com.linkFlow.manager.common.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.linkFlow.manager.common.model.config.ConfigData;
import com.test32.common.StaticLoggerDef;
import com.test32.common.config.ServerConfigAssistantUtil;
import lombok.Data;
import org.apache.commons.dbcp.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import static com.linkFlow.manager.common.config.CommonConstants.CONFIG_ENC_KEY;

@Data
public class ConfigDataManager
{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private ConfigData configData;

    private String hostName;
    private String contextName;
    private boolean isInit = false;

    public ConfigDataManager()
    {
        configData = new ConfigData();
        this.loadConfig();
        this.updateChange();
    }

    public ConfigDataManager(String hostName, String contextName)
    {
        configData = new ConfigData();
        this.hostName = hostName;
        this.contextName = contextName;
        this.loadConfig();
        this.updateChange();
    }

    public void changeDataSource(BasicDataSource dataSource)
    {
        CustomServerConfigData defaultServerConfigData = configData.getDefaultServerConfigData();
        dataSource.setDriverClassName(defaultServerConfigData.getJdbcDriverClassName());
        dataSource.setUrl(defaultServerConfigData.getJdbcUrl());
        dataSource.setUsername(defaultServerConfigData.getJdbcUsername());
        dataSource.setPassword(defaultServerConfigData.getJdbcPassword());
        isInit = false;
    }

    private String getConfigFilePath(boolean isIncludeTomcatDirectoryName)
    {
        String configPath = null;
        try {
            String entirePath = System.getProperty("catalina.home");
            int lastIndex = entirePath.lastIndexOf("\\");
            if (lastIndex < 0)
                lastIndex = entirePath.lastIndexOf("/");
            String directoryName = entirePath.substring(lastIndex + 1);

            if (isIncludeTomcatDirectoryName)
                configPath = CommonConstants.CONFIG_FILE_PATH.replace("TOMCAT_DIRECTORY_NAME_IS_HERE", directoryName);
            else
                configPath = CommonConstants.CONFIG_FILE_PATH.replace("/TOMCAT_DIRECTORY_NAME_IS_HERE", "");

            configPath = configPath.replace("HOST_NAME_IS_HERE", this.hostName);
            configPath = configPath.replace("/CONTEXT_NAME_IS_HERE", this.contextName);

            logger.warn(StaticLoggerDef.PREFIX_CONFIG + " get config from : " + configPath);
        }
        catch (Exception e) {
            logger.error(e.toString());
        }
        return configPath;
    }

    public boolean isConfigFileExist()
    {
        String loadedConfigData = ServerConfigAssistantUtil.loadServerConfigInfo(getConfigFilePath(true), CONFIG_ENC_KEY);
        return !StringUtils.isEmpty(loadedConfigData);
    }

    public String getConfigFileDataFromFile()
    {
        return ServerConfigAssistantUtil.loadServerConfigInfo(getConfigFilePath(true), CONFIG_ENC_KEY);
    }

    private ConfigData convertFromFile(String loadedConfigData)
    {
        ConfigData configData = null;
        try {
            if (!StringUtils.isEmpty(loadedConfigData)) {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                configData = objectMapper.readValue(loadedConfigData, ConfigData.class);
            }
        }
        catch (Exception e) {
            logger.error(e.toString());
        }
        return configData;
    }

    private boolean loadConfig()
    {
        boolean result = false;
        String loadedConfigData = ServerConfigAssistantUtil.loadServerConfigInfo(getConfigFilePath(true), CONFIG_ENC_KEY);

        try {
            if (loadedConfigData != null && !loadedConfigData.isEmpty()) {
                this.configData = convertFromFile(loadedConfigData);
            }
            else {
                saveConfig(null);
                isInit = true;
            }
            result = true;
        }
        catch (Exception e) {
            logger.error(e.toString());
        }
        return result;
    }

    public boolean saveConfig(String configEncodeMode)
    {
        boolean result = false;
        try {
            logger.warn(StaticLoggerDef.PREFIX_CONFIG + " saveConfig");
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
            String configDataStr = objectMapper.writeValueAsString(configData);
            result = ServerConfigAssistantUtil.saveServerConfigInfo(getConfigFilePath(true), configDataStr, configEncodeMode, CONFIG_ENC_KEY);
        }
        catch (Exception e) {
            logger.error(e.toString());
        }
        return result;
    }

    private boolean updateChange()
    {
        boolean result = false;
        try {
            CommonConstants.FILE_PATH_UPLOAD = configData.getDefaultServerConfigData().getUploadFilePath();
        }
        catch (Exception e) {
            logger.error(e.toString());
        }
        return result;
    }
}