/*
 * 文件名（可选），如 CodingRuler.java
 * 
 * 版本信息（可选），如：@version 1.0.0
 * 
 * 版权申明（开源代码一般都需要添加），如：Copyright (C) 2010-2013 SINA Corporation.
 */

package com.sina.weibo.sdk.codestyle;

/**
 * 类的大体描述放在这里。
 * 
 * <p>
 * <b>NOTE：以下部分为一个简要的编码规范，更多规范请参考 ORACLE 官方文档。</b><br>
 * 地址：http://www.oracle.com/technetwork/java/codeconventions-150003.pdf<br>
 * 另外，请使用 UTF-8 格式来查看代码，避免出现中文乱码。<br>
 * <b>至于注释应该使用中文还是英文，请自己行决定，根据公司或项目的要求而定，推荐使用英文。</b><br>
 * </p>
 * <h3>1. 整理代码</h3>
 * <ul>
 *    <li>1.1. Java 代码中不允许出现在警告，无法消除的警告要用 @SuppressWarnings。
 *    <li>1.2. 去掉无用的包、方法、变量等，减少僵尸代码。
 *    <li>1.3. 使用 Lint 工具来查看并消除警告和错误。
 *    <li>1.4. 使用 Ctrl+Shift+F 来格式化代码，然后再进行调整。
 *    <li>1.5. 使用 Ctrl+Shift+O 来格式化 Import 包。
 * </ul>
 * 
 * <h3>2. 命名规则</h3>
 *    <h3>2.1. 基本原则</h3>
 *    <ul>
 *         <li>2.1.1. 变量，方法，类命名要表义，严格禁止使用 name1, name2 等命名。
 *         <li>2.1.2. 命名不能太长，适当使用简写或缩写。（最好不要超过 25 个字母）
 *         <li>2.1.3. 方法名以小写字母开始，以后每个单词首字母大写。
 *         <li>2.1.4. 避免使用相似或者仅在大小写上有区别的名字。
 *         <li>2.1.5. 避免使用数字，但可用 2 代替 to，用 4 代替 for 等，如 go2Clean。
 *     </ul>
 *    
 *    <h3>2.2. 类、接口</h3>
 *    <ul>
 *         <li>2.2.1. 所有单词首字母都大写。使用能确切反应该类、接口含义、功能等的词。一般采用名词。
 *         <li>2.2.2. 接口带 I 前缀，或able, ible, er等后缀。如ISeriable。
 *    </ul>
 *    
 *    <h3>2.3. 字段、常量</h3>
 *    <ul>
 *         <li>2.3.1. 成员变量以 m 开头，静态变量以 s 开头，如 mUserName, sInstance。
 *         <li>2.3.2. 常量全部大写，在词与词之前用下划线连接，如 MAX_NUMBER。
 *         <li>2.3.3. 代码中禁止使用硬编码，把一些数字或字符串定义成常用量。
 *         <li>2.3.4. 对于废弃不用的函数，为了保持兼容性，通常添加 @Deprecated，如 {@link #doSomething()}
 *    </ul>
 *         
 * <h3>3. 注释</h3>
 *    请参考 {@link #SampleCode} 类的注释。
 *    <ul>
 *    <li>3.1. 常量注释，参见 {@link #ACTION_MAIN} 
 *    <li>3.2. 变量注释，参见 {@link #mObject0} 
 *    <li>3.3. 函数注释，参见 {@link #doSomething(int, float, String)}
 *    </ul> 
 *    
 * <h3>4. Class 内部顺序和逻辑</h3>
 * <ul>
 *    <li>4.1. 每个 class 都应该按照一定的逻辑结构来排列基成员变量、方法、内部类等，
 *             从而达到良好的可读性。
 *    <li>4.2. 总体上来说，要按照先 public, 后 protected, 最后 private, 函数的排布
 *             也应该有一个逻辑的先后顺序，由重到轻。
 *    <li>4.3. 以下顺序可供参考：<br>
 *         定义TAG，一般为 private（可选）<br>
 *         定义 public 常量<br>
 *         定义 protected 常量、内部类<br>
 *         定义 private 变量<br>
 *         定义 public 方法<br>
 *         定义 protected 方法<br>
 *         定义 private 方法<br>
 * </ul>        
 * 
 * <h3>5. 表达式与语句</h3>
 *    <h3>5.1. 基本原则：采用紧凑型风格来编写代码</h3>
 *    <h3>5.2. 细则</h3>
 *    <ul>
 *         <li>5.2.1. 条件表示式，参见 {@link #conditionFun(boolean)} 
 *         <li>5.2.2. switch 语句，参见 {@link #switchFun(int)}
 *         <li>5.2.3. 循环语句，参见 {@link #circulationFun(boolean)}
 *         <li>5.2.4. 错误与异常，参见 {@link #exceptionFun()}
 *         <li>5.2.5. 杂项，参见 {@link #otherFun()}
 *         <li>5.2.6. 批注，参见 {@link #doSomething(int, float, String)}
 *     </ul>
 * 
 * @author 作者名
 * @since 2013-XX-XX
 */
