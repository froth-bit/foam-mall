package com.fmwyc.common.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public class NumUtils {

    //加
    public static BigDecimal addPrecise(Object a,Object b){
        return getBigDecimal(a).add(getBigDecimal(b));
    }

    //减
    public static BigDecimal subPrecise(Object a,Object b){
        return getBigDecimal(a).subtract(getBigDecimal(b));
    }

    //乘
    public static BigDecimal mulPrecise(Object a,Object b){
        return getBigDecimal(a).multiply(getBigDecimal(b));
    }

    //除法默认2位 4舍5入
    public static BigDecimal divPrecise(Object a,Object b){
        return getBigDecimal(a).divide(getBigDecimal(b),2,RoundingMode.HALF_UP);
    }

    //除
    public static BigDecimal divPrecise(Object a,Object b,int i,RoundingMode mode){
        return getBigDecimal(a).divide(getBigDecimal(b),i,mode);
    }

    /**
     * 先转换Object-->BigDecimal
     * @param value
     * @return
     */
    public static BigDecimal getBigDecimal(Object value) {
        BigDecimal ret = null;
        if( value != null ) {
            if( value instanceof BigDecimal ) {
                ret = (BigDecimal) value;
            } else if( value instanceof String ) {
                ret = new BigDecimal( (String) value );
            } else if( value instanceof BigInteger) {
                ret = new BigDecimal( (BigInteger) value );
            } else if( value instanceof Number ) {
                ret = BigDecimal.valueOf(((Number) value).doubleValue());
            } else {
                throw new ClassCastException("不可能强制 ["+value+"] 从 "+value.getClass()+" 转到BigDecimal。");
            }
        }
        return ret;
    }

}
