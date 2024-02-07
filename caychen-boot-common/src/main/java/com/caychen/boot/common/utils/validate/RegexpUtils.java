package com.caychen.boot.common.utils.validate;

import lombok.extern.slf4j.Slf4j;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public final class RegexpUtils {

    private RegexpUtils() {
    }

    /**
     * 匹配图象
     * <p>
     * <p>
     * 格式: /相对路径/文件名.后缀 (后缀为gif,dmp,png)
     * <p>
     * 匹配 : /forum/head_icon/admini2005111_ff.gif 或 admini2005111.dmp
     * <p>
     * <p>
     * 不匹配: c:/admins4512.gif
     */
    public static final String ICON_REGEXP = "^(/{0,1}//w){1,}//.(gif|dmp|png|jpg)$|^//w{1,}//.(gif|dmp|png|jpg)$";


    /**
     * 匹配正整数或小数
     * <p>
     * <p>
     * 格式: XXX.XXX或XXX
     * <p>
     */
    public static final String INTEGE_FLOAT = "[0-9]+([.]{1}[0-9]+){0,1}";

    /**
     * 匹配email地址
     * <p>
     * <p>
     * 格式: XXX@XXX.XXX.XX
     * <p>
     * 匹配 : foo@bar.com 或 foobar@foobar.com.au
     * <p>
     * 不匹配: foo@bar 或 $$$@bar.com
     */
    public static final String MAPBAR_EMAIL_REGEXP =
            "^([a-zA-Z0-9]+[_|\\-|\\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\\-|\\.]?)*[a-zA-Z0-9]+\\.[a-zA-Z]{2,3}$";

    /**
     * 匹配email地址
     * <p>
     * <p>
     * 格式: XXX@XXX.XXX.XX
     * <p>
     * 匹配 : foo@bar.com 或 foobar@foobar.com.au
     * <p>
     * 不匹配: foo@bar 或 $$$@bar.com
     */
    public static final String EMAIL_REGEXP = "(?://w[-._//w]*//w@//w[-._//w]*//w//.//w{2,3}$)";

    /**
     * 匹配并提取url
     * <p>
     * <p>
     * 格式: XXXX://XXX.XXX.XXX.XX/XXX.XXX?XXX=XXX
     * <p>
     * 匹配 : http://www.suncer.com 或news://www
     * <p>
     * 不匹配: c:/window
     */
    public static final String URL_REGEXP = "(//w+)://([^/:]+)(://d*)?([^#//s]*)";

    /**
     * 匹配并提取http
     * <p>
     * 格式: http://XXX.XXX.XXX.XX/XXX.XXX?XXX=XXX 或 ftp://XXX.XXX.XXX 或 https://XXX
     * <p>
     * 匹配 : http://www.suncer.com:8080/index.html?login=true
     * <p>
     * 不匹配: news://www
     */
    public static final String HTTP_REGEXP = "(http|https|ftp)://([^/:]+)(://d*)?([^#//s]*)";

    /**
     * 匹配并提取http
     * <p>
     * 格式: http://XXX.XXX.XXX.XX/XXX.XXX?XXX=XXX
     * <p>
     * 匹配 : http://www.suncer.com:8080/index.html?login=true
     * <p>
     * 不匹配: news://www
     */
    public static final String HTTP_REGEXP_ONLY = "(http://)([0-9a-z_!~*'()-]+\\.)*([a-zA-Z0-9][-a-zA-Z0-9]{0,62})+(\\.[a-zA-Z0-9]{1,4})(:[0-9]{1,4})?((/?)|(/[0-9a-zA-Z_!~*'().;?:@&=+$,%#-]+)+/?)\\.?$";

    /**
     * 匹配并提取http
     * <p>
     * 格式: http://XXX.XXX.XXX.XX/XXX.XXX?XXX=XXX 或 https://XXX.XXX.XXX.XX/XXX.XXX?XXX=XXX
     * <p>
     * 匹配 : http://www.suncer.com:8080/index.html?login=true
     * <p>
     * 不匹配: news://www
     */
    public static final String HTTP_AND_HTTPS_REGEXP = "((https|http)?://)([0-9a-z_!~*'()-]+\\.)*([a-zA-Z0-9][-a-zA-Z0-9]{0,62})+(\\.[a-zA-Z0-9]{1,4})(:[0-9]{1,4})?((/?)|(/[0-9a-zA-Z_!~*'().;?:@&=+$,%#-]+)+/?)\\.?$";

    /**
     * 匹配并提取http
     * <p>
     * 格式: http://XXX.XXX.XXX.XX/XXX.XXX?XXX=XXX 或 ftp://XXX.XXX.XXX 或 https://XXX
     * <p>
     * 匹配 : http://www.suncer.com:8080/index.html?login=true
     * <p>
     * 不匹配: news://www
     */
    public static final String HTTP_REGEXP_BANNER = "((https|http|ftp|rtsp|mms)?://)([0-9a-z_!~*'()-]+\\.)*([a-zA-Z0-9][-a-zA-Z0-9]{0,62})+(\\.[a-zA-Z0-9]{1,4})(:[0-9]{1,4})?((/?)|(/[0-9a-zA-Z_!~*'().;?:@&=+$,%#-]+)+/?)\\.?$";

    /**
     * 匹配日期
     * <p>
     * <p>
     * 格式(首位不为0): XXXX-XX-XX或 XXXX-X-X
     * <p>
     * <p>
     * 范围:1900--2099
     * <p>
     * <p>
     * 匹配 : 2005-04-04
     * <p>
     * <p>
     * 不匹配: 01-01-01
     */
    public static final String DATE_BARS_REGEXP = "^((((19){1}|(20){1})\\d{2})|\\d{2})-[0,1]?\\d{1}-[0-3]?\\d{1}$";

    /**
     * wzw
     * <p>
     * <p>
     * 匹配格式 20160606
     */
    public static final String DATE_BARS_DAY_REGEXP = "^((((19){1}|(20){1})\\d{2})|\\d{2})[0,1]?\\d{1}[0-3]?\\d{1}$";

    /**
     * 匹配格式 20130101
     */
    public static final String DATE_BARS_REGEXP_SIMPLE =
            "([\\d]{4}(((0[13578]|1[02])((0[1-9])|([12][0-9])|(3[01])))|(((0[469])|11)((0[1-9])|([12][0-9])|30))|(02((0[1-9])|(1[0-9])|(2[0-8])))))|((((([02468][048])|([13579][26]))00)|([0-9]{2}(([02468][048])|([13579][26]))))(((0[13578]|1[02])((0[1-9])|([12][0-9])|(3[01])))|(((0[469])|11)((0[1-9])|([12][0-9])|30))|(02((0[1-9])|(1[0-9])|(2[0-9])))))";

    /**
     * 匹配格式 20130101
     */
    public static final String DATE_BARS_REGEXP_HOUR_FULL =
            "^\\d{4}-(?:0\\d|1[0-2])-(?:[0-2]\\d|3[01])( (?:[01]\\d|2[0-3])\\:[0-5]\\d)?$";

    /**
     * 匹配日期
     * <p>
     * <p>
     * 格式(首位不为0): XXXX-XX-XX或 XXXX-X-X
     * <p>
     * <p>
     * 范围:1900--2099
     * <p>
     * <p>
     * 匹配 : 2005-04-04 11:00:00
     * <p>
     * <p>
     * 不匹配: 01-01-01
     */
    public static final String DATE_BARS_REGEXP_HOUR =
            "^((((19){1}|(20){1})\\d{2})|\\d{2})-[0,1]?\\d{1}-[0-3]?\\d{1}\\s[0,2]?\\d{1}[0,9]?\\d{1}$";

    /**
     * 匹配 : 2005-04-04 11:00:00
     */
    public static final String DATE_BARS_REGEXP_HOUR_MIN_SS =
            "^\\d{4}\\D+\\d{1,2}\\D+\\d{1,2}\\D+\\d{1,2}\\D+\\d{1,2}\\D+\\d{1,2}\\D*$";

    /**
     * 匹配日期
     * <p>
     * <p>
     * 格式: XXXX.XX.XX
     * <p>
     * <p>
     * 范围:
     * <p>
     * <p>
     * 匹配 : 2005.04.04
     * <p>
     * <p>
     * 不匹配: 01.01.01
     */
    // public static final String DATE_SLASH_REGEXP = "("
    // + "(^\\d{3}[1-9]|\\d{2}[1-9]\\d{1}|\\d{1}[1-9]\\d{2}|[1-9]\\d{3}" + "([.]?)" + "(10|12|0?[13578])" + "([.]?)"
    // + "((3[01]|[12][0-9]|0?[1-9])?)" + "([\\s]?)" + "((([0-1]?[0-9]|2[0-3]):([0-5]?[0-9]):([0-5]?[0-9]))?))$" + "|"
    // + "(^\\d{3}[1-9]|\\d{2}[1-9]\\d{1}|\\d{1}[1-9]\\d{2}|[1-9]\\d{3}" + "([.]?)" + "(11|0?[469])" + "([.]?)"
    // + "(30|[12][0-9]|0?[1-9])" + "([\\s]?)" + "((([0-1]?[0-9]|2[0-3]):([0-5]?[0-9]):([0-5]?[0-9]))?))$" + "|"
    // + "(^\\d{3}[1-9]|\\d{2}[1-9]\\d{1}|\\d{1}[1-9]\\d{2}|[1-9]\\d{3}" + "([.]?)" + "(0?2)" + "([.]?)"
    // + "(2[0-8]|1[0-9]|0?[1-9])" + "([\\s]?)" + "((([0-1]?[0-9]|2[0-3]):([0-5]?[0-9]):([0-5]?[0-9]))?))$" + "|"
    // + "(^((\\d{2})(0[48]|[2468][048]|[13579][26]))|((0[48]|[2468][048]|[13579][26])00)" + "([.]?)" + "(0?2)"
    // + "([.]?)" + "(29)" + "([\\s]?)" + "((([0-1]?\\d|2[0-3]):([0-5]?\\d):([0-5]?\\d))?))$" + ")";
    public static final String DATE_SLASH_REGEXP = "^[0-9]{4}\\.[0-9]{2}\\.[0-9]{2}$";

    /**
     * 匹配电话
     * <p>
     * <p>
     * 格式为: 0XXX-XXXXXX(10-13位首位必须为0) 或0XXX XXXXXXX(10-13位首位必须为0) 或
     * <p>
     * (0XXX)XXXXXXXX(11-14位首位必须为0) 或 XXXXXXXX(6-8位首位不为0) 或 XXXXXXXXXXX(11位首位不为0)
     * <p>
     * <p>
     * 匹配 : 0371-123456 或 (0371)1234567 或 (0371)12345678 或 010-123456 或 010-12345678 或 12345678912
     * <p>
     * <p>
     * 不匹配: 1111-134355 或 0123456789
     */
    public static final String PHONE_REGEXP =
            "^(?:0[0-9]{2,3}[-//s]{1}|//(0[0-9]{2,4}//))[0-9]{6,8}$|^[1-9]{1}[0-9]{5,7}$|^[1-9]{1}[0-9]{10}$";

    /**
     * 11位手机号格式验证
     */
    public static final String MOBILE_PHONE_REGEXP = "^1\\d{10}$";

    /**
     * 20位联系方式格式验证
     */
    public static final String PHONE_20_REGEXP = "^[0-9\\-]{1,20}$";

    /**
     * 匹配身份证
     * <p>
     * 格式为: XXXXXXXXXX(10位) 或 XXXXXXXXXXXXX(13位) 或 XXXXXXXXXXXXXXX(15位) 或 XXXXXXXXXXXXXXXXXX(18位)
     * <p>
     * 匹配 : 0123456789123
     * <p>
     * 不匹配: 0123456
     */
    public static final String IDENTITY_CARD_REGEXP =
            "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X)$";

    /**
     * 匹配身份证
     * <p>
     * 格式为: XXXXXXXXXX(10位) 或 XXXXXXXXXXXXX(13位) 或 XXXXXXXXXXXXXXX(15位) 或 XXXXXXXXXXXXXXXXXX(18位)
     * <p>
     * 匹配 : 0123456789123
     * <p>
     * 不匹配: 0123456
     */
    public static final String ID_CARD_REGEXP = "^d{18} | $";

    /**
     * 匹配邮编代码
     * <p>
     * 格式为: XXXXXX(6位)
     * <p>
     * 匹配 : 012345
     * <p>
     * 不匹配: 0123456
     */
    public static final String ZIP_REGEXP = "^[0-9]{6}$";// 匹配邮编代码

    /**
     * 不包括特殊字符的匹配 (字符串中不包括符号 数学次方号^ 单引号' 双引号" 分号; 逗号, 帽号: 数学减号- 右尖括号> 左尖括号< 反斜杠/ 即空格,制表符,回车符等 )
     * <p>
     * 格式为: x 或 一个一上的字符
     * <p>
     * 匹配 : 012345
     * <p>
     * 不匹配: 0123456 // ;,:-<>//s].+$";//
     */
    public static final String NON_SPECIAL_CHAR_REGEXP = "^[^'/";

    // 匹配邮编代码
    /**
     * 匹配一位数的0-1整数
     */
    public static final String ZERO__TO_ONE_NUMBER = "[0-1]$";

    /**
     * 匹配一位数的0-2整数
     */
    public static final String ZERO__TO_TWO_NUMBER = "[0-2]$";

    /**
     * 匹配一位数的0-2整数
     */
    public static final String ZERO__TO_THREE_NUMBER = "[0-3]$";

    /**
     * 匹配一位数的1-4的非负整数
     */
    public static final String ONE__TO_FOUR_NATURAL_NUMBER = "[1-4]$";

    /**
     * 匹配一位数的0-4的非负整数
     */
    public static final String ZERO__TO_FOUR_NATURAL_NUMBER = "^[0-4]$";

    /**
     * 匹配一位数的0-6的非负整数
     */
    public static final String ZERO__TO_SIX_NATURAL_NUMBER = "^[0-6]$";

    /**
     * 匹配一位数的1-8的非负整数
     */
    public static final String ONE_TO_EIGHT_NATURAL_NUMBER = "[1-8]$";

    /**
     * 匹配一位数的1-3的非负整数
     */
    public static final String ONE_TO_THREE_NATURAL_NUMBER = "[1-3]$";

    /**
     * 匹配一位数的1-2的非负整数
     */
    public static final String ONE__TO_TWO_NATURAL_NUMBER = "[1-2]$";
    /**
     * 匹配一位数的1-3的非负整数
     */
    public static final String ONE__TO_THERE_NATURAL_NUMBER = "[1-3]$";

    /**
     * 匹配三位数的正整数
     */
    public static final String TRHEE__TO_NATURAL_NUMBER = "^(\\d{1,3})?$";

    /**
     * 匹配数字
     */
    public static final String NUMBER_CHECK = "^[0-9]*$";

    /**
     * 匹配一位数的1-5的正整数
     */
    public static final String ONE__TO_FIVE_NATURAL_NUMBER = "^[1-5]$";

    /**
     * 匹配一位数的1-6的非负整数
     */
    public static final String ONE__TO_SIX_NATURAL_NUMBER = "^[1-6]$";

    /**
     * 匹配0-18的正整数
     */
    public static final String ONE_TO_EIGHTEEN_NUMBER = "^(18|[0-9]|(1[0-8]))$";

    /**
     * 匹配非负整数（正整数 + 0)
     */
    public static final String NON_NEGATIVE_INTEGERS_REGEXP = "^//d+$";

    /**
     * 匹配带1位小数且不超过2位的数
     */
    public static final String CAR_LENGTH_REGEXP = "^(\\d|\\d\\d)(\\.\\d)?$";

    /**
     * 匹配整数位最大3位且小数位最大2位的数
     */
    public static final String CAR_LOAD_REGEXP = "^(\\d{1,3})(\\.\\d{1,2})?$";

    /**
     * 匹配不包括零的非负整数（正整数 > 0)
     */
    public static final String NON_ZERO_NEGATIVE_INTEGERS_REGEXP = "^[1-9]\\d*$";


    /**
     * 匹配零或正整数（正整数 > 0)
     */
    public static final String ZERO_OR_POSITIVE_INTEGER_REGEXP = "^(0|[1-9]\\d*)$";

    /**
     * 匹配包括零的正整数
     */
    public static final String ZERO_NEGATIVE_INTEGERS_REGEXP = "^[0-9]*[0-9][0-9]*$";

    /**
     * 匹配正整数
     */
    public static final String POSITIVE_INTEGER_REGEXP = "^[0-9]*[1-9][0-9]*$";

    /**
     * 匹配非正整数（负整数 + 0）
     */
    public static final String NON_POSITIVE_INTEGERS_REGEXP = "^((-//d+)|(0+))$";

    /**
     * 匹配负整数
     */
    public static final String NEGATIVE_INTEGERS_REGEXP = "^-[0-9]*[1-9][0-9]*$";

    /**
     * 匹配整数
     */
    public static final String INTEGER_REGEXP = "^-?[0-9]\\d*$";

    /**
     * 匹配非负浮点数（正浮点数 + 0）
     */
    public static final String NON_NEGATIVE_RATIONAL_NUMBERS_REGEXP = "^//d+(//.//d+)?$";

    /**
     * 匹配正浮点数
     */
    public static final String POSITIVE_RATIONAL_NUMBERS_REGEXP = "^[0-9]+(.[0-9]{1})?$";

    /**
     * 匹配正浮点数(1-6位)
     */
    public static final String POSITIVE_ONE_TO_SIX_RATIONAL_NUMBERS_REGEXP = "^((?!0\\d)\\d+(\\.\\d{1,6}?))$";

    // "^([1-9]\\d*|0)\\.(\\d*[1-9]|0)$";

    /**
     * 匹配非正浮点数（负浮点数 + 0）
     */
    public static final String NON_POSITIVE_RATIONAL_NUMBERS_REGEXP = "^((-//d+(//.//d+)?)|(0+(//.0+)?))$";

    /**
     * 匹配负浮点数
     */
    public static final String NEGATIVE_RATIONAL_NUMBERS_REGEXP =
            "^(-(([0-9]+//.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*//.[0-9]+)|([0-9]*[1-9][0-9]*)))$";

    /**
     * 匹配浮点数
     */
    public static final String RATIONAL_NUMBERS_REGEXP = "^(-?//d+)(//.//d+)?$";

    /**
     * 匹配大于等于0小于1,000的1位小数（浮点数）
     * 匹配：0 / 999 / 0.1 / 999.1 / 10.0
     * 不匹配：1000 / 1000.2 / 00 / 011 / 0.12 / 1000.12 / 10.20 / -1.1
     */
    public static final String NON_NEGATIVE_DECIMAL_LESS_THAN_1K = "^(([1-9]\\d{0,2})|(0))(\\.(\\d){0,1})?$";

    /**
     * 匹配大于等于0小于100,000的1位小数（浮点数）
     * 匹配：0 / 99999 / 0.1 / 99999.1 / 10.0
     * 不匹配：100000 / 100000.2 / 00 / 011 / 0.12 / 100000.12 / 10.20 / -1.1
     */
    public static final String NON_NEGATIVE_DECIMAL_LESS_THAN_100K = "^(([1-9]\\d{0,4})|(0))(\\.(\\d){0,1})?$";

    /**
     * 匹配大于等于0小于10,000,000,000的1位小数（浮点数）
     * 匹配：0 / 9999999999 / 0.1 / 9999999999.1 / 10.0
     * 不匹配：10000000000 / 10000000000.2 / 00 / 011 / 0.12 / 100000.12 / 10.20 / -1.1
     */
    public static final String NON_NEGATIVE_DECIMAL_LESS_THAN_10B = "^(([1-9]\\d{0,9})|(0))(\\.(\\d){0,1})?$";

    /**
     * 匹配特定数字2和7
     */
    public static final String MATCHING_SPECIFIC_NUMBERS_REGEXP = "2|7";

    /**
     * 匹配由26个英文字母组成的字符串
     */
    public static final String LETTER_REGEXP = "^[A-Za-z]+$";

    /**
     * 匹配由26个英文字母的大写组成的字符串
     */
    public static final String UPWARD_LETTER_REGEXP = "^[A-Z]+$";

    /**
     * 匹配A到D大写字母
     */
    public static final String UPWARD_LETTER_AD = "^[A-D]+$";


    /**
     * 匹配由26个英文字母的大写的字符
     */
    public static final String UPWARD_LETTER_STR = "^[A-Z]{1}$";

    /**
     * 匹配由26个英文字母的小写组成的字符串
     */
    public static final String LOWER_LETTER_REGEXP = "^[a-z]+$";

    /**
     * 匹配由数字和26个英文字母组成的字符串
     */
    public static final String LETTER_NUMBER_REGEXP = "^[A-Za-z0-9]+$";

    /**
     * 匹配由数字、26个英文字母或者下划线组成的字符串
     */
    public static final String LETTER_NUMBER_UNDERLINE_REGEXP = "^//w+$";

    /**
     * 必须由数字和26个英文字母组合组成的字符串
     */
    public static final String LETTER_NUMBER_REGEXP_MUST = "[0-9]+[a-zA-Z]+[0-9a-zA-Z]*|[a-zA-Z]+[0-9]+[0-9a-zA-Z]*";

    /**
     * 匹配5位数字
     */
    public static final String FIVE_NUMBER_ONLY_REGEXP = "^[0-9]{5}$";

    /**
     * 匹配1位字母
     */
    public static final String ONE_LETTER = "^[a-zA-Z]{1}$";

    /**
     * 匹配车牌号后5位
     */
    public static final String CAR_NUMBER_FIVE = "^[0-9a-zA-Z]{5}$";

    /**
     * 匹配底盘号8位
     */
    public static final String EIGHT_CHASSIS_NUMBER = "^[0-9a-zA-Z]{8}$";

    /**
     * 匹配底盘号8位或17位
     */
    public static final String EIGHT_OR_SEVENTEEN_CHASSIS_NUMBER = "^([0-9a-zA-Z]{8}|[0-9a-zA-Z]{17})$";

    /**
     * 匹配6-20位用户名
     */
    public static final String USER_NAME_LENGTH = "^[0-9a-zA-Z]{6,20}$";

    /**
     * 匹配1-100位红包活动名
     */
    public static final String RED_PACKET_NAME_LENGTH = "^[\\u4e00-\\u9fa5]{1,100}$";
    /**
     * 匹配1-100位红包活动说明
     */
    public static final String RED_PACKET_OTHER_LENGTH = "^[\\u4e00-\\u9fa5]{1,200}$";

    /**
     * 匹配VIN17位
     */
    public static final String SEVENTEEN_VIN = "^[0-9a-zA-Z]{17}$";

    /**
     * 匹配19位数字
     */
    public static final String NINETEEN_NUMBER_ONLY_REGEXP = "^[0-9]{19}$";

    /**
     * 匹配19位数字
     */
    public static final String TWENTY_NUMBER_ONLY_REGEXP = "^[0-9]{20}$";

    /**
     * 匹配6位数字
     */
    public static final String REGION_CODE_ONLY_REGEXP = "^[0-9]{6}$";

    /**
     * 匹配1位数字
     */
    public static final String NUMBER_ONLY_ONE_REGEXP = "^\\d$";

    /**
     * 匹配1或2或3
     */
    public static final String NUMBER_ONLY_ONE_TO_THREE_REGEXP = "^[1]$";

    /**
     * 匹配1或2
     */
    public static final String NUMBER_ONLY_ONE_TO_TWO_REGEXP = "^[1-2]$";
    /**
     * 匹配0或1
     */
    public static final String NUMBER_ONLY_ZERO_ONE_REGEXP = "^[0-1]$";

    /**
     * 匹配0或1或2或3
     */
    public static final String NUMBER_ONLY_ZERO_TWO_REGEXP = "^[0-3]$";

    /**
     * 匹配2或3
     */
    public static final String NUMBER_ONLY_TWO_AND_THREE_REGEXP = "^[2-3]$";

    /**
     * 匹配10
     */
    public static final String NUMBER_ONLY_TEN_REGEXP = "^[10]$";

    /**
     * 匹配0到9
     */
    public static final String NUMBER_ONLY_ONE_SIX_THREE_REGEXP = "^[0-9]$";

    /**
     * 匹配车牌号
     */
    public static final String CAR_NUMBER = "^[\\u4e00-\\u9fa5]{1}[a-zA-Z]{1}[a-zA-Z0-9\\u4e00-\\u9fa5]{5,6}$";
    /**
     * 匹配日期
     * <p>
     * <p>
     * 格式: yyyymmdd
     * <p>
     * <p>
     * 范围:
     * <p>
     * <p>
     * 匹配 : 20050404
     * <p>
     * <p>
     * 不匹配: 010101
     */
    public static final String DATE_YYYYMMMDD_REGEXP = "("
            + "(^\\d{3}[1-9]|\\d{2}[1-9]\\d{1}|\\d{1}[1-9]\\d{2}|[1-9]\\d{3}" + "(10|12|0?[13578])"
            + "((3[01]|[12][0-9]|0?[1-9])?)" + "([\\s]?)" + "((([0-1]?[0-9]|2[0-3]):([0-5]?[0-9]):([0-5]?[0-9]))?))$" + "|"
            + "(^\\d{3}[1-9]|\\d{2}[1-9]\\d{1}|\\d{1}[1-9]\\d{2}|[1-9]\\d{3}" + "(11|0?[469])" + "(30|[12][0-9]|0?[1-9])"
            + "([\\s]?)" + "((([0-1]?[0-9]|2[0-3]):([0-5]?[0-9]):([0-5]?[0-9]))?))$" + "|"
            + "(^\\d{3}[1-9]|\\d{2}[1-9]\\d{1}|\\d{1}[1-9]\\d{2}|[1-9]\\d{3}" + "(0?2)" + "(2[0-8]|1[0-9]|0?[1-9])"
            + "([\\s]?)" + "((([0-1]?[0-9]|2[0-3]):([0-5]?[0-9]):([0-5]?[0-9]))?))$" + "|"
            + "(^((\\d{2})(0[48]|[2468][048]|[13579][26]))|((0[48]|[2468][048]|[13579][26])00)" + "(0?2)" + "(29)"
            + "([\\s]?)" + "((([0-1]?\\d|2[0-3]):([0-5]?\\d):([0-5]?\\d))?))$" + ")";

    /**
     * 匹配日期 格式: yyyymm 匹配 : 200504 不匹配: 010101
     */
    public static final String DATE_YYYYMM_REGEXP = "^(\\d{4})(0\\d{1}|1[0-2])$";

    /**
     * 匹配日期 格式: yyyy-MM-dd 10位 匹配 : 2005-04-01 不匹配: 010101
     */
    public static final String DATE_YYYY_MM_DD_REGEXP = "^((?:19|20)\\d\\d)-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$";

    /**
     * 匹配日期 格式: yyyy-MM 10位 匹配 : 2005-04 不匹配: 010101
     */
    public static final String DATE_YYYY_MM_REGEXP = "^((?:19|20)\\d\\d)-(0[1-9]|1[012])$";

    /**
     * 匹配日期 格式: yyyy/MM/dd 10位 匹配 : 2005/04/01 不匹配: 010101
     */
    public static final String DATE_YYYYMMDD_REGEXP = "^((?:19|20)\\d\\d)/(0[1-9]|1[012])/(0[1-9]|[12][0-9]|3[01])$";

    /**
     * 匹配日期 格式: yyyyMMdd 8位 匹配 : 20050401 不匹配: 010101
     */
    public static final String DATE_YYYYMMDD_EIGHT_REGEXP =
            "^((?:19|20)\\d\\d)(0[1-9]|1[012])(0[1-9]|[12][0-9]|3[01])$";

    /**
     * 匹配日期 格式: yyyyMMdd 8位 匹配 : 20050401 不匹配: 010101
     */
    public static final String DATE_YYYYMMDD_HHMM_REGEXP =
            "^[1-2][0-9][0-9][0-9]-([1][0-2]|0?[1-9])-([12][0-9]|3[01]|0?[1-9]) ([01][0-9]|[2][0-3]):[0-5][0-9]$";
    /**
     * 匹配格式 2016-02-02 01:01
     */
    public static final String DATE_BARS_REGEXP_HOUR_MIN =
            "^\\d{4}-(?:0\\d|1[0-2])-(?:[0-2]\\d|3[01]) (?:[01]\\d|2[0-3])\\:[0-5]\\d?$";
    /**
     * 匹配格式 2016-02-02 01:01:01
     */
    public static final String DATE_BARS_REGEXP_HOUR_MIN_SECOND =
            "^\\d{4}[-]([0][1-9]|(1[0-2]))[-]([1-9]|([012]\\d)|(3[01]))([ \\t\\n\\x0B\\f\\r])(([0-1]{1}[0-9]{1})|([2]{1}[0-4]{1}))([:])(([0-5]{1}[0-9]{1}|[6]{1}[0]{1}))([:])((([0-5]{1}[0-9]{1}|[6]{1}[0]{1})))$";

    /**
     * 匹配整数或者空串
     */
    public static final String NUMBER_BLANK_REGEXP = "^[1-9]\\d*|0|^\\s*$";

    /**
     * 匹配正整数或者空串
     */
    public static final String POSITIVE_NUMBER_BLANK_REGEXP = "^[1-9]\\d*|^\\s*$";

    /**
     * 匹配精度正则
     */
    public static final String LON_REGEXP = "^(-?((180)|(((1[0-7]\\d)|(\\d{1,2}))(\\.\\d+)?)))$";

    /**
     * 匹纬度正则
     */
    public static final String LAT_REGEXP = "^(-?((90)|((([0-8]\\d)|(\\d{1}))(\\.\\d+)?)))$";

    /**
     * 匹配SN码：产品代码+方案商代码+工厂代码（前三位为0-9任意数字），年（第四位为A-F或4-9），月（第5位和第六位为：01-12），流水（第7位到第11位为0-9），验证（第12-13位为00-FF）
     */

    public static final String SN_CODE_REGEXP =
            "^[0-9]{3}[A-F4-9]{1}(0[1-9]{1}|1[0-2]{1})[0-9]{5}([0-9]|[a-f]|[A-F]){1}([0-9]|[a-f]|[A-F]){1}$";

    /**
     * 任意位数的非负整数
     */
    public static final String NATURAL_NUMBER = "\\d*$";

    /**
     * 数字加逗号
     */
    public static final String COMMA_NUMBER = "[\\d,]*$";

    /**
     * 匹配汉字
     */
    public static final String CHINESE_VAR = "[\\u4e00-\\u9fa5]*$";

    /**
     * 匹配包含字母等字符串
     */
    public static final String CHINESE_LETTER = ".*[a-zA-Z]+.*";


    /**
     * 匹配管理员账户
     */
    public static final String TBOSS_MANAGER_ACCOUNT = "[a-zA-Z]{1}[a-zA-Z0-9_]{4,20}";


    /**
     * 匹配管理员名称
     */
    public static final String TBOSS_MANAGER_NAME = " [^(a-zA-Z0-9\\u4e00-\\u9fa5)]{0,20}";

    /**
     * 匹配管理员密码
     */
    public static final String TBOSS_MANAGER_PASSWORD = "[a-zA-Z0-9]{6,20}";

    /**
     * 车队web密码
     */
    public static final String TEAMWEB_RESET_PASSWORD = "^[\\x21-\\x7e]{6,20}$";

    /**
     * 车队名称校验
     */
    public static final String TEAMNAME = "^[_\\-a-zA-Z0-9\\u4e00-\\u9fa5]{1,14}$";
    /**
     * 司机姓名校验
     */
    public static final String DRIVERNAME = "^[a-zA-Z0-9\\u4e00-\\u9fa5]{1,16}$";
    /**
     * 用户姓名校验
     */
    public static final String USERNAME = "^[a-zA-Z\\u4e00-\\u9fa5]{1,20}$";

    /**
     * 服务站周边车辆范围校验
     */
    public static final String STATION_RANGE = "^5|10|50$";

    public static final String SIX_DECIMA = "^\\d+(\\.\\d{6})?$";

    /**
     * 5位数字或字母用逗号分隔 故障标识码
     */
    public static final String FIVE_STRING_SEPARATE_COMMA = "^0|[0-9a-zA-Z]{5}((,|，)[0-9a-zA-Z]{5})*$";

    /**
     * yyyyMMddHHmmss 日期正则表达式
     */
    public static final String DATE_ST_PATTERN_YYYYMMDDHHMMSS = "^((([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})(((0[13578]|1[02])(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)(0[1-9]|[12][0-9]|30))|(02(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))0229))([0-1]?[0-9]|2[0-3])([0-5][0-9])([0-5][0-9])$";

    /**
     * 字母数字组合校验，长度22位
     * (?!^\d+$) 排除全数字
     * (?!^[a-zA-Z]+$) 排除全字母
     * [0-9a-zA-Z]{23} 字符或字母23位
     */
    public static final String REPAIR_NO = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{22}$";

    /**
     * 匹配 10位时间戳: 示例值(1576833877)
     */
    public static final String TIME_STAMP_REGEXP_TEN = "^\\d{10}$";

    /**
     * 校验正则表达式结果
     *
     * @param content
     * @param reg
     * @return
     */
    public static boolean validateInfo(String content, String reg) {

        Pattern pat = Pattern.compile(reg);
        Matcher mat = pat.matcher(content);

        return mat.matches();
    }
}