package com.linkFlow.manager.api.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ReqAssetHistory
{
    @Schema(example = "검색 시작일, yyyy-MM-dd, 옵셔널")
    private String searchStart;

    @Schema(example = "검색 종료일, yyyy-MM-dd, 옵셔널")
    private String searchEnd;
}