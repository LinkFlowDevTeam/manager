package com.linkFlow.manager.common.service.impl;

import com.linkFlow.manager.common.dao.NoticeDao;
import com.linkFlow.manager.common.model.vo.NoticeVO;
import com.linkFlow.manager.common.model.vo.NoticeVOForApi;
import com.linkFlow.manager.common.service.NoticeService;
import com.test32.common.paging.PagingData;
import com.test32.common.paging.PagingManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

@Service
public class NoticeServiceImpl implements NoticeService
{
    @Autowired private NoticeDao noticeDao;
    @Override public boolean insert(NoticeVO entity) { return noticeDao.insert(entity); }
    @Override public NoticeVO select(Long index) { return noticeDao.selectByIndex(index); }
    @Override public List<NoticeVO> selectAll() { return noticeDao.selectAll(); }
    @Override public List<NoticeVO> selectBySearch(Map<String, Object> parameter) { return noticeDao.selectBySearch(parameter); }
    @Override public boolean update(NoticeVO entity) { return noticeDao.update(entity); }
    @Override public boolean delete(Long index) { return noticeDao.deleteByIndex(index); }

    @Override
    public PagingData selectPagingBySearch(Map<String, Object> parameter)
    {
        String pageNumberStr = (String) parameter.get("pageNo");
        String pageSizeStr = (String) parameter.get("pageSize");
        int pageNumber = StringUtils.isEmpty(pageNumberStr) ? 1 : Integer.valueOf(pageNumberStr);
        int pageSize = StringUtils.isEmpty(pageSizeStr) ? 20 : Integer.valueOf(pageSizeStr);
        Long totalRecode = noticeDao.countBySearch(parameter);

        PagingData pagingData = PagingManager.getPagingList(pageNumber, totalRecode, pageSize, 10);
        parameter.put("startRow", pagingData.getStartRow());
        parameter.put("rowCount", pagingData.getPageSize());

        List<NoticeVO> dataList = selectBySearch(parameter);
        pagingData.setObject(dataList);
        pagingData.setCurrentPageRowCount(dataList.size());

        return pagingData;
    }

    @Override
    public PagingData selectPagingBySearchForApi(Map<String, Object> parameter)
    {
        String pageNumberStr = (String) parameter.get("pageNo");
        String pageSizeStr = (String) parameter.get("pageSize");
        int pageNumber = StringUtils.isEmpty(pageNumberStr) ? 1 : Integer.valueOf(pageNumberStr);
        int pageSize = StringUtils.isEmpty(pageSizeStr) ? 20 : Integer.valueOf(pageSizeStr);
        Long totalRecode = noticeDao.countBySearch(parameter);

        PagingData pagingData = PagingManager.getPagingList(pageNumber, totalRecode, pageSize, 10);
        parameter.put("startRow", pagingData.getStartRow());
        parameter.put("rowCount", pagingData.getPageSize());

        List<NoticeVOForApi> dataList = noticeDao.selectBySearchForApi(parameter);
        pagingData.setObject(dataList);
        pagingData.setCurrentPageRowCount(dataList.size());

        return pagingData;
    }
}