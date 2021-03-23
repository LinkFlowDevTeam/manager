package com.linkFlow.manager.common.model.config;

import com.linkFlow.manager.common.config.CustomServerConfigData;
import lombok.Data;

@Data
public class ConfigData
{
	private String configEncodeMode = null;

	private CustomServerConfigData defaultServerConfigData;

	public ConfigData()
	{
		defaultServerConfigData = new CustomServerConfigData();
	}
}