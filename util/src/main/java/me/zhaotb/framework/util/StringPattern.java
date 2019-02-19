package me.zhaotb.framework.util;

import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * 字符串模糊匹配： 支持  *(多个字符)  ?(单个字符).
 * 如果本是为正则表达式，则会破坏其结构，此时应该用 {@link Pattern}
 * @see Pattern
 * @author zhaotangbo
 * @date 2019/2/19
 */
public class StringPattern {

    private Pattern pattern;

    private StringPattern(String regex){
       pattern = Pattern.compile(regex);
    }

    public String pattern() {
        return pattern.pattern();
    }

    @Override
    public String toString() {
        return pattern.toString();
    }

    public Matcher matcher(CharSequence input) {
        return pattern.matcher(input);
    }

    public int flags() {
        return pattern.flags();
    }

    public String[] split(CharSequence input, int limit) {
        return pattern.split(input, limit);
    }

    public String[] split(CharSequence input) {
        return pattern.split(input);
    }

    public Predicate<String> asPredicate() {
        return pattern.asPredicate();
    }

    public Stream<String> splitAsStream(CharSequence input) {
        return pattern.splitAsStream(input);
    }

    /**
     *
     * @param searchStr  支持  *(多个字符)  ?(单个字符)
     * @return 返回模式对象
     */
    public static StringPattern compile(String searchStr){
        String regex = searchStr.replaceAll("\\*", "\\\\S+");
        regex = regex.replaceAll("\\?", "\\\\S");
        return new StringPattern(regex);
    }

}
