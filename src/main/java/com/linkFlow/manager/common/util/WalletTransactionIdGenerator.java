package com.linkFlow.manager.common.util;


import java.util.Date;

public class WalletTransactionIdGenerator
{
	private volatile static WalletTransactionIdGenerator instance;
	private WalletTransactionIdGenerator()	{}
	public static WalletTransactionIdGenerator getInstance()
	{
		if(instance == null)
		{
			synchronized(WalletTransactionIdGenerator.class)
			{
				if(instance == null)
				{
					instance = new WalletTransactionIdGenerator();
				}
			}
		}
		return instance;
	}

	private long _prior_milli;
	private int _num;
	final String prefix4 = "_";

	private Long getNextData()
	{
		long current = new Date().getTime();
		if(current != _prior_milli)
		{
			_prior_milli = current;
			_num = 1;
		}
		else
		{
			_num++;
			if(_num > 999)
				return null;
		}
		return current;
	}

	public String generateUniqueTransactionIdByServerTag(String serverTag)
	{
		Long nextData = getNextData();
		if(nextData == null) return null;
		// serverTag4 + prefix4 + 16 length : 24
		return serverTag + prefix4 + nextData + String.format("%03d", _num);
	}

	public String generateUniqueTransactionIdBy(String serverTag, String externalPrefix)
	{
		Long nextData = getNextData();
		if(nextData == null) return null;
		return serverTag + externalPrefix + nextData+ String.format("%03d", _num);
	}
}