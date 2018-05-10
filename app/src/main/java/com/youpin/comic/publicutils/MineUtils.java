package com.youpin.comic.publicutils;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by hantao on 2017/10/17.
 */

public class MineUtils {
//    /**
//     * 判断手机号是否符合规范
//     *
//     * @param phoneNo 输入的手机号
//     * @return
//     */
//    public static boolean isPhoneNumber(String phoneNo) {
//        if (TextUtils.isEmpty(phoneNo)) {
//            return false;
//        }
//        try {
//            if (phoneNo.length() == 11) {
//                for (int i = 0; i < 11; i++) {
//                    if (!PhoneNumberUtils.isISODigit(phoneNo.charAt(i))) {
//                        return false;
//                    }
//                }
//                Pattern p = Pattern.compile("^((13[^4,\\D])" + "|(134[^9,\\D])" +
//                        "|(14[5,7])" +
//                        "|(15[^4,\\D])" +
//                        "|(17[3,6-8])" +
//                        "|(18[0-9]))\\d{8}$");
//                Matcher m = p.matcher(phoneNo);
//                return m.matches();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return false;
//    }
    /**
     * 判断密码是否规范
     *
     * @param pwd 输入的密码
     * @return
     */
    public static boolean isPwdIsNumber(String pwd) {
        if (TextUtils.isEmpty(pwd)) {
            return true;
        }
        if (pwd.length()<6) {
            return true;
        }
        Pattern p = Pattern.compile("[0-9]*");
        Matcher m = p.matcher(pwd);
        if(m.matches()){
           return true;
        }
        return false;
    }

    // 判断email格式是否正确
    public static boolean isEmail(String email) {
        if (TextUtils.isEmpty(email)) {
            return false;
        }
        try {
            String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
            Pattern p = Pattern.compile(str);
            Matcher m = p.matcher(email);
            return m.matches();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public static String isShowOrHide(String telStr){
        String tel="";
        try {
            if(!TextUtils.isEmpty(telStr) && telStr.length() > 6 ){
                StringBuilder sb  =new StringBuilder();
                for (int i = 0; i < telStr.length(); i++) {
                    char c = telStr.charAt(i);
                    if (i >= 3 && i <= 8) {
                        sb.append('*');
                    } else {
                        sb.append(c);
                    }
                }
                tel=sb.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tel;
    }

    /**
     * 判断用户名是否规范
     *
     * @param name 输入的用户名
     * @return
     */
    public static boolean isNameIsNumber(String name) {
        if (TextUtils.isEmpty(name)) {
            return true;
        }
        Pattern p = Pattern.compile("[0-9]*");
        Matcher m = p.matcher(name);
        if(m.matches()){
            return true;
        }
        return false;
    }

    /**
     * 大陆号码或香港号码均可
     */
    public static boolean isPhoneNumber(String str){
        if (TextUtils.isEmpty(str)||str.length() != 11) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 大陆手机号码11位数，匹配格式：前三位固定格式+后8位任意数
     * 此方法中前三位格式有：
     * 13+任意数
     * 15+除4的任意数
     * 18+除1和4的任意数
     * 17+除9的任意数
     * 147
     */
    public static boolean isChinaPhoneLegal(String str){
        try {
            String regExp = "^((13[0-9])|(15[^4])|(18[0,2,3,5-9])|(17[0-8])|(147))\\d{8}$";
            Pattern p = Pattern.compile(regExp);
            Matcher m = p.matcher(str);
            return m.matches();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 香港手机号码8位数，5|6|8|9开头+7位任意数
     */
    public static boolean isHKPhoneLegal(String str){
        try {
            String regExp = "^(5|6|8|9)\\d{7}$";
            Pattern p = Pattern.compile(regExp);
            Matcher m = p.matcher(str);
            return m.matches();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
