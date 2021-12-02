package com.topit.mylibrary.util;

/**
 * @ClassName: MathUtils
 * @Description: java类作用描述
 * @Author: ls
 * @CreateDate: 2021/4/23 11:20
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/4/23 11:20
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class MathUtils {

    /**
     * @Description: 随机数生成工具类
     * @author: Yangxf
     * @date: 2019/4/14 12:38
     */

    private static final String DEFAULT_DIGITS = "0";
    private static final String FIRST_DEFAULT_DIGITS = "1";

    /**
     * @param target 目标数字
     * @param length 需要补充到的位数, 补充默认数字[0], 第一位默认补充[1]
     * @return 补充后的结果
     */
    public static String makeUpNewData(String target, int length) {
        return makeUpNewData(target, length, DEFAULT_DIGITS);
    }

    /**
     * @param target 目标数字
     * @param length 需要补充到的位数
     * @param add    需要补充的数字, 补充默认数字[0], 第一位默认补充[1]
     * @return 补充后的结果
     */
    public static String makeUpNewData(String target, int length, String add) {
        if (target.startsWith("-")) target.replace("-", "");
        if (target.length() >= length) return target.substring(0, length);
        StringBuffer sb = new StringBuffer(FIRST_DEFAULT_DIGITS);
        for (int i = 0; i < length - (1 + target.length()); i++) {
            sb.append(add);
        }
        return sb.append(target).toString();
    }

    /**
     * 生产一个随机的指定位数的字符串数字
     *
     * @param length
     * @return
     */
    public static String randomDigitNumber(int length) {
        int rs = (int) ((Math.random() * 9 + 1) * Math.pow(10, length - 1));
        return String.valueOf(rs);
    }
}
