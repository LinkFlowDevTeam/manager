package com.linkFlow.manager.common.service.impl;

import com.linkFlow.manager.common.dao.SysDataDao;
import com.linkFlow.manager.common.model.vo.SysDataVO;
import com.linkFlow.manager.common.model.vo.SysDataVOForApi;
import com.linkFlow.manager.common.service.SysDataService;
import com.test32.common.paging.PagingData;
import com.test32.common.paging.PagingManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

@Service
public class SysDataServiceImpl implements SysDataService
{
    @Autowired private SysDataDao sysDataDao;

    @Override public SysDataVOForApi selectByIndexForApi() { return sysDataDao.selectByIndexForApi(1); }
    @Override public boolean insert(SysDataVO entity) { return sysDataDao.insert(entity); }
    @Override public SysDataVO select(Integer index) { return sysDataDao.selectByIndex(index); }
    @Override public List<SysDataVO> selectAll() { return sysDataDao.selectAll(); }
    @Override public List<SysDataVO> selectBySearch(Map<String, Object> parameter) { return sysDataDao.selectBySearch(parameter); }
    @Override public boolean update(SysDataVO entity) { entity.setSsIdx(1); return sysDataDao.update(entity); }
    @Override public boolean delete(Integer index) { return sysDataDao.deleteByIndex(index); }

    @Override
    public SysDataVO selectDefault()
    {
        return sysDataDao.selectByIndex(1);
    }

    @Override
    public boolean generateDefault()
    {
        SysDataVO sysDataVO = select(1);
        if(sysDataVO == null)
        {
            sysDataVO = new SysDataVO();
            sysDataVO.setSsIdx(1);
            return insert(sysDataVO);
        }
        return true;
    }

    @Override
    public PagingData selectPagingBySearch(Map<String, Object> parameter)
    {
        String pageNumberStr = (String) parameter.get("pageNo");
        String pageSizeStr = (String) parameter.get("pageSize");
        int pageNumber = StringUtils.isEmpty(pageNumberStr) ? 1 : Integer.valueOf(pageNumberStr);
        int pageSize = StringUtils.isEmpty(pageSizeStr) ? 20 : Integer.valueOf(pageSizeStr);
        Integer totalRecode = sysDataDao.countBySearch(parameter);

        PagingData pagingData = PagingManager.getPagingList(pageNumber, Long.valueOf(totalRecode), pageSize, 10);
        parameter.put("startRow", pagingData.getStartRow());
        parameter.put("rowCount", pagingData.getPageSize());

        List<SysDataVO> dataList = selectBySearch(parameter);
        pagingData.setObject(dataList);
        pagingData.setCurrentPageRowCount(dataList.size());

        return pagingData;
    }
}