@SuppressWarnings("unused")
public class CodingRuler {

    /** 公有的常量注释 */
    public static final String ACTION_MAIN = "android.intent.action.MAIN";
    
    /** 私有的常量注释（同类型的常量可以分块并紧凑定义） */
    private static final int MSG_AUTH_NONE    = 0;
    private static final int MSG_AUTH_SUCCESS = 1;
    private static final int MSG_AUTH_FAILED  = 2;
    
    /** 保护的成员变量注释 */
    protected Object mObject0;
    
    /** 私有的成员变量 mObject1 注释（同类型的成员变量可以分块并紧凑定义） */
    private Object mObject1;
    /** 私有的成员变量 mObject2 注释 */
    private Object mObject2;
    /** 私有的成员变量 mObject3 注释 */
    private Object mObject3;
    
    /**
     * 对于注释多于一行的，采用这种方式来
     * 定义该变量
     */
    private Object mObject4;

    /**
     * 公有方法描述...
     * 
     * @param param1  参数1描述...
     * @param param2  参数2描述...
     * @param paramXX 参数XX描述... （注意：请将参数、描述都对齐）
     */
    public void doSomething(int param1, float param2, String paramXX) {
        // 以下注释标签可以通过Eclipse内置的Task插件看到
        // TODO  使用TODO来标记代码，说明标识处有功能代码待编写
        // FIXME 使用FIXME来标记代码，说明标识处代码需要修正，甚至代码是
        //       错误的，不能工作，需要修复
        // XXX   使用XXX来标记代码，说明标识处代码虽然实现了功能，但是实现
        //       的方法有待商榷，希望将来能改进
    }
    
    /**
     * 保护方法描述...
     */
    @Deprecated
    protected void doSomething() {
        // ...implementation
    }
    
    /**
     * 私有方法描述...
     * 
     * @param param1  参数1描述...
     * @param param2  参数2描述...
     */
    private void doSomethingInternal(int param1, float param2) {
        // ...implementation        
    }
    
    /**
     * 条件表达式原则。
     */
    private void conditionFun() {
        boolean condition1 = true;
        boolean condition2 = false;
        boolean condition3 = false;
        boolean condition4 = false;
        boolean condition5 = false;
        boolean condition6 = false;
        
        // 原则： 1. 所有 if 语句必须用 {} 包括起来，即便只有一句，禁止使用不带{}的语句
        //       2. 在含有多种运算符的表达式中，使用圆括号来避免运算符优先级问题
        //       3. 判断条件很多时，请将其它条件换行
        if (condition1) {
            // ...implementation
        }
        
        if (condition1) {
            // ...implementation
        } else {
            // ...implementation
        }
        
        if (condition1)          /* 禁止使用不带{}的语句 */
            condition3 = true;
        
        if ((condition1 == condition2) 
                || (condition3 == condition4)
                || (condition5 == condition6)) {
            
        }
    }
    
    /**
     * Switch语句原则。
     */
    private void switchFun() {
        
        // 原则： 1. switch 语句中，break 与下一条 case 之间，空一行
        //       2. 对于不需要 break 语句的，请使用 /* Falls through */来标注
        //       3. 请默认写上 default 语句，保持完整性
        int code = MSG_AUTH_SUCCESS;
        switch (code) {
        case MSG_AUTH_SUCCESS:
            break;
            
        case MSG_AUTH_FAILED:
            break;
            
        case MSG_AUTH_NONE:
            /* Falls through */
        default:
            break;
        }
    }
    
    /**
     * 循环表达式。
     */
    private void circulationFun() {
        
        // 原则： 1. 尽量使用for each语句代替原始的for语句
        //       2. 循环中必须有终止循环的条件或语句，避免死循环
        //       3. 循环要尽可能的短, 把长循环的内容抽取到方法中去
        //       4. 嵌套层数不应超过3层, 要让循环清晰可读
        
        int array[] = { 1, 2, 3, 4, 5 };
        for (int data : array) {
            // ...implementation
        }
        
        int length = array.length;
        for (int ix = 0; ix < length; ix++) {
            // ...implementation
        }
        
        boolean condition = true;
        while (condition) {
            // ...implementation
        }
        
        do {
            // ...implementation
        } while (condition);
    }
    
    /**
     * 异常捕获原则。
     */
    private void exceptionFun() {
        
        // 原则： 1. 捕捉异常是为了处理它，通常在异常catch块中输出异常信息。
        //       2. 资源释放的工作，可以放到 finally 块部分去做。如关闭 Cursor 等。
        try {
            // ...implementation
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            
        }
    }
    
    /**
     * 其它原则（整理中...）。
     */
    private void otherFun() {
        // TODO
    }    
}
