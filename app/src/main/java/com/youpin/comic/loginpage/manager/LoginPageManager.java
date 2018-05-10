package com.youpin.comic.loginpage.manager;

import android.text.TextUtils;
import android.util.Log;

import com.youpin.comic.CApplication;
import com.youpin.comic.constant.AppConfig;
import com.youpin.comic.constant.URLPathMaker;
import com.youpin.comic.loginpage.bean.LoginUserBean;
import com.youpin.comic.loginpage.bean.RegisterBean;
import com.youpin.comic.loginpage.dao.LoginDao;
import com.youpin.comic.loginpage.event.CaptchaEvents;
import com.youpin.comic.loginpage.event.LoginOutEvents;
import com.youpin.comic.loginpage.event.LoginPageEvents;
import com.youpin.comic.loginpage.event.RegisterPageEvents;
import com.youpin.comic.publichttp.OkhttpHelper;
import com.youpin.comic.publicmanager.BaseManager;
import com.youpin.comic.publicutils.ObjectMaker;
import com.youpin.comic.publicutils.URLData;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

import okhttp3.Call;
import okhttp3.Response;

/**
 * 登录管理类
 * Created by hantao on 2018/2/7.
 */
public class LoginPageManager extends BaseManager {
    private static final String TAG = "LoginPageManager";
    private static LoginPageManager instance;


    public LoginPageManager() {
        instance = this;
    }

    public static LoginPageManager getInstance() {
        return instance;
    }

