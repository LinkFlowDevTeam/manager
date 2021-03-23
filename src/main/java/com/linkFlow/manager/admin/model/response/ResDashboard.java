package com.linkFlow.manager.admin.model.response;

import com.linkFlow.manager.common.model.BaseResponse;
import lombok.Data;

import java.util.List;


@Data
public class ResDashboard extends BaseResponse
{
    private List<ResDashboardData> tokenAll;
    private List<ResDashboardData> tokenToday;
    private List<ResDashboardData> tokenYesterday;
}