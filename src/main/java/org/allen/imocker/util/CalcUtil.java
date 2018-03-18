package org.allen.imocker.util;

import org.apache.commons.lang3.StringUtils;
import org.mvel2.MVEL;
import org.mvel2.compiler.CompiledExpression;
import org.mvel2.compiler.ExpressionCompiler;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by fuyong on 2018/1/24.
 */
public class CalcUtil {

    /**
     * MVEL表达式比较运算符,编译模式
     *
     * @param x
     * @param oper
     * @param y
     * @return true or false
     */
    public static boolean compare(Object x, String oper, Object y) {
        // 操作符为null或者""直接返回false
        if (StringUtils.isEmpty(oper)) {
            return false;
        }

        // 传入操作符,返回表达式
        String express = getMvelExpression(oper);

        ExpressionCompiler compiler = new ExpressionCompiler(express);
        CompiledExpression exp = compiler.compile();

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("x", x);
        params.put("y", y);
        Object result = MVEL.executeExpression(exp, params);
        return (boolean) result;
    }

    /**
     * 根据传入的操作符获取MVEL表达式,例如x > y,x >= y
     *
     * @param oper
     * @return
     */
    public static String getMvelExpression(String oper) {
        StringBuffer strBuffer = new StringBuffer();
        strBuffer.append("x");
        strBuffer.append(oper);
        strBuffer.append("y");
        return strBuffer.toString();
    }

}
