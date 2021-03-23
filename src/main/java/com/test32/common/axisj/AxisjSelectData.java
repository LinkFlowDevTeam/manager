package com.test32.common.axisj;

import java.util.List;

public class AxisjSelectData
{
	private String result;
	
	private List<AxisjSelectOption> options;
	
	private String etcs;
	
	public AxisjSelectData()
	{
		result = "false";
		options = null;
		etcs = "";
	}

	public String getResult()
	{
		return result;
	}

	public void setResult(String result)
	{
		this.result = result;
	}

	public List<AxisjSelectOption> getOptions()
	{
		return options;
	}

	public void setOptions(List<AxisjSelectOption> options)
	{
		this.options = options;
	}

	public String getEtcs()
	{
		return etcs;
	}

	public void setEtcs(String etcs)
	{
		this.etcs = etcs;
	}

	@Override
	public String toString()
	{
		return "AxisjSelectData [result=" + result + ", options=" + options + ", etcs=" + etcs + "]";
	}
}