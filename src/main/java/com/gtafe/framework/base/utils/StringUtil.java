package com.gtafe.framework.base.utils;

import com.gtafe.data.center.dataetl.datasource.utils.ConnectDB;
import com.gtafe.data.center.dataetl.datasource.vo.DatasourceVO;
import com.gtafe.data.center.information.data.vo.DataStandardItemVo;
import com.gtafe.data.center.system.config.vo.SysConfigVo;
//import sun.misc.BASE64Encoder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 字符串的一些实用操作
 */
public class StringUtil {
    public static int checkConnection(Map<Integer, Object[]> connMap, int id) {
        if (!connMap.containsKey(id)) {
            return 2;
        }
        Object[] obj = connMap.get(id);
        DatasourceVO vo = (DatasourceVO) obj[0];
        int status = (Integer) obj[1];
        if (status >= 0) {
            return status;
        }
        ConnectDB tDb = StringUtil.getEntityBy(vo);
        Connection connection = null;
        status = 2;
        try {
            connection = tDb.getConn();
            if (connection != null) {
                status = 1;
            } else {
                boolean machineFlag = StringUtil.ping(vo.getHost(), 1, 2);
                if (machineFlag) {
                    status = 2;
                } else {
                    status = 3;
                }
            }
        } finally {
            tDb.closeDbConn(connection);
        }
        obj[1] = status;
        return status;
    }

    /**
     * 功能描述：分割字符串
     *
     * @param str       String 原始字符串
     * @param splitsign String 分隔符
     * @return String[] 分割后的字符串数组
     */
    public static String[] split(String str, String splitsign) {
        int index;
        if (str == null || splitsign == null) {
            return null;
        }
        ArrayList<String> al = new ArrayList<String>();
        while ((index = str.indexOf(splitsign)) != -1) {
            al.add(str.substring(0, index));
            str = str.substring(index + splitsign.length());
        }
        al.add(str);
        return (String[]) al.toArray(new String[0]);
    }

    public static List<String> splitList(String str) {
        List<String> result = new ArrayList<String>();
        if (isBlank(str)) {
            return result;
        }
        String splitsign = ",";
        String[] strs = str.trim().split(splitsign);
        if (strs != null) {
            for (String s : strs) {
                result.add(s);
            }
        }
        return result;
    }

    public static String join(List<?> list) {
        return join(list, ",");
    }

