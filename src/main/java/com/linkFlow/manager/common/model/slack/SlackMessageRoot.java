package com.linkFlow.manager.common.model.slack;

import lombok.Data;

import java.util.List;

@Data
public class SlackMessageRoot
{
    private List<SlackMessageAttachmentWithdrawRequest> attachments;
}