    private boolean isRelease(){
        return AppConfig.RELEASE;
    }
    /**
     * 登录
     * @param tel
     * @param pwd
     */
    public void doLoginPost(final String tel, final String pwd, final String mark) {
        doLoginPost(tel,pwd,null,mark,AppConfig.API_LOGIN);
    }
    /**
     * 找回密码
     * @param tel
     * @param pwd
     */
    public void doLoginPost(final String tel, final String pwd, final String captcha,final String mark) {
        doLoginPost(tel,pwd,captcha,mark,AppConfig.API_RESETPWD);
    }
    /**
     * 找回密码跟登录
     * @param tel
     * @param pwd
     */
    public void doLoginPost(final String tel, final String pwd, final String captcha, final String mark, final String url) {

        //构造请求参数
        final Map<String, String> reqBody = new ConcurrentSkipListMap<>();
        reqBody.put("phone", tel);
        reqBody.put("password", pwd);
        if (!TextUtils.isEmpty(captcha)) {
            reqBody.put("captcha", captcha);
        }

        CApplication.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                //获取网络请求工具类实例
                OkhttpHelper netUtils = OkhttpHelper.getInstance();
                //提交数据
                netUtils.postDataAsynToNet(url, reqBody, new OkhttpHelper.MyNetCall() {
                    @Override
                    public void success(Call call, Response response) throws IOException {
                        if (!isRelease()) {
                            Log.i(TAG, "success");
                        }
                        try {
                            String result = response.body().string();
                            JSONObject jb = new JSONObject(result);
                            int code=jb.optInt("code",-1);
                            String msg=jb.optString("msg");
                            if (code == 0) {
                                JSONObject jsonObject=jb.getJSONObject("data");
                                String api_token=jsonObject.optString("api_token");

                                JSONObject object=jsonObject.getJSONObject("user");
                                LoginUserBean loginUserBean= ObjectMaker.convert(object,LoginUserBean.class);
                                if (loginUserBean==null) {
                                    loginUserBean=new LoginUserBean();
                                }
                                loginUserBean.setApi_token(api_token);
                                loginUserBean.setStatus(LoginUserBean.STATUS_ONLINE);
                                if (TextUtils.isEmpty(captcha)) {
                                    LoginDao.deleteAll();
                                    LoginDao.insertUser(loginUserBean);
                                }
                                EventBus.getDefault().post(LoginPageEvents.pullSuccess(true,msg,loginUserBean,mark));
                            } else {
                                EventBus.getDefault().post(LoginPageEvents.pullFale(msg,mark));
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (Exception e){
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void failed(Call call, IOException e) {
                        if (!isRelease()) {
                            Log.i(TAG, "failed");
                        }
                        String msg=TextUtils.isEmpty(captcha)?"登录失败":"找回密码失败";
                        EventBus.getDefault().post(LoginPageEvents.pullFale(msg,mark));
                    }
                });
            }
        });
    }

    /**
     * 注册
     * @param tel
     * @param pwd
     */
    public void doRegisterPost(final String tel, final String pwd, final String captcha) {

        //构造请求参数
        final Map<String, String> reqBody = new ConcurrentSkipListMap<>();
        reqBody.put("phone", tel);
        reqBody.put("password", pwd);
        reqBody.put("captcha", captcha);

        CApplication.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                //获取网络请求工具类实例
                OkhttpHelper netUtils = OkhttpHelper.getInstance();
                //提交数据
                netUtils.postDataAsynToNet(AppConfig.API_REGISTER, reqBody, new OkhttpHelper.MyNetCall() {
                    @Override
                    public void success(Call call, Response response) throws IOException {
                        if (!isRelease()) {
                            Log.i(TAG, "success");
                        }
                        try {
                            String result = response.body().string();
                            JSONObject jb = new JSONObject(result);
                            int code=jb.optInt("code",-1);
                            String msg=jb.optString("msg");
                            JSONObject jsonObject=jb.getJSONObject("data");
                            String api_token=jsonObject.optString("api_token");

                            JSONObject object=jsonObject.getJSONObject("user");
                            RegisterBean registerBean= ObjectMaker.convert(object,RegisterBean.class);
                            if (code == 0) {
                                if (registerBean==null) {
                                    registerBean=new RegisterBean();
                                }
                                registerBean.setApi_token(api_token);
                                EventBus.getDefault().post(RegisterPageEvents.pullSuccess(true,msg,registerBean));
                            } else {
                                EventBus.getDefault().post(RegisterPageEvents.pullFale(msg));
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (Exception e){
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void failed(Call call, IOException e) {
                        if (!isRelease()) {
                            Log.i(TAG, "failed");
                        }
                        EventBus.getDefault().post(RegisterPageEvents.pullFale("注册失败"));
                    }
                });
            }
        });
    }


    /**
     * 获取验证码
     * @param tel
     * @param mark
     */
    public void doCaptcha(final String tel, final String mark) {
        CApplication.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                //获取网络请求工具类实例
                OkhttpHelper netUtils = OkhttpHelper.getInstance();
                //提交数据
                netUtils.postDataAsynToNet(URLPathMaker.get_url(AppConfig.API_CAPTCHA,tel), null, new OkhttpHelper.MyNetCall() {
                    @Override
                    public void success(Call call, Response response) throws IOException {
                        if (!isRelease()) {
                            Log.i(TAG, "success");
                        }
                        try {
                            String result = response.body().string();
                            JSONObject jb = new JSONObject(result);
                            int code=jb.optInt("code",-1);
                            String msg=jb.optString("msg");
                            JSONObject jsonObject=jb.getJSONObject("data");
                            if (code == 0) {
                                String captcha=jsonObject.optString("captcha");
                                EventBus.getDefault().post(CaptchaEvents.pullSuccess(true,msg,captcha,mark));
                            } else {
                                EventBus.getDefault().post(CaptchaEvents.pullFale(msg,mark));
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (Exception e){
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void failed(Call call, IOException e) {
                        if (!isRelease()) {
                            Log.i(TAG, "failed");
                        }
                        EventBus.getDefault().post(CaptchaEvents.pullFale("获取验证码失败",mark));
                    }
                });
            }
        });
    }


    /**
     * 退出
     * @param loginUserBean
     */
    public void doLoginOut(final LoginUserBean loginUserBean) {
        CApplication.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                //获取网络请求工具类实例
                OkhttpHelper netUtils = OkhttpHelper.getInstance();
                //提交数据
                netUtils.postDataAsynToNet(URLPathMaker.get_equal_url(AppConfig.API_LOGOUT, URLData.Key.VALID_API_TOKEN,loginUserBean.getApi_token()), null, new OkhttpHelper.MyNetCall() {
                    @Override
                    public void success(Call call, Response response) throws IOException {
                        if (!isRelease()) {
                            Log.i(TAG, "success");
                        }
                        try {
                            String result = response.body().string();
                            JSONObject jb = new JSONObject(result);
                            int code=jb.optInt("code",-1);
                            String msg=jb.optString("msg");
                            if (code == 0) {
                                LoginDao.deleteAll();
                                EventBus.getDefault().post(LoginOutEvents.pullSuccess(true,msg,loginUserBean));
                            } else {
                                EventBus.getDefault().post(LoginOutEvents.pullFale(msg));
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (Exception e){
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void failed(Call call, IOException e) {
                        if (!isRelease()) {
                            Log.i(TAG, "failed");
                        }
                        EventBus.getDefault().post(LoginOutEvents.pullFale("退出失败"));
                    }
                });
            }
        });
    }


    /**
     * 第三方登录
     */
    public void doOtherLoginPost(final String appkey, final String openid, final String accesstoken, final int sign,final String mark) {

        //构造请求参数
        final Map<String, String> reqBody = new ConcurrentSkipListMap<>();
//        if (!TextUtils.isEmpty(appkey)) {
//            reqBody.put("appkey", appkey);
//        }
        reqBody.put("appkey", appkey);
        reqBody.put("openid", openid);
        reqBody.put("accesstoken", accesstoken);
        CApplication.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                //获取网络请求工具类实例
                OkhttpHelper netUtils = OkhttpHelper.getInstance();
                //提交数据
                String url="";
                if (sign==1) {
                    url=AppConfig.API_LOGIN_WECHAT;
                } else if (sign == 2) {
                    url=AppConfig.API_LOGIN_QQ;
                } else {
                    url=AppConfig.API_LOGIN_WEIBO;
                }
                netUtils.postDataAsynToNet(url, reqBody, new OkhttpHelper.MyNetCall() {
                    @Override
                    public void success(Call call, Response response) throws IOException {
                        if (!isRelease()) {
                            Log.i(TAG, "success");
                        }
                        try {
                            String result = response.body().string();
                            JSONObject jb = new JSONObject(result);
                            int code=jb.optInt("code",-1);
                            String msg=jb.optString("msg");
                            if (code == 0) {
                                JSONObject jsonObject=jb.getJSONObject("data");
                                String api_token=jsonObject.optString("api_token");

                                JSONObject object=jsonObject.getJSONObject("user");
                                LoginUserBean loginUserBean= ObjectMaker.convert(object,LoginUserBean.class);
                                if (loginUserBean==null) {
                                    loginUserBean=new LoginUserBean();
                                }
                                loginUserBean.setApi_token(api_token);
                                loginUserBean.setStatus(LoginUserBean.STATUS_ONLINE);
                                LoginDao.deleteAll();
                                LoginDao.insertUser(loginUserBean);
                                EventBus.getDefault().post(LoginPageEvents.pullSuccess(true,msg,loginUserBean,mark));
                            } else {
                                EventBus.getDefault().post(LoginPageEvents.pullFale(msg,mark));
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (Exception e){
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void failed(Call call, IOException e) {
                        if (!isRelease()) {
                            Log.i(TAG, "failed");
                        }
                        EventBus.getDefault().post(LoginPageEvents.pullFale("登录失败",mark));
                    }
                });
            }
        });
    }

}
