package me.zhaotb.leecode.day20201216;


import java.util.LinkedList;
import java.util.List;

/**
 * 给你一个字符串 s。请你按照单词在 s 中的出现顺序将它们全部竖直返回。
 * 单词应该以字符串列表的形式返回，必要时用空格补位，但输出尾部的空格需要删除（不允许尾随空格）。
 * 每个单词只能放在一列上，每一列中也只能有一个单词。
 *
 *
 *
 * 示例 1：
 *
 * 输入：s = "HOW ARE YOU"
 * 输出：["HAY","ORO","WEU"]
 * 解释：每个单词都应该竖直打印。
 *  "HAY"
 *  "ORO"
 *  "WEU"
 *
 * 示例 2：
 *
 * 输入：s = "TO BE OR NOT TO BE"
 * 输出：["TBONTB","OEROOE","   T"]
 * 解释：题目允许使用空格补位，但不允许输出末尾出现空格。
 * "TBONTB"
 * "OEROOE"
 * "   T"
 *
 * 示例 3：
 *
 * 输入：s = "CONTEST IS COMING"
 * 输出：["CIC","OSO","N M","T I","E N","S G","T"]
 *
 *
 *
 * 提示：
 *
 *     1 <= s.length <= 200
 *     s 仅含大写英文字母。
 *     题目数据保证两个单词之间只有一个空格。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/print-words-vertically
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 * @author zhaotangbo
 * @since 2020/12/16
 */
public class PrintWords {

    public List<String> printVertically(String s) {
        char space = ' ';
        int maxLen = Integer.MIN_VALUE;//单词最大长度
        List<String> words = new LinkedList<>();
        StringBuilder sb = new StringBuilder();
        char[] chars = s.toCharArray();
        for (char c : chars) {
            if (space == c) {
                words.add(sb.toString());
                if (sb.length() > maxLen) {
                    maxLen = sb.length();
                }
                sb.delete(0, sb.length());
            } else {
                sb.append(c);
            }
        }
        words.add(sb.toString());
        if (sb.length() > maxLen) {
            maxLen = sb.length();
        }

        char[][] dp = new char[maxLen][words.size()];
        for (int i = 0; i < words.size(); i++) {
            String word = words.get(i);
            for (int j = 0; j < word.length(); j++) {
                dp[j][i] = word.charAt(j);
            }
            for (int j = word.length(); j < maxLen; j++) {
                dp[j][i] = space;
            }
        }

        List<String> res = new LinkedList<>();
        for (int i = 0; i < maxLen; i++) {
            int limit = words.size() - 1;
            while (dp[i][limit] == space)limit -= 1;
            res.add(new String(dp[i], 0, limit + 1));
        }

        return res;
    }

}
