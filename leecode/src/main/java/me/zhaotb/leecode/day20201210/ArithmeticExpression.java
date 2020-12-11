package me.zhaotb.leecode.day20201210;


import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Stack;

import static me.zhaotb.leecode.day20201210.ArithmeticExpression.Op.*;

/**
 * @author zhaotangbo
 * @since 2020/12/11
 */
public class ArithmeticExpression {

    public enum Op {
        ADD("+", 1), SUB("-", 1), MULTIPLY("*", 5), DIVIDE("/", 5), LP("(", -1),
        RP(")", -1);

        public static final HashMap<String, Op> allMap = new HashMap<>();

        static {
            for (Op op : values()) {
                allMap.put(op.mark, op);
            }
        }

        private String mark;
        private int priority;

        Op(String mark, int priority) {
            this.mark = mark;
            this.priority = priority;
        }

    }

    public List<String> parseInfixExpression(String exp) {
        if (StringUtils.isBlank(exp)) return Collections.emptyList();
        HashSet<String> valid = new HashSet<>(Op.allMap.keySet());
        ArrayList<String> res = new ArrayList<>();
        int idx = 0;
        StringBuilder number = new StringBuilder();
        do {
            if (valid.contains(String.valueOf(exp.charAt(idx)))) {
                String n = number.toString().trim();
                if (n.length() > 0) {
                    res.add(n);
                    number.delete(0, number.length());
                }
                res.add(String.valueOf(exp.charAt(idx)));
            } else {
                number.append(exp.charAt(idx));
            }
        } while (++idx < exp.length());
        if (number.length() > 0){
            res.add(number.toString().trim());
        }
        return res;
    }

    public List<String> infix2Postfix(List<String> infix) {
        ArrayList<String> postfix = new ArrayList<>();

        Stack<Op> opStack = new Stack<>();
        HashMap<String, Op> allMap = Op.allMap;
        for (String exp : infix) {
            Op op = allMap.get(exp);
            if (op == null){//数字
                postfix.add(exp);
            } else if (opStack.empty() || LP == op || opStack.peek().priority <= op.priority) {
                opStack.push(op);
            } else if (RP == op) {
                Op pop;
                while (!opStack.empty() && LP != (pop = opStack.pop())) {
                    postfix.add(pop.mark);
                }
            } else {
                Op lastOp;
                while (!opStack.empty() && LP != (lastOp = opStack.peek()) && lastOp.priority > op.priority) {
                    lastOp = opStack.pop();
                    postfix.add(lastOp.mark);
                }
                opStack.push(op);
            }
        }
        while (!opStack.empty()){
            postfix.add(opStack.pop().mark);
        }

        return postfix;
    }

    public Double calculate(List<String> postfix) {
        Stack<Double> stack = new Stack<>();
        HashMap<String, Op> allMap = Op.allMap;
        for (String exp : postfix) {
            Op op = allMap.get(exp);
            if (op == null){
                stack.push(Double.valueOf(exp));
            } else {
                switch (op) {
                    case ADD:
                        Double d1 = stack.pop();
                        Double d2 = stack.pop();
                        stack.push(d1 + d2);
                        break;
                    case SUB:
                        d1 = stack.pop();
                        d2 = stack.pop();
                        stack.push(d2 - d1);
                        break;
                    case MULTIPLY:
                        d1 = stack.pop();
                        d2 = stack.pop();
                        stack.push(d1 * d2);
                        break;
                    case DIVIDE:
                        d1 = stack.pop();
                        d2 = stack.pop();
                        stack.push(d2 / d1);
                        break;
                }
            }
        }

        return stack.pop();
    }

}
