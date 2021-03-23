package com.test32.common.util;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CommonMathUtil
{
    public static BigDecimal addBigDecimal(BigDecimal lValue, BigDecimal rValue)
    {
        if(rValue == null){
            return lValue;
        }

        if(lValue == null) {
            BigDecimal result = BigDecimal.ZERO;
            return result.add(rValue);
        }

        return lValue.add(rValue);
    }

    public static Long addLong(Long lValue, Long rValue)
    {
        if(rValue == null){
            return lValue;
        }

        if(lValue == null) {
            Long result = 0L;
            return result + rValue;
        }

        return lValue + rValue;
    }

    public static Integer addInteger(Integer lValue, Integer rValue)
    {
        if(rValue == null){
            return lValue;
        }

        if(lValue == null) {
            Integer result = 0;
            return result + rValue;
        }

        return lValue + rValue;
    }

    public static BigDecimal subBigDecimal(BigDecimal lValue, BigDecimal rValue)
    {
        if(rValue == null){
            return lValue;
        }

        if(lValue == null) {
            BigDecimal result = BigDecimal.ZERO;
            return result.subtract(rValue);
        }

        return lValue.subtract(rValue);
    }

    public static Long subLong(Long lValue, Long rValue)
    {
        if(rValue == null){
            return lValue;
        }

        if(lValue == null) {
            Long result = 0L;
            return result - rValue;
        }

        return lValue - rValue;
    }

    public static Integer subInteger(Integer lValue, Integer rValue)
    {
        if(rValue == null){
            return lValue;
        }

        if(lValue == null) {
            Integer result = 0;
            return result - rValue;
        }

        return lValue - rValue;
    }

    public static Integer getOrZero(Integer value)
    {
        if(value == null){
            return 0;
        }

        return value;
    }


    public static BigDecimal getOrZero(BigDecimal value)
    {
        if(value == null){
            return BigDecimal.ZERO;
        }

        return value;
    }

    public static Long getOrZero(Long value)
    {
        if(value == null){
            return 0L;
        }

        return value;
    }
}