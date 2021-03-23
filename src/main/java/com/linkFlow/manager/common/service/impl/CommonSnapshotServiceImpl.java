package com.linkFlow.manager.common.service.impl;

import com.linkFlow.manager.common.dao.CommonSnapshotDao;
import com.linkFlow.manager.common.model.vo.CommonSnapshotVO;
import com.linkFlow.manager.common.model.vo.MemberVO;
import com.linkFlow.manager.common.service.CommonSnapshotService;
import com.test32.common.paging.PagingData;
import com.test32.common.paging.PagingManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("commonSnapshotService")
public class CommonSnapshotServiceImpl implements CommonSnapshotService
{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired private CommonSnapshotDao commonSnapshotDao;

    @Override public boolean insert(CommonSnapshotVO entity) { return commonSnapshotDao.insert(entity); }
    @Override public CommonSnapshotVO select(Long index) { return commonSnapshotDao.selectByIndex(index); }
    @Override public List<CommonSnapshotVO> selectAll() { return commonSnapshotDao.selectAll(); }
    @Override public List<CommonSnapshotVO> selectBySearch(Map<String, Object> parameter) { return commonSnapshotDao.selectBySearch(parameter); }
    @Override public boolean update(CommonSnapshotVO entity) { return commonSnapshotDao.update(entity); }
    @Override public boolean delete(Long index) { return commonSnapshotDao.deleteByIndex(index); }
    @Override public boolean insertOrUpdate(CommonSnapshotVO entity)
    {
        if(commonSnapshotDao.updateByKey(entity))
            return true;
        else
            return commonSnapshotDao.insertOrUpdate(entity);
    }

    @Override
    public PagingData selectPagingBySearch(Map<String, Object> parameter)
    {
        String pageNumberStr = (String) parameter.get("pageNo");
        String pageSizeStr = (String) parameter.get("pageSize");
        int pageNumber = StringUtils.isEmpty(pageNumberStr) ? 1 : Integer.valueOf(pageNumberStr);
        int pageSize = StringUtils.isEmpty(pageSizeStr) ? 20 : Integer.valueOf(pageSizeStr);
        Long totalRecode = commonSnapshotDao.countBySearch(parameter);

        PagingData pagingData = PagingManager.getPagingList(pageNumber, totalRecode, pageSize, 10);
        parameter.put("startRow", pagingData.getStartRow());
        parameter.put("rowCount", pagingData.getPageSize());

        List<CommonSnapshotVO> dataList = selectBySearch(parameter);
        pagingData.setObject(dataList);
        pagingData.setCurrentPageRowCount(dataList.size());

        return pagingData;
    }

    @Override
    public boolean isDataExist(int type, String searchStartDate)
    {
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("csType", type);
        parameter.put("searchStartDate", searchStartDate.substring(0, 10) + " 00:00:00");
        return (commonSnapshotDao.countBySearch(parameter) > 0);
    }

    @Override
    public boolean isMemberDataExist(String searchStartDate)
    {
        return isDataExist(CommonSnapshotService.TYPE_MEMBER, searchStartDate);
    }

    @Override
    public CommonSnapshotVO generateMember(MemberVO memberVO, BigDecimal baseAmount, Integer childCount, BigDecimal childBaseAmount)
    {
        CommonSnapshotVO entity = new CommonSnapshotVO();
        // unique: type, targetIdx
        entity.setCsType(CommonSnapshotService.TYPE_MEMBER);
        entity.setCsTargetIdx(memberVO.getMbIdx());

        entity.setCsData1(baseAmount.toPlainString());
        entity.setCsData2("" + childCount);
        entity.setCsData3(childBaseAmount.toPlainString());

        return entity;
    }
}