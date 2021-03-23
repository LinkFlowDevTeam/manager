package com.linkFlow.manager.admin.web;

import com.linkFlow.manager.admin.service.SetupService;
import com.linkFlow.manager.common.config.CommonConstants;
import com.linkFlow.manager.common.config.ConfigDataManager;
import com.linkFlow.manager.common.model.config.ConfigData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping("/setup/asdf")
@Controller
@PreAuthorize("hasAuthority('RL_SYSTEM_ADMIN')")
public class SetupController
{
    @Autowired private SetupService setupService;
    @Autowired private ConfigDataManager configDataManager;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(value = "/configure", method = RequestMethod.GET)
    public String showThirdPartyGameSetup(Model model)
    {
        ConfigData configData = configDataManager.getConfigData();
        model.addAttribute("configData", configData);
        return "/setup/configure";
    }

    @RequestMapping(value = "/configure", method = RequestMethod.POST)
    public String saveConfigure(@ModelAttribute ConfigData configData)
    {
        logger.debug(configData.toString());
        setupService.setupConfigureData(configData, configData.getConfigEncodeMode());
        // return "redirect:/setup/configure";
        return "redirect:" + CommonConstants.SETUP_PAGE_PATH;
    }
}