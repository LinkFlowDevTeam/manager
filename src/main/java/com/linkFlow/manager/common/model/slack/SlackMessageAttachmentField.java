package com.linkFlow.manager.common.model.slack;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SlackMessageAttachmentField
{
    private String title;
    private String value;
    @JsonProperty("short")
    private Boolean isShort = false;
}