package com.test32.common.axisj;

public class AxisjSelectOption
{
	private String optionValue;

	private String optionText;

	public String getOptionValue()
	{
		return optionValue;
	}

	public void setOptionValue(String optionValue)
	{
		this.optionValue = optionValue;
	}

	public String getOptionText()
	{
		return optionText;
	}

	public void setOptionText(String optionText)
	{
		this.optionText = optionText;
	}

	@Override
	public String toString()
	{
		return "AxisjSelectOption [optionValue=" + optionValue + ", optionText=" + optionText + "]";
	}
}