    public static String join(List<?> list, String splitsign) {
        if (list == null || list.isEmpty()) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < list.size(); i++) {
            if (i > 0) {
                sb.append(splitsign);
            }
            sb.append(list.get(i));
        }
        return sb.toString();
    }

    public static String getComment(int dbType, String dataExplain) {
        String comment = "";
        if (dbType == 1) {
            comment = "  comment '" + dataExplain + "' ";
        }
        return comment;
    }

    public static String getColumnType(String dataType, int dbType, String dataLength) {
        String columnType = "";
        if (dbType == 1) {
            switch (dataType) {
                case "C":
                    columnType = "varchar";
                    break;
                case "D":
                    columnType = "datetime";
                    break;
                case "N":
                    if (StringUtil.isNotBlank(dataLength)) {
                        columnType = getFromDataLength(dataLength, 1);
                    } else {
                        columnType = "int";
                    }
                    break;
                case "M":
                    columnType = "decimal";
                    break;
                case "B":
                    columnType = "blob";
                    break;
                case "T":
                    columnType = "text";
                    break;
                case "L":
                    columnType = "varchar";
                    break;
            }
        } else if (dbType == 2) {
            switch (dataType) {
                case "C":
                    columnType = "varchar2";
                    break;
                case "D":
                    columnType = "date";
                    break;
                case "N":
                    if (StringUtil.isNotBlank(dataLength)) {
                        columnType = getFromDataLength(dataLength, 2);
                    } else {
                        columnType = "number";
                    }
                    break;
                case "M":
                    columnType = "number";
                    break;
                case "B":
                    columnType = "blob";
                    break;
                case "T":
                    columnType = "clob";
                    break;
                case "L":
                    columnType = "varchar2";
                    break;
            }
        } else {
            switch (dataType) {
                case "C":
                    columnType = "varchar";
                    break;
                case "D":
                    columnType = "datetime";
                    break;
                case "N":
                    if (StringUtil.isNotBlank(dataLength)) {
                        columnType = getFromDataLength(dataLength, 3);
                    } else {
                        columnType = "int";
                    }
                    break;
                case "M":
                    columnType = "decimal";
                    break;
                case "B":
                    columnType = "blob";
                    break;
                case "T":
                    columnType = "text";
                    break;
                case "L":
                    columnType = "varchar";
                    break;
            }
        }
        return columnType;
    }

    private static String getFromDataLength(String dataLength, int d) {
        String s[] = dataLength.split(",");
        if (s.length > 1) {
            //说明有逗号
            return "decimal";
        } else {
            Integer aa = Integer.parseInt(s[0]);
            if (aa <= 10) {
                if (d == 1 || d == 3) {
                    return "int";
                } else {
                    return "number";
                }
            } else if (aa > 10 && aa < 19) {
                return "bigint";
            } else {
                return "decimal";
            }
        }
    }

    public static List<Integer> splitListInt(String str) {
        List<Integer> result = new ArrayList<Integer>();
        if (isBlank(str)) {
            return result;
        }
        String splitsign = ",";
        String[] strs = str.trim().split(splitsign);
        if (strs != null) {
            for (String s : strs) {
                result.add(Integer.parseInt(s));
            }
        }
        return result;
    }

    public static List<String> splitListString(String str) {
        List<String> result = new ArrayList<String>();
        if (isBlank(str)) {
            return result;
        }
        String splitsign = ",";
        String[] strs = str.trim().split(splitsign);
        if (strs != null) {
            for (String s : strs) {
                result.add(s);
            }
        }
        return result;
    }


    /**
     * 利用MD5进行加密
     *
     * @param 待加密的字符串
     * @return 加密后的字符串
     * @throws NoSuchAlgorithmException     没有这种产生消息摘要的算法
     * @throws UnsupportedEncodingException
     */
