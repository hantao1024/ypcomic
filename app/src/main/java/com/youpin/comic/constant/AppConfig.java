package com.youpin.comic.constant;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

/**
 * app配置
 *
 * @author hantao
 */
public final class AppConfig {

    /**
     * (调试|正式)版本(正式为true)
     */
    public static boolean RELEASE = false;

    /**
     * 添加Bridge广告
     */
    public static boolean HAS_BRIDGE_AD = true;

    /**
     * 当前应用版本号
     */
    public static String APP_VERSION(Context context) {
        PackageInfo pinfo = null;
        try {
            pinfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return pinfo == null ? "1.0.001" : pinfo.versionName;
    }

    public static final String API_NEW ="https://v2api.dmzj.com/article/category.json";
    public static final String API_BASE ="http://zeke.api.upmanhua.com/";

    public static final String API_BASE_TWO ="http://api.upmanhua.com:8080/";

    public static final String API_LOGIN =API_BASE+"v1/login";//手机号登录
    public static final String API_CAPTCHA =API_BASE+"v1/login/captcha/";//获取验证码
    public static final String API_REGISTER =API_BASE+"v1/register";//手机号注册
    public static final String API_RESETPWD =API_BASE+"v1/resetpwd";//找回密码
    public static final String API_LOGOUT =API_BASE+"v1/logout?";//退出登录
    public static final String API_LOGIN_QQ =API_BASE+"v1/login/qq";//qq登录
    public static final String API_LOGIN_WEIBO =API_BASE+"v1/login/weibo";//weibo登录
    public static final String API_LOGIN_WECHAT =API_BASE+"v1/login/wechat";//wechat登录
}
