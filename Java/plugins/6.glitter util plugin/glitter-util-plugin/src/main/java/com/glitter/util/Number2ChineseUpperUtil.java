package com.glitter.util;

import java.math.BigDecimal;

/**
 * 数字转中文大写
 *
 * @author limengjun
 * @date 2020/7/22 10:51
 **/
public class Number2ChineseUpperUtil {

    public static String digitCapital(double n) {
        String fraction[] = {"角", "分"};
        String digit[] = {"零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖"};
        String unit[][] = {{"元", "万", "亿"}, {"", "拾", "佰", "仟"}};

        String head = n < 0 ? "负" : "";
        // 如果是负数取绝对值
        n = Math.abs(n);
        String s = "";
        BigDecimal bigDecimal = new BigDecimal(Double.valueOf(n).toString());
        String nStr = bigDecimal.toString();
        // 小数部分
        String[] split = nStr.split("\\.");
        if (split.length > 1) {
            // 小数点为特殊符号，在分割时需进行转义
            String decimalStr = split[1];
            if (decimalStr.length() > 2) {
                decimalStr = decimalStr.substring(0, 2);
            }
            // 将小数部分转换为整数
            Integer integer = Integer.valueOf(decimalStr);
            String p = "";
            for (int i = 0; i < decimalStr.length() && i < fraction.length; i++) {
                p = digit[integer % 10] + fraction[decimalStr.length() - i - 1] + p;
                integer = integer / 10;
            }
            s = p.replaceAll("(零.)+", "") + s;
        }
        if (s.length() < 1) {
            s = "整";
        }
        int integerPart = (int)Math.floor(n);
        // 整数部分
        for (int i = 0; i < unit[0].length && integerPart > 0; i++) {
            String p = "";
            for (int j = 0; j < unit[1].length && n > 0; j++) {
                p = digit[integerPart % 10] + unit[1][j] + p;
                integerPart = integerPart / 10;
            }
            s = p.replaceAll("(零.)*零$", "").replaceAll("^$", "零") + unit[0][i] + s;
        }
        return head + s.replaceAll("(零.)*零元", "元").replaceFirst("(零.)+", "").replaceAll("(零.)+", "零").replaceAll("^整$", "零元整");
    }

    public static void main(String[] args) {
//        long t1 = System.currentTimeMillis();
//        System.err.println(digitCapital(1234340899.011231));
//        long t2 = System.currentTimeMillis();
//        System.err.println(t2 - t1);
//        long t3 = System.currentTimeMillis();
//        System.err.println(digitCapital(1234340899.011231));
//        long t4 = System.currentTimeMillis();
//        System.err.println(t4 - t3);

        long t1 = System.currentTimeMillis();
        System.err.println(digitCapital(40899.01));
        long t2 = System.currentTimeMillis();
        System.err.println(t2 - t1);
        long t3 = System.currentTimeMillis();
        System.err.println(digitCapital(340899.211231));
        long t4 = System.currentTimeMillis();
        System.err.println(t4 - t3);
    }

}