/*    public static String EncoderByMd5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        //确定计算方法
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        BASE64Encoder base64en = new BASE64Encoder();
        //加密后的字符串
        String newstr = base64en.encode(md5.digest(str.getBytes("utf-8")));
        return newstr;
    }*/
    public final static String MD5(String s) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            byte[] strTemp = s.getBytes();
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(strTemp);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 功能描述：替换字符串
     *
     * @param from   String 原始字符串
     * @param to     String 目标字符串
     * @param source String 母字符串
     * @return String 替换后的字符串
     */
    public static String replace(String from, String to, String source) {
        if (source == null || from == null || to == null)
            return null;
        StringBuffer str = new StringBuffer("");
        int index = -1;
        while ((index = source.indexOf(from)) != -1) {
            str.append(source.substring(0, index) + to);
            source = source.substring(index + from.length());
            index = source.indexOf(from);
        }
        str.append(source);
        return str.toString();
    }

    /**
     * 替换字符串，能能够在HTML页面上直接显示(替换双引号和小于号)
     *
     * @param str String 原始字符串
     * @return String 替换后的字符串
     */
    public static String htmlencode(String str) {
        if (str == null) {
            return null;
        }
        return replace("\"", "&quot;", replace("<", "&lt;", str));
    }

    /**
     * 替换字符串，将被编码的转换成原始码（替换成双引号和小于号）
     *
     * @param str String
     * @return String
     */
    public static String htmldecode(String str) {
        if (str == null) {
            return null;
        }

        return replace("&quot;", "\"", replace("&lt;", "<", str));
    }

    private static final String _BR = "<br/>";

    /**
     * 功能描述：在页面上直接显示文本内容，替换小于号，空格，回车，TAB
     *
     * @param str String 原始字符串
     * @return String 替换后的字符串
     */
    public static String htmlshow(String str) {
        if (str == null) {
            return null;
        }

        str = replace("<", "&lt;", str);
        str = replace(" ", "&nbsp;", str);
        str = replace("\r\n", _BR, str);
        str = replace("\n", _BR, str);
        str = replace("\t", "&nbsp;&nbsp;&nbsp;&nbsp;", str);
        return str;
    }

    /**
     * html字符串转换成纯字符串，删除所有html标签
     *
     * @param str
     * @return
     */
    public static String htmlToText(String str) {
        String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; // 定义script的正则表达式
        String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; // 定义style的正则表达式
        String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
        String regEx_space = "\\s*|\t|\r|\n";// 定义空格回车换行符

        Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
        Matcher m_script = p_script.matcher(str);
        str = m_script.replaceAll(""); // 过滤script标签

        Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
        Matcher m_style = p_style.matcher(str);
        str = m_style.replaceAll(""); // 过滤style标签

        Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(str);
        str = m_html.replaceAll(""); // 过滤html标签

        Pattern p_space = Pattern.compile(regEx_space, Pattern.CASE_INSENSITIVE);
        Matcher m_space = p_space.matcher(str);
        str = m_space.replaceAll(""); // 过滤空格回车标签
        return str.trim();
    }

    /**
     * 功能描述：返回指定字节长度的字符串
     *
     * @param str    String 字符串
     * @param length int 指定长度
     * @return String 返回的字符串
     */
    public static String toLength(String str, int length) {
        if (str == null) {
            return null;
        }
        if (length <= 0) {
            return "";
        }
        try {
            if (str.getBytes("GBK").length <= length) {
                return str;
            }
        } catch (Exception e) {
        }
        StringBuffer buff = new StringBuffer();

        int index = 0;
        char c;
        length -= 3;
        while (length > 0) {
            c = str.charAt(index);
            if (c < 128) {
                length--;
            } else {
                length--;
                length--;
            }
            buff.append(c);
            index++;
        }
        buff.append("...");
        return buff.toString();
    }

    /**
     * 功能描述：判断是否为整数
     *
     * @param str 传入的字符串
     * @return 是整数返回true, 否则返回false
     */
    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]+$");
        return pattern.matcher(str).matches();
    }

    public static String JM(String inStr) {
        char[] a = inStr.toCharArray();
        for (int i = 0; i < a.length; i++) {
            a[i] = (char) (a[i] ^ 't');
        }
        String k = new String(a);
        return k;
    }

    /**
     * 判断字符串长度
     *
     * @param str
     * @return
     */
    public static boolean isLength(String str) {
        return (str.length() <= 6);
    }

    /**
     * 判断是否为浮点数，包括double和float
     *
     * @param str 传入的字符串
     * @return 是浮点数返回true, 否则返回false
     */
    public static boolean isDouble(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?\\d+\\.\\d+$");
        return pattern.matcher(str).matches();
    }

    /**
     * 判断是不是合法字符 c 要判断的字符
     */
    public static boolean isLetter(String str) {
        if (str == null || str.length() < 0) {
            return false;
        }
        Pattern pattern = Pattern.compile("[\\w\\.-_]*");
        return pattern.matcher(str).matches();
    }

    /**
     * 从指定的字符串中提取Email content 指定的字符串
     *
     * @param content
     * @return
     */
    public static String parse(String content) {
        String email = null;
        if (content == null || content.length() < 1) {
            return email;
        }
        // 找出含有@
        int beginPos;
        int i;
        String token = "@";
        String preHalf = "";
        String sufHalf = "";

        beginPos = content.indexOf(token);
        if (beginPos > -1) {
            // 前项扫描
            String s = null;
            i = beginPos;
            while (i > 0) {
                s = content.substring(i - 1, i);
                if (isLetter(s))
                    preHalf = s + preHalf;
                else
                    break;
                i--;
            }
            // 后项扫描
            i = beginPos + 1;
            while (i < content.length()) {
                s = content.substring(i, i + 1);
                if (isLetter(s))
                    sufHalf = sufHalf + s;
                else
                    break;
                i++;
            }
            // 判断合法性
            email = preHalf + "@" + sufHalf;
            if (isEmail(email)) {
                return email;
            }
        }
        return null;
    }

    /**
     * 功能描述：判断输入的字符串是否符合Email样式.
     *
     * @param email 传入的字符串
     * @return 是Email样式返回true, 否则返回false
     */
    public static boolean isEmail(String email) {
        if (email == null || email.length() < 1 || email.length() > 256) {
            return false;
        }
        Pattern pattern = Pattern.compile("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");
        return pattern.matcher(email).matches();
    }

    /**
     * 判断是否为手机号
     *
     * @param mobile
     * @return
     */
    public static boolean isMobile(String mobile) {
        if (isEmpty(mobile)) {
            return false;
        }
        Pattern pattern = Pattern.compile("^1[3|4|5|7|8][0-9]\\d{8}$");
        return pattern.matcher(mobile).matches();
    }


    /**
     * 校验日期格式
     *
     * @return
     */
    public static boolean isRightDate(String date) {
        if (isEmpty(date)) {
            return false;
        }
        Pattern pattern = Pattern.compile("^(\\d{4})-(0\\d{1}|1[0-2])-(0\\d{1}|[12]\\d{1}|3[01])$");
        return pattern.matcher(date).matches();
    }

    /**
     * 判断是否为神通卡号激活码
     *
     * @return
     */
    public static boolean isStkhCode(String code) {
        if (isEmpty(code)) {
            return false;
        }
        Pattern pattern = Pattern.compile("^\\d{6}$");
        return pattern.matcher(code).matches();
    }


    public static void main(String[] args) {
        System.out.println(isRightDate("2017-02-31"));
    }

    /**
     * 功能描述：判断输入的字符串是否为纯汉字
     *
     * @param str 传入的字符窜
     * @return 如果是纯汉字返回true, 否则返回false
     */
    public static boolean isChinese(String str) {
        Pattern pattern = Pattern.compile("[\u0391-\uFFE5]+$");
        return pattern.matcher(str).matches();
    }

    /**
     * 功能描述：是否为空白 包括null和空格
     *
     * @param str
     * @return
     */
    public static boolean isBlank(String str) {
        return str == null || str.trim().length() == 0;
    }

    /**
     * 功能描述：是否不为空白 包括null和空格
     *
     * @param str
     * @return
     */
    public static boolean isNotBlank(String str) {
        return str != null && str.trim().length() > 0;
    }

    /**
     * 功能描述：是否为空白 不包括空格
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    /**
     * 功能描述：是否不为空白 不包括空格
     *
     * @param str
     * @return
     */
    public static boolean isNotEmpty(String str) {
        return str != null && str.length() > 0;
    }

    /**
     * 功能描述：判断是否为质数
     *
     * @param x
     * @return
     */
    public static boolean isPrime(int x) {
        if (x <= 7) {
            if (x == 2 || x == 3 || x == 5 || x == 7)
                return true;
        }
        int c = 7;
        if (x % 2 == 0)
            return false;
        if (x % 3 == 0)
            return false;
        if (x % 5 == 0)
            return false;
        int end = (int) Math.sqrt(x);
        while (c <= end) {
            if (x % c == 0) {
                return false;
            }
            c += 4;
            if (x % c == 0) {
                return false;
            }
            c += 2;
            if (x % c == 0) {
                return false;
            }
            c += 4;
            if (x % c == 0) {
                return false;
            }
            c += 2;
            if (x % c == 0) {
                return false;
            }
            c += 4;
            if (x % c == 0) {
                return false;
            }
            c += 6;
            if (x % c == 0) {
                return false;
            }
            c += 2;
            if (x % c == 0) {
                return false;
            }
            c += 6;
        }
        return true;
    }

    /**
     * 功能描述：人民币转成大写
     *
     * @param str 数字字符串
     * @return String 人民币转换成大写后的字符串
     */
    public static String hangeToBig(String str) {
        double value;
        try {
            value = Double.parseDouble(str.trim());
        } catch (Exception e) {
            return null;
        }
        char[] hunit = {'拾', '佰', '仟'}; // 段内位置表示
        char[] vunit = {'万', '亿'}; // 段名表示
        char[] digit = {'零', '壹', '贰', '叁', '肆', '伍', '陆', '柒', '捌', '玖'}; // 数字表示
        long midVal = (long) (value * 100); // 转化成整形
        String valStr = String.valueOf(midVal); // 转化成字符串

        String head = valStr.substring(0, valStr.length() - 2); // 取整数部分
        String rail = valStr.substring(valStr.length() - 2); // 取小数部分

        String prefix = ""; // 整数部分转化的结果
        String suffix = ""; // 小数部分转化的结果
        // 处理小数点后面的数
        if (rail.equals("00")) { // 如果小数部分为0
            suffix = "整";
        } else {
            suffix = digit[rail.charAt(0) - '0'] + "角" + digit[rail.charAt(1) - '0'] + "分"; // 否则把角分转化出来
        }
        // 处理小数点前面的数
        char[] chDig = head.toCharArray(); // 把整数部分转化成字符数组
        char zero = '0'; // 标志'0'表示出现过0
        byte zeroSerNum = 0; // 连续出现0的次数
        for (int i = 0; i < chDig.length; i++) { // 循环处理每个数字
            int idx = (chDig.length - i - 1) % 4; // 取段内位置
            int vidx = (chDig.length - i - 1) / 4; // 取段位置
            if (chDig[i] == '0') { // 如果当前字符是0
                zeroSerNum++; // 连续0次数递增
                if (zero == '0') { // 标志
                    zero = digit[0];
                } else if (idx == 0 && vidx > 0 && zeroSerNum < 4) {
                    prefix += vunit[vidx - 1];
                    zero = '0';
                }
                continue;
            }
            zeroSerNum = 0; // 连续0次数清零
            if (zero != '0') { // 如果标志不为0,则加上,例如万,亿什么的
                prefix += zero;
                zero = '0';
            }
            prefix += digit[chDig[i] - '0']; // 转化该数字表示
            if (idx > 0)
                prefix += hunit[idx - 1];
            if (idx == 0 && vidx > 0) {
                prefix += vunit[vidx - 1]; // 段结束位置应该加上段名如万,亿
            }
        }

        if (prefix.length() > 0)
            prefix += '圆'; // 如果整数部分存在,则有圆的字样
        return prefix + suffix; // 返回正确表示
    }

    /**
     * 功能描述：去掉字符串中重复的子字符串
     *
     * @param str 原字符串，如果有子字符串则用空格隔开以表示子字符串
     * @return String 返回去掉重复子字符串后的字符串
     */
    public static String removeSameString(String str) {
        Set<String> mLinkedSet = new LinkedHashSet<String>();// set集合的特征：其子集不可以重复
        String[] strArray = str.split(" ");// 根据空格(正则表达式)分割字符串
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < strArray.length; i++) {
            if (!mLinkedSet.contains(strArray[i])) {
                mLinkedSet.add(strArray[i]);
                sb.append(strArray[i] + " ");
            }
        }
        return sb.toString();
    }

    /**
     * 功能描述：过滤特殊字符
     *
     * @param src
     * @return
     */
    public static String encoding(String src) {
        if (src == null)
            return "";
        StringBuilder result = new StringBuilder();
        if (src != null) {
            src = src.trim();
            for (int pos = 0; pos < src.length(); pos++) {
                switch (src.charAt(pos)) {
                    case '\"':
                        result.append("&quot;");
                        break;
                    case '<':
                        result.append("&lt;");
                        break;
                    case '>':
                        result.append("&gt;");
                        break;
                    case '\'':
                        result.append("&apos;");
                        break;
                    case '&':
                        result.append("&amp;");
                        break;
                    case '%':
                        result.append("&pc;");
                        break;
                    case '_':
                        result.append("&ul;");
                        break;
                    case '#':
                        result.append("&shap;");
                        break;
                    case '?':
                        result.append("&ques;");
                        break;
                    default:
                        result.append(src.charAt(pos));
                        break;
                }
            }
        }
        return result.toString();
    }

    /**
     * 功能描述：判断是不是合法的手机号码
     *
     * @param handset
     * @return boolean
     */
    public static boolean isHandset(String handset) {
        try {
            String regex = "^1[\\d]{10}$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(handset);
            return matcher.matches();

        } catch (RuntimeException e) {
            return false;
        }
    }

    /**
     * 功能描述：反过滤特殊字符
     *
     * @param src
     * @return
     */
    public static String decoding(String src) {
        if (src == null)
            return "";
        String result = src;
        result = result.replace("&quot;", "\"").replace("&apos;", "\'");
        result = result.replace("&lt;", "<").replace("&gt;", ">");
        result = result.replace("&amp;", "&");
        result = result.replace("&pc;", "%").replace("&ul", "_");
        result = result.replace("&shap;", "#").replace("&ques", "?");
        return result;
    }


    /**
     * 检查字符串是否为空
     *
     * @param str
     * @return
     */
    public static String checkIsNull(String str) {
        if ((str == null) || ("".equals(str)) || "null".equals(str)) {
            return "";
        }
        return str;
    }

    public static String getLocalIp() {
        InetAddress addr = null;
        String ip = "";
        // String address = "";
        try {
            addr = InetAddress.getLocalHost();
            ip = addr.getHostAddress().toString();// 获得本机IP
            // address = addr.getHostName().toString();// 获得本机名称
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ip;
    }

    public static boolean ping(String ipAddress, int pingTimes, int timeOut) {
        BufferedReader in = null;
        Runtime r = Runtime.getRuntime();  // 将要执行的ping命令,此命令是windows格式的命令
        String pingCommand = "ping " + ipAddress + " -n " + pingTimes + " -w " + timeOut;
        try {   // 执行命令并获取输出
            System.out.println(pingCommand);
            Process p = r.exec(pingCommand);
            if (p == null) {
                return false;
            }
            in = new BufferedReader(new InputStreamReader(p.getInputStream()));   // 逐行检查输出,计算类似出现=23ms TTL=62字样的次数
            int connectedCount = 0;
            String line = null;
            while ((line = in.readLine()) != null) {
                connectedCount += getCheckResult(line);
            }   // 如果出现类似=23ms TTL=62这样的字样,出现的次数=测试次数则返回真
            return connectedCount == pingTimes;
        } catch (Exception ex) {
            ex.printStackTrace();   // 出现异常则返回假
            return false;
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static int getCheckResult(String line) {  // System.out.println("控制台输出的结果为:"+line);
        Pattern pattern = Pattern.compile("(\\d+ms)(\\s+)(TTL=\\d+)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(line);
        while (matcher.find()) {
            return 1;
        }
        return 0;
    }

    /**
     * 根据数据源定义 组装 链接
     *
     * @param datasourceVO
     * @return
     */
    public static ConnectDB getEntityBy(DatasourceVO datasourceVO) {

        ConnectDB connectDB = new ConnectDB();
        if (datasourceVO.getDbType() == 2) {
            connectDB.driver = "oracle.jdbc.driver.OracleDriver";
            connectDB.url = "jdbc:oracle:thin:@" + datasourceVO.getHost() + ":"
                    + datasourceVO.getPort() + ":"
                    + datasourceVO.getDbName();
            connectDB.username = datasourceVO.getUsername();
            connectDB.pwd = datasourceVO.getPassword();
        } else if (datasourceVO.getDbType() == 3) {
            connectDB.driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
            connectDB.url = "jdbc:sqlserver://" + datasourceVO.getHost() + ":"
                    + datasourceVO.getPort() + ";DatabaseName="
                    + datasourceVO.getDbName();
            connectDB.username = datasourceVO.getUsername();
            connectDB.pwd = datasourceVO.getPassword();
        } else {
            connectDB.driver = "com.mysql.jdbc.Driver";
            connectDB.url = "jdbc:mysql://" + datasourceVO.getHost() + ":"
                    + datasourceVO.getPort() + "/"
                    + datasourceVO.getDbName();
            connectDB.username = datasourceVO.getUsername();
            connectDB.pwd = datasourceVO.getPassword();
        }
        return connectDB;
    }

    //"("+itt.getDataLength()+") "
    public static String getEntityLength(String dataType, String dataLength) {
        String lengthstr = "";
        switch (dataType) {
            case "C":
                lengthstr = "(" + dataLength + ")";
                break;
            case "D":
                lengthstr = "";
                break;
            case "N":
                lengthstr = getFromDataLength2(dataLength, dataType);
                break;
            case "M":
                lengthstr = "(" + dataLength + ")";
                break;
            case "B":
                lengthstr = "";
                break;
            case "T":
                lengthstr = "";
                break;
            case "L":
                lengthstr = "(4000)";
                break;
        }
        return lengthstr;
    }

    private static String getFromDataLength2(String dataLength, String dataType) {
        StringBuffer res = new StringBuffer("(");
        if (StringUtil.isNotBlank(dataLength)) {
            String s[] = dataLength.split(",");
            if (s.length > 1) {
                if (Integer.parseInt(s[0]) > 65) {
                    dataLength = "65";
                }
                res = res.append(dataLength);
            } else {
                res = res.append(s[0]);
            }
        } else {
            res = res.append("0");
        }
        res = res.append(")");
        return res.toString();
    }

    public static String dropTableSql(String tableName, int dbType) {
        if (dbType == 1) {
            return "drop table if exists " + tableName;
        }
        return null;
    }

    public static String createSql(String tableName, List<DataStandardItemVo> dataStandardItemVos, int dbType) {
        StringBuffer sb = new StringBuffer(" create table ").append(tableName).append(" ( ");
        for (DataStandardItemVo itt : dataStandardItemVos) {
            sb.append(itt.getItemName()).append(" ")
                    .append(StringUtil.getColumnType(itt.getDataType(), dbType, itt.getDataLength()))
                    .append(StringUtil.getEntityLength(itt.getDataType(), itt.getDataLength()))
                    .append(itt.getDataPrimarykey() == 1 ? " PRIMARY KEY " : "")
                    .append(itt.getDataNullable() == 1 ? "" : " not null ")
                    .append(StringUtil.getComment(dbType, itt.getDataExplain())).append(",");
        }
        String s = sb.toString();
        s = s.substring(0, s.length() - 1);
        StringBuffer sbb = new StringBuffer(s).append(")");
        if (dbType == 1) {
            sbb.append(" ENGINE=InnoDB \n" +
                    " DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci\n" +
                    " ROW_FORMAT=COMPACT; ");
        }
        if (dbType == 2) {
            for (DataStandardItemVo itt : dataStandardItemVos) {
                sbb.append("  comment on column " + tableName + "." + itt.getItemName() + " is '" + itt.getDataExplain() + "'; ");
            }
        }
        if (dbType == 3) {
            for (DataStandardItemVo itt : dataStandardItemVos) {
                sbb.append(" EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'" + itt.getDataExplain() + "' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'" + tableName + "', @level2type=N'COLUMN',@level2name=N'" + itt.getItemName() + "'\n" +
                        "GO \n");
            }
        }
        return sbb.toString();
    }

    public static ConnectDB getEntityBySysConfig(SysConfigVo vo) {
        ConnectDB connectDB = new ConnectDB();
        if (vo.getDbType().equals("2")) {
            connectDB.driver = "oracle.jdbc.driver.OracleDriver";
            connectDB.url = "jdbc:oracle:thin:@" + vo.getIpAddress() + ":"
                    + vo.getPort() + ":"
                    + vo.getDbName();
            connectDB.username = vo.getUsername();
            connectDB.pwd = vo.getPassword();
        } else if (vo.getDbType().equals("3")) {
            connectDB.driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
            connectDB.url = "jdbc:sqlserver://" + vo.getIpAddress() + ":"
                    + vo.getPort() + ";DatabaseName="
                    + vo.getDbName();
            connectDB.username = vo.getUsername();
            connectDB.pwd = vo.getPassword();
        } else {
            connectDB.driver = "com.mysql.jdbc.Driver";
            connectDB.url = "jdbc:mysql://" + vo.getIpAddress() + ":"
                    + vo.getPort() + "/"
                    + vo.getDbName();
            connectDB.username = vo.getUsername();
            connectDB.pwd = vo.getPassword();
        }
        return connectDB;
    }

    public static DatasourceVO getBySysConfig(SysConfigVo vo) {
        DatasourceVO vo1 = new DatasourceVO();
        if (vo != null) {
            vo1.setName(vo.getDbName());
            vo1.setDbName(vo.getDbName());
            vo1.setDbType(Integer.parseInt(vo.getDbType()));
            vo1.setHost(vo.getIpAddress());
            vo1.setUsername(vo.getUsername());
            vo1.setPassword(vo.getPassword());
            vo1.setPort(Integer.parseInt(vo.getPort()));
            vo1.setTableSpaces(vo.getTableSpaces());
        }
        return vo1;
    }
}
