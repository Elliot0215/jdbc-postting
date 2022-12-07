package com.wp.utils;
 
/**
 * 通用属性解析器 用于解析占位符标签
 */
public class GenericTokenParser {
 
    /**
     * 占位符开始标志
     */
    private final String openToken;
    /**
     * 占位符结束标志
     */
    private final String closeToken;
    /**
     *
     */
    private final TokenHandler handler;
 
    public GenericTokenParser(String openToken, String closeToken, TokenHandler handler) {
        this.openToken = openToken;
        this.closeToken = closeToken;
        this.handler = handler;
    }
 
    /**
     *这段代码主要处理{@code text}拥有多个符合以{@code openToken}开头，{@code closeToken}结尾的字符窜的情况
     *
     * 同时还要处理拥有{@code openToken}或{@code closeToken}，但是使用了转义字符的情况。
     *
     * @param text
     * @return
     */
    public String parse(String text) {
        //非空判断
        if (text == null || text.isEmpty()) {
            return "";
        }
        // search open token
        //获取第一个{@code openToken}的位置
        int start = text.indexOf(openToken);
        //如果这个位置不存在 则直接返回原字符窜
        if (start == -1) {
            return text;
        }
        //生成原字符窜的数组
        char[] src = text.toCharArray();
        //字符窜拥有多个符合条件的{@code openToken}时，将会进行多轮分析，以确认每一轮的{@code openToken}在原字符窜的位置，而
        //offset就表示每一轮解析时，应该从原字符窜的哪个位置开始
        int offset = 0;
        //builder是拼接最后结果，进行输出的
        final StringBuilder builder = new StringBuilder();
        //expression的内容表示{@code openToken}和{@code closeToken}之间的内容
        StringBuilder expression = null;
        //下面这个循环 就是循环处理多个{@code openToken}、{@code closeToken}的情况
        while (start > -1) {
 
            if (start > 0 && src[start - 1] == '\\') {
                //寻找{@code openToken}的条件分支一：这一个条件判断 是处理出现了{@code openToken}，但是这个{@code openToken}前面出现了转移字符
                // this open token is escaped. remove the backslash and continue.
                //这里表示既然遇到了转义字符 那么这个开始标识符不能当做开始标识符
                // 因此它不是需要替换的部分，所以就要将从本轮开启的位置 到{@code openToken}结束位置的字符都直接拼接到{@code builder}上
                builder.append(src, offset, start - offset - 1).append(openToken);
                //确认新一轮的开始位置
                offset = start + openToken.length();
            } else {
                //寻找{@code openToken}的条件分支二：下面的条件判断表示 出现了{@code openToken} 且 这个{@code openToken}前面没有转移字符的情况===
                // found open token. let's search close token.
                //重置复用expression
                if (expression == null) {
                    expression = new StringBuilder();
                } else {
                    expression.setLength(0);
                }
                //这里表示如果有转义字符 则拼接转义的开始字符到真正的开始字符之间的部分
                builder.append(src, offset, start - offset);
                //{@code openToken}找到了，接下来来需要找{@code closeToken}，其实{@code closeToken}的状况和{@code openToken}
                //一样的情况
                offset = start + openToken.length();
                int end = text.indexOf(closeToken, offset);
                //遍历循环一直找{@code closeToken}的位置
                while (end > -1) {
                    if (end > offset && src[end - 1] == '\\') {
                        //寻找{@code closeToken}的分支条件一：如果找到的{@code closeToken}是拥有转义字符的，则继续寻找，但是expression需要拼接本轮解析开始
                        //位置到{@code openToken}间的字符，因为这个也属于{@code openToken}和{@code closeToken}间的内容，然后进行下一轮
                        // this close token is escaped. remove the backslash and continue.
                        expression.append(src, offset, end - offset - 1).append(closeToken);
                        offset = end + closeToken.length();
                        end = text.indexOf(closeToken, offset);
                    } else {
                        //寻找{@code closeToken}的分支条件二：这里表示找到了符合条件的{@code closeToken}，那么将内容拼接到{@code expression}里
                        expression.append(src, offset, end - offset);
                        break;
                    }
                }
                if (end == -1) {
                    //综合评定 条件分支一：{@code closeToken}位置没有找到，那么结束了，直接拼接
                    // close token was not found.
                    builder.append(src, start, src.length - start);
                    offset = src.length;
                } else {
                    //综合评定 条件分支二：{@code closeToken}位置也找到了，那么说明expression里也存放好了{@code openToken}和{@code closeToken}
                    //内容，这时候用handler去处理。
                    builder.append(handler.handleToken(expression.toString()));
                    offset = end + closeToken.length();
                }
            }
            //这里表示 从offset位置 从新获取start的位置，很显然如果为0，start还是不变
            start = text.indexOf(openToken, offset);
        }
        if (offset < src.length) {
            builder.append(src, offset, src.length - offset);
        }
        return builder.toString();
    }
}