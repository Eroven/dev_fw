package me.zhotb.oauth.common;

/**
 * @author zhaotangbo
 * @date 2018/11/23
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {

    /**
     * 将蛇型命名字符串转换为驼峰命名方式<br>
     * 例子:  <br>
     *  <strong>1. root_123  ->  root123</strong>
     *  <strong>2. first_name  ->  firstName</strong>
     *  <strong>2. first_name  ->  firstName</strong>
     * @param snake 蛇形字符串
     * @return 驼峰字符串
     */
    public static String snake2Camel(String snake){
        if (snake == null){
            return null;
        }
        StringBuilder sb = new StringBuilder();
        char[] chars = snake.toCharArray();
        int len = chars.length - 1;
        sb.append(chars[0]);
        for (int i = 1; i < len; i++) {

        }
        return sb.toString();
    }
}
