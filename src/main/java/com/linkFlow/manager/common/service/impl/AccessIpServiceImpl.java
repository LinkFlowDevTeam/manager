package com.linkFlow.manager.common.service.impl;

import com.linkFlow.manager.common.config.CommonConstants;
import com.linkFlow.manager.common.dao.AccessIpDao;
import com.linkFlow.manager.common.model.vo.AccessIpVO;
import com.linkFlow.manager.common.service.AccessIpService;
import com.test32.common.paging.PagingData;
import com.test32.common.paging.PagingManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AccessIpServiceImpl implements AccessIpService
{
	@Autowired
    private AccessIpDao accessIpDao;


	@Override
	public AccessIpVO selectByIp(String ip)
	{
		return accessIpDao.selectByIp(ip);
	}

	@Override
	public boolean insertNotExist(String ip)
	{
		if( ! StringUtils.isEmpty(ip) && selectByIp(ip) == null)
		{
		    if(ip.length() <= AccessIpService.MAX_IP_LENGTH)
            {
                AccessIpVO accessIpVO = new AccessIpVO();
                accessIpVO.setAcIp(ip);
                return accessIpDao.insertNotExist(accessIpVO);
            }
		}
		return false;
	}

	@Override
	public boolean increaseErrorCount(String ip)
	{
		AccessIpVO accessIpVO = selectByIp(ip);
		if(accessIpVO != null)
		{
			Map<String, Object> map = new HashMap<>();
			map.put("acIdx", accessIpVO.getAcIdx());
            map.put("maxErrorCount", CommonConstants.MAX_LOGIN_ERROR_COUNT);
			return accessIpDao.increaseErrorCount(map);
		}
		return false;
	}

    @Override
    public boolean decreaseErrorCount(String ip)
    {
        AccessIpVO accessIpVO = selectByIp(ip);
        if(accessIpVO != null)
        {
            Map<String, Object> map = new HashMap<>();
            map.put("acIdx", accessIpVO.getAcIdx());
            return accessIpDao.decreaseErrorCount(map);
        }
        return false;
    }

    @Override
	public boolean resetErrorCount(String ip)
	{
		AccessIpVO accessIpVO = selectByIp(ip);
		if(accessIpVO != null)
		{
			Map<String, Object> map = new HashMap<>();
			map.put("acIdx", accessIpVO.getAcIdx());
			return accessIpDao.resetErrorCount(map);
		}
		return false;
	}

	@Override
	public boolean resetErrorCountByIdx(int idx)
	{
		Map<String, Object> map = new HashMap<>();
		map.put("acIdx", idx);
		return accessIpDao.resetErrorCount(map);
	}

	@Override
	public boolean resetErrorCountAll()
	{
		List<AccessIpVO> list = selectAll();
		for(AccessIpVO item : list)
		{
			Map<String, Object> map = new HashMap<>();
			map.put("acIdx", item.getAcIdx());
			accessIpDao.resetErrorCount(map);
		}
		return true;
	}

	@Override
    public boolean updateDate(String ip)
    {
        AccessIpVO accessIpVO = selectByIp(ip);
        if(accessIpVO != null)
        {
            AccessIpVO forUpdate = new AccessIpVO();
            return accessIpDao.update(forUpdate);
        }
        return false;
    }

    @Override
	public boolean insert(AccessIpVO entity)
	{
		return false;
	}

	@Override
	public AccessIpVO select(Long index)
	{
		return accessIpDao.selectByIndex(index);
	}

	@Override
	public List<AccessIpVO> selectAll()
	{
		return accessIpDao.selectAll();
	}

	@Override
	public List<AccessIpVO> selectBySearch(Map<String, Object> parameter)
	{
		return accessIpDao.selectBySearch(parameter);
	}

	@Override
	public boolean update(AccessIpVO entity)
	{
		return accessIpDao.update(entity);
	}

	@Override
	public boolean delete(Long index)
	{
		return accessIpDao.deleteByIndex(index);
	}

	@Override
	public PagingData selectPagingBySearch(Map<String, Object> map)
	{
		String pageNumberStr = (String) map.get("pageNo");
		String pageSizeStr = (String) map.get("pageSize");
		int pageNumber = StringUtils.isEmpty(pageNumberStr) ? 1 : Integer.valueOf(pageNumberStr);
		int pageSize = StringUtils.isEmpty(pageSizeStr) ? 20 : Integer.valueOf(pageSizeStr);
		Long totalRecode = accessIpDao.countBySearch(map);

		PagingData pagingData = PagingManager.getPagingList(pageNumber, totalRecode, pageSize, 10);
		map.put("startRow", pagingData.getStartRow());
		map.put("rowCount", pagingData.getPageSize());

		List<AccessIpVO> dataList = selectBySearch(map);
		pagingData.setObject(dataList);
		pagingData.setCurrentPageRowCount(dataList.size());

		return pagingData;
	}
}