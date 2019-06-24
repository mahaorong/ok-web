package com.geek.okweb.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * 正则校验数据工具类
 */
@Slf4j
public class RegularUtil {

    public static String notNullRegular = "[^\\s]{1,}"; //非空正则

    public static String emailRegular = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$"; //邮箱校验正则

    public static String telephoneRegular = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[0135678])|(18[0,5-9]))\\d{8}$|^\\d{8}$|^\\d{5}$"; //手机号校验

    public static String urlRegular = "^(?=^.{3,255}$)(http(s)?:\\/\\/)?(www\\.)?[a-zA-Z0-9][-a-zA-Z0-9]{0,62}(\\.[a-zA-Z0-9][-a-zA-Z0-9]{0,62})+(:\\d+)*(\\/\\w+\\.\\w+)*$";

    public static String dataRegular = "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))";

    public static String postalRegular = "[1-9]\\d{5}";

    public static String numberRegular = "[0-9]{1,}";

    //正则表达式校验
    public static boolean getDataRegularVerify(String regular, String verifyData) {
        boolean verifyResult = false;
        if (StringUtils.isNotBlank(verifyData) && StringUtils.isNotBlank(regular)) {
            verifyResult = verifyData.matches(regular);
            return verifyResult;
        }
        return verifyResult;
    }

    /*public static String getLengthRegular(Integer index , Integer last){
        String lengthRegular = RegularUtil.lengthRegular;
        if (last != 0) {
            lengthRegular = "[\\u4e00-\\u9fa5_a-zA-Z0-9_]{" + index + "," + last + "}";
            return lengthRegular;
        }
        return lengthRegular;
    }*/

    /**
     * 正则表达式校验(单个正则，多个数据校验)
     * @param regulae 正则表达式
     * @param verifyDatas 传来的参数必须全部符合传来的正则表达式
     * @return
     */
    public static boolean getDataRegularVerify(String regulae, String... verifyDatas) {
        boolean verifyResult = false;
        if (StringUtils.isNotBlank(regulae) && verifyDatas.length > 0) {
            for (int i = 0; i < verifyDatas.length; i++) {
                String verifyData = verifyDatas[i];
                verifyResult = verifyData.matches(regulae);
                if (!verifyResult){
                    return verifyResult;
                }
            }
            return verifyResult;
        }
        return verifyResult;
    }

    /**
     * 正则表达式校验(单个数据，多个正则校验)
     * @param regulaes 正则表达式
     * @param verifyData 传来的单个参数必须全部符合传来的正则表达式
     * @return
     */
    public static boolean getDatasRegularVerify(String verifyData, String... regulaes) {
        boolean verifyResult = false;
        if (StringUtils.isNotBlank(verifyData) && regulaes.length > 0) {
            for (int i = 0; i < regulaes.length; i++) {
                String regulae = regulaes[i];
                verifyResult = verifyData.matches(regulae);
                if (!verifyResult){
                    return verifyResult;
                }
            }
            return verifyResult;
        }
        return verifyResult;
    }

    public static boolean getListDatasRegularVerify(String verifyData, List<String> regulaes){
        boolean verifyResult = true;
        if (regulaes.size() > 0) {
            for (int i = 0; i < regulaes.size(); i++) {
                String regulae = regulaes.get(i);
                verifyResult = verifyData.matches(regulae);
                if (!verifyResult){
                    return verifyResult;
                }
            }
        }
        return verifyResult;
    }

    public static String getMessDatasRegularVerify(String verifyData, List<String> regulaes){
        String message = "";
        if (regulaes.size() > 0) {
            for (int i = 0; i < regulaes.size(); i++) {
                String regulae = regulaes.get(i);
                boolean verifyResult = verifyData.matches(regulae);
                if (!verifyResult){
                    if (StringUtils.equals(regulae, RegularUtil.notNullRegular)) {
                        message = "数据不能为空";
                    } else if (StringUtils.equals(regulae, RegularUtil.telephoneRegular)) {
                        message = "电话号码格式不正确";
                    } else if (StringUtils.equals(regulae, RegularUtil.emailRegular)) {
                        message = "邮箱格式不正确";
                    } else if (StringUtils.equals(regulae, RegularUtil.urlRegular)) {
                        message = "网址格式不正确";
                    } else if (StringUtils.equals(regulae, RegularUtil.dataRegular)) {
                        message = "日期格式不正确";
                    } else if (StringUtils.equals(regulae, RegularUtil.postalRegular)) {
                        message = "邮政编码不正确";
                    } else if (StringUtils.equals(regulae, RegularUtil.numberRegular)) {
                        message = "数字格式不正确";
                    }
                    return message;
                }
            }
        }
        return message;
    }

    public static String getVerify(String regular){
        if (StringUtils.equals("0", regular)) {
            return notNullRegular;
        } else if (StringUtils.equals("1", regular)) {
            return telephoneRegular;
        } else if (StringUtils.equals("2", regular)) {
            return emailRegular;
        } else if (StringUtils.equals("3", regular)) {
            return urlRegular;
        } else if (StringUtils.equals("4", regular)) {
            return dataRegular;
        } else if (StringUtils.equals("5", regular)) {
            return postalRegular;
        } else if (StringUtils.equals("6", regular)) {
            return numberRegular;
        }
        return "";
    }

}
