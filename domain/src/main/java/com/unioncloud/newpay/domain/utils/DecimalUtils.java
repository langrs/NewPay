package com.unioncloud.newpay.domain.utils;

import java.math.BigDecimal;

public class DecimalUtils {

	public static BigDecimal getCeiling(int digit, String value){
		if(value == null || "".equals(value)){
			return BigDecimal.ZERO;
		}
		BigDecimal   bDecimal  =   new BigDecimal(value);  
		return bDecimal.setScale(digit, BigDecimal.ROUND_CEILING);  
	}
	
	public static BigDecimal getFloor(int digit, String value){
		if(value == null || "".equals(value)){
			return BigDecimal.ZERO;
		}
		BigDecimal   bDecimal  =   new BigDecimal(value);  
		return bDecimal.setScale(digit, BigDecimal.ROUND_FLOOR);  
	}
	
	public static BigDecimal getHalfUp(int digit, String value){
		if(value == null || "".equals(value)){
			return BigDecimal.ZERO;
		}
		BigDecimal   bDecimal  =   new BigDecimal(value);  
		return bDecimal.setScale(digit, BigDecimal.ROUND_HALF_UP);
	}
	
	public static BigDecimal getCeiling(int digit,float value){
		BigDecimal   bDecimal  =   new BigDecimal(value);  
		return bDecimal.setScale(digit, BigDecimal.ROUND_CEILING);  
	}
	
	public static BigDecimal getFloor(int digit,float value){
		BigDecimal   bDecimal  =   new BigDecimal(value);  
		return bDecimal.setScale(digit, BigDecimal.ROUND_FLOOR);  
	}
	
	public static BigDecimal getHalfUp(int digit,float value){
		BigDecimal   bDecimal  =   new BigDecimal(value);  
		return bDecimal.setScale(digit, BigDecimal.ROUND_HALF_UP);
	}
	
	public static float getCeilingFloat(int digit,float value){
         return getCeiling(digit, value).floatValue();  
	}
	
	public static float getFloorFloat(int digit,float value){
        return getFloor(digit, value).floatValue();  
	}
	
	public static float getHalfUpFloat(int digit,float value){
        return getHalfUp(digit, value).floatValue();
	}